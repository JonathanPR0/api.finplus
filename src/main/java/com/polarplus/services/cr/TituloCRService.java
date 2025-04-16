package com.polarplus.services.cr;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.controllers.AuthController;
import com.polarplus.domain.Categoria;
import com.polarplus.domain.ContaBancaria;
import com.polarplus.domain.Empresa;
import com.polarplus.domain.FormaPagamento;
import com.polarplus.domain.cr.Cliente;
import com.polarplus.domain.cr.PagamentoCR;
import com.polarplus.domain.cr.Servico;
import com.polarplus.domain.cr.StatusCR;
import com.polarplus.domain.cr.TituloCR;
import com.polarplus.domain.user.User;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.cr.FiltersTitulosCRDTO;
import com.polarplus.dto.filters.cr.PagamentosDTO;
import com.polarplus.dto.filters.cr.ResponseTituloCRGetAllDTO;
import com.polarplus.dto.filters.cr.ServicosDTO;
import com.polarplus.dto.filters.cr.TitulosCRDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.infra.context.UserContext;
import com.polarplus.repositories.CategoriaRepository;
import com.polarplus.repositories.ContaBancariaRepository;
import com.polarplus.repositories.FormaPagamentoRepository;
import com.polarplus.repositories.cr.ClienteRepository;
import com.polarplus.repositories.cr.PagamentoRepository;
import com.polarplus.repositories.cr.ServicoRepository;
import com.polarplus.repositories.cr.StatusCRRepository;
import com.polarplus.repositories.cr.TituloCRRepository;
import com.polarplus.utils.PaginationUtil;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TituloCRService {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TituloCRRepository repository;
    private final StatusCRRepository statusRepository;
    private final ClienteRepository clienteRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ServicoRepository servicoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final CategoriaRepository categoriaRepository;
    private final PagamentoRepository pagamentoRepository;

    private final EmpresaContext empresaContext;
    private final UserContext userContext;

    public PaginationUtil.PaginatedResponse<ResponseTituloCRGetAllDTO> getAll(PaginationDTO pagination,
            FiltersTitulosCRDTO filters) {
        // Criar Pageable para a consulta paginada
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        Empresa empresa = empresaContext.getEmpresa();

        Page<Tuple> titulosPage = repository.getAllByEmpresa(
                empresa.getId(),
                filters.idFormaRecebimento(),
                filters.idStatus(),
                filters.idCliente(),
                filters.idCategoria(),
                filters.idContaBancaria(),
                pageable);

        List<ResponseTituloCRGetAllDTO> dtos = titulosPage.getContent().stream()
                .map(tuple -> new ResponseTituloCRGetAllDTO(
                        tuple.get("id", Long.class),
                        tuple.get("descricao", String.class),
                        tuple.get("categoria", String.class),
                        tuple.get("status", String.class),
                        tuple.get("valor", BigDecimal.class),
                        tuple.get("cor", String.class),
                        tuple.get("dataServico", Date.class)

                ))
                .collect(Collectors.toList());

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                dtos,
                titulosPage.getPageable().getPageNumber(),
                titulosPage.getPageable().getPageSize(),
                (int) titulosPage.getTotalElements());
    }

    public TituloCR getOne(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        return repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Titulo não encontrado"));
    }

    @Transactional
    public TituloCR insertOne(TitulosCRDTO titulo) {
        try {
            List<PagamentosDTO> pagamentos = titulo.pagamentos();

            // * Validação de campos obrigatórios do título
            if (titulo.idCliente() == null) {
                throw new IllegalArgumentException("Cliente não informado");
            }
            if (titulo.idFormaRecebimento() == null) {
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

            // * Validando erro de quantidade de pagamentos inválida
            if (pagamentos.size() > 10) {
                throw new IllegalArgumentException("O número de pagamentos não pode exceder 10");
            }

            // * Validando erro total de pagamentos inválido
            BigDecimal totalPagamentos = pagamentos.stream()
                    .map(PagamentosDTO::valor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalPagamentos.compareTo(titulo.valor()) == 1) {
                throw new IllegalArgumentException("O valor dos pagamentos excede o valor do título");
            }

            // * Validando erro de data de vencimento inválida
            boolean hasInvalidDate = pagamentos.stream()
                    .anyMatch(pagamento -> pagamento.dataVencimento().isBefore(LocalDate.now()));

            if (hasInvalidDate) {
                throw new RuntimeException(
                        "Existe ao menos um pagamento com data de vencimento menor que a data de criação do título.");
            }

            ServicosDTO servico = titulo.servico();
            // * Validando erro de serviço nulo
            if (servico.idCategoria() == null) {
                throw new IllegalArgumentException("A categoria do serviço é obrigatória");
            }
            if (servico.dataServico() == null) {
                throw new IllegalArgumentException("A data do serviço é obrigatória");
            }
            if (servico.dataServicoFuturo() == null) {
                throw new IllegalArgumentException("A data do serviço futuro é obrigatória");
            }
            if (servico.dataExpiracaoGarantia() == null) {
                throw new IllegalArgumentException("A data de expiracao da garantia é obrigatória");
            }
            if (servico.descricao() == null || servico.descricao().isEmpty()) {
                throw new IllegalArgumentException("A descrição do serviço é obrigatória");
            }

            Empresa empresa = empresaContext.getEmpresa();
            Categoria categoria = categoriaRepository.findById(servico.idCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrado"));

            Servico newServico = new Servico();
            BeanUtils.copyProperties(servico, newServico);
            newServico.setCategoria(categoria);
            ;
            newServico.setEmpresa(empresa);

            Servico savedServico = servicoRepository.save(newServico);

            // Atualizar o Título com o Serviço já persistido

            User user = userContext.getUser();
            StatusCR status = statusRepository.findById(Long.parseLong("10"))
                    .orElseThrow(() -> new RuntimeException("Status não encontrado"));
            Cliente cliente = clienteRepository.findByIdAndEmpresa(titulo.idCliente(), empresa)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            FormaPagamento formaRecebimento = formaPagamentoRepository
                    .findByIdAndEmpresa(titulo.idFormaRecebimento(), empresa)
                    .orElseThrow(() -> new RuntimeException("Forma de recebimento não encontrada"));
            ContaBancaria contaBancaria = contaBancariaRepository.findByIdAndEmpresa(titulo.idContaBancaria(), empresa)
                    .orElseThrow(() -> new RuntimeException("Conta bancária não encontrado"));

            TituloCR newTitulo = new TituloCR();
            BeanUtils.copyProperties(titulo, newTitulo);
            newTitulo.setStatus(status);
            newTitulo.setCliente(cliente);
            newTitulo.setFormaRecebimento(formaRecebimento);
            newTitulo.setUser(user);
            newTitulo.setContaBancaria(contaBancaria);
            newTitulo.setServico(savedServico);
            newTitulo.setEmpresa(empresa);

            TituloCR savedTitulo = repository.save(newTitulo);
            // Salva os pagamentos
            for (PagamentosDTO pagamento : pagamentos) {
                PagamentoCR newPagamento = new PagamentoCR();
                BeanUtils.copyProperties(pagamento, newPagamento);
                newPagamento.setTitulo(savedTitulo);
                newPagamento.setEmpresa(empresa);
                pagamentoRepository.save(newPagamento);
            }

            return savedTitulo;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    // public TituloCR update(Long id, TituloCR titulo) {
    // if (titulo.getNome() == null || titulo.getNome().isBlank()) {
    // throw new IllegalArgumentException("O nome do titulo é obrigatório");
    // }

    // TituloCR tituloExistente = repository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Forma de recebimento não
    // encontrada"));

    // // Atualiza os campos permitidos
    // tituloExistente.setNome(titulo.getNome());

    // return repository.save(tituloExistente);
    // }

}
