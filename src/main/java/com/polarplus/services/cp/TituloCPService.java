package com.polarplus.services.cp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Cartao;
import com.polarplus.domain.Categoria;
import com.polarplus.domain.ContaBancaria;
import com.polarplus.domain.Empresa;
import com.polarplus.domain.FormaPagamento;
import com.polarplus.domain.cp.Fornecedor;
import com.polarplus.domain.cp.StatusCP;
import com.polarplus.domain.cp.TituloCP;
import com.polarplus.domain.cp.Vencimento;
import com.polarplus.domain.user.User;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.cp.FiltersTitulosCPDTO;
import com.polarplus.dto.filters.cp.ResponseTituloCPGetAllDTO;
import com.polarplus.dto.filters.cp.TitulosCPDTO;
import com.polarplus.dto.filters.cp.VencimentosDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.infra.context.UserContext;
import com.polarplus.repositories.CartaoRepository;
import com.polarplus.repositories.CategoriaRepository;
import com.polarplus.repositories.ContaBancariaRepository;
import com.polarplus.repositories.FormaPagamentoRepository;
import com.polarplus.repositories.cp.FornecedorRepository;
import com.polarplus.repositories.cp.StatusCPRepository;
import com.polarplus.repositories.cp.TituloCPRepository;
import com.polarplus.repositories.cp.VencimentoRepository;
import com.polarplus.utils.PaginationUtil;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TituloCPService {

    // private static final Logger logger =
    // LoggerFactory.getLogger(AuthController.class);

    private final TituloCPRepository repository;
    private final StatusCPRepository statusRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final CategoriaRepository categoriaRepository;
    private final VencimentoRepository vencimentoRepository;
    private final CartaoRepository cartaorepository;

    private final EmpresaContext empresaContext;
    private final UserContext userContext;

    public PaginationUtil.PaginatedResponse<ResponseTituloCPGetAllDTO> getAll(PaginationDTO pagination,
            FiltersTitulosCPDTO filters) {
        // Criar Pageable para a consulta paginada
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        Empresa empresa = empresaContext.getEmpresa();

        Page<Tuple> titulosPage = repository.getAllByEmpresa(
                empresa.getId(),
                filters.idFormaPagamento(),
                filters.idStatus(),
                filters.idFornecedor(),
                filters.idCategoria(),
                filters.idContaBancaria(),
                pageable);

        List<ResponseTituloCPGetAllDTO> dtos = titulosPage.getContent().stream()
                .map(tuple -> new ResponseTituloCPGetAllDTO(
                        tuple.get("id", Long.class),
                        tuple.get("descricao", String.class),
                        tuple.get("categoria", String.class),
                        tuple.get("status", String.class),
                        tuple.get("valor", BigDecimal.class),
                        tuple.get("cor", String.class),
                        tuple.get("dataTitulo", Timestamp.class)

                ))
                .collect(Collectors.toList());

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                dtos,
                titulosPage.getPageable().getPageNumber(),
                titulosPage.getPageable().getPageSize(),
                (int) titulosPage.getTotalElements());
    }

    public TituloCP getOne(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        return repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Titulo não encontrado"));
    }

    @Transactional
    public TituloCP insertOne(TitulosCPDTO titulo) {
        try {
            List<VencimentosDTO> vencimentos = titulo.vencimentos();

            // * Validação de campos obrigatórios do título
            if (titulo.idFornecedor() == null) {
                throw new IllegalArgumentException("Fornecedor não informado");
            }
            if (titulo.idFormaPagamento() == null) {
                throw new IllegalArgumentException("Forma de recebimento não informada");
            }
            if (titulo.idContaBancaria() == null) {
                throw new IllegalArgumentException("Conta bancária não informada");
            }
            if (titulo.descricao() == null || titulo.descricao().isBlank()) {
                throw new IllegalArgumentException("A descrição do título é obrigatório");
            }
            if (titulo.valor() == null || titulo.valor().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("O valor do título é inválido");
            }

            // * Validando erro de quantidade de vencimentos inválida
            if (vencimentos.size() > 10) {
                throw new IllegalArgumentException("O número de vencimentos não pode exceder 10");
            }

            // * Validando erro total de vencimentos inválido
            BigDecimal totalVencimentos = vencimentos.stream()
                    .map(VencimentosDTO::valor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalVencimentos.compareTo(titulo.valor()) == 1) {
                throw new IllegalArgumentException("O valor dos vencimentos excede o valor do título");
            }

            // * Validando erro de data de vencimento inválida
            boolean hasInvalidDate = vencimentos.stream()
                    .anyMatch(pagamento -> pagamento.dataVencimento().isBefore(LocalDate.now()));

            if (hasInvalidDate) {
                throw new RuntimeException(
                        "Existe ao menos um pagamento com data de vencimento menor que a data de criação do título.");
            }

            Empresa empresa = empresaContext.getEmpresa();
            Categoria categoria = categoriaRepository.findById(titulo.idCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            User user = userContext.getUser();
            StatusCP status = statusRepository.findById(Long.parseLong("10"))
                    .orElseThrow(() -> new RuntimeException("Status não encontrado"));

            Fornecedor cliente = fornecedorRepository.findByIdAndEmpresa(titulo.idFornecedor(), empresa)
                    .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
            FormaPagamento formaPagamento = formaPagamentoRepository
                    .findByIdAndEmpresa(titulo.idFormaPagamento(), empresa)
                    .orElseThrow(() -> new RuntimeException("Forma de recebimento não encontrada"));
            ContaBancaria contaBancaria = contaBancariaRepository.findByIdAndEmpresa(titulo.idContaBancaria(), empresa)
                    .orElseThrow(() -> new RuntimeException("Conta bancária não encontrado"));

            Cartao cartao = null;
            if (titulo.idCartao() != null) {
                cartao = cartaorepository.findByIdAndEmpresa(titulo.idCartao(), empresa)
                        .orElseThrow(() -> new RuntimeException("Conta bancária não encontrado"));
            }

            TituloCP newTitulo = new TituloCP();
            BeanUtils.copyProperties(titulo, newTitulo);
            newTitulo.setStatus(status);
            newTitulo.setFornecedor(cliente);
            newTitulo.setFormaPagamento(formaPagamento);
            newTitulo.setUser(user);
            newTitulo.setContaBancaria(contaBancaria);
            newTitulo.setEmpresa(empresa);
            newTitulo.setCategoria(categoria);
            newTitulo.setCartao(cartao);

            TituloCP savedTitulo = repository.save(newTitulo);
            // Salva os vencimentos
            for (VencimentosDTO vencimento : vencimentos) {
                Vencimento newVencimento = new Vencimento();
                BeanUtils.copyProperties(vencimento, newVencimento);
                newVencimento.setTitulo(savedTitulo);
                newVencimento.setEmpresa(empresa);
                vencimentoRepository.save(newVencimento);
            }

            return savedTitulo;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    // public TituloCP update(Long id, TituloCP titulo) {
    // if (titulo.getNome() == null || titulo.getNome().isBlank()) {
    // throw new IllegalArgumentException("O nome do titulo é obrigatório");
    // }

    // TituloCP tituloExistente = repository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Forma de recebimento não
    // encontrada"));

    // // Atualiza os campos permitidos
    // tituloExistente.setNome(titulo.getNome());

    // return repository.save(tituloExistente);
    // }

}
