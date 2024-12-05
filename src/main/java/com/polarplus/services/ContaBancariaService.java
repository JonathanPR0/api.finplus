package com.polarplus.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Banco;
import com.polarplus.domain.ContaBancaria;
import com.polarplus.domain.Empresa;
import com.polarplus.dto.ContaBancariaDTO;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FiltersContaBancariaDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.repositories.BancoRepository;
import com.polarplus.repositories.ContaBancariaRepository;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    @Autowired
    private final ContaBancariaRepository repository;

    @Autowired
    private final BancoRepository bancoRepository;

    @Autowired
    private final EmpresaContext empresaContext;

    public ContaBancaria getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
    }

    public PaginationUtil.PaginatedResponse<ContaBancaria> getAll(PaginationDTO pagination,
            FiltersContaBancariaDTO filters) {
        // Configurar a paginação
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        Empresa empresa = empresaContext.getEmpresa();

        // Verificar se o termo é válido e buscar os dados
        Page<ContaBancaria> contasBancariasPage = repository.findByTermo(filters.termo(), filters.idBanco(),
                empresa.getId(), pageable); // Com
        // filtro

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                contasBancariasPage.getContent(), // Lista de itens da página
                contasBancariasPage.getPageable().getPageNumber(), // Página atual
                contasBancariasPage.getPageable().getPageSize(), // Tamanho da página
                (int) contasBancariasPage.getTotalElements() // Total de elementos encontrados
        );
    }

    public ContaBancaria insertOne(ContaBancariaDTO contaBancariaDTO) {
        // Validações, se necessário
        if (contaBancariaDTO.descricao() == null || contaBancariaDTO.descricao().isBlank()) {
            throw new IllegalArgumentException("O nome do conta bancária é obrigatório");
        }
        if (contaBancariaDTO.conta() == null || contaBancariaDTO.conta().isBlank()) {
            throw new IllegalArgumentException("O código da conta bancária é obrigatório");
        }
        if (contaBancariaDTO.agencia() == null || contaBancariaDTO.agencia().isBlank()) {
            throw new IllegalArgumentException("A agência da conta bancária é obrigatória");
        }
        if (repository.existsByDescricao(contaBancariaDTO.descricao())) {
            throw new IllegalArgumentException("Já existe um conta bancária com este nome.");
        }
        Empresa empresa = empresaContext.getEmpresa();

        Banco banco = bancoRepository.findById(contaBancariaDTO.idBanco())
                .orElseThrow(() -> new RuntimeException("Banco não encontrado"));

        // Criando uma nova instância de ContaBancaria
        ContaBancaria contaBancaria = new ContaBancaria();

        // Usando BeanUtils para copiar propriedades do DTO para a entidade
        BeanUtils.copyProperties(contaBancariaDTO, contaBancaria);

        // Associando o banco
        contaBancaria.setBanco(banco);
        contaBancaria.setEmpresa(empresa);

        // Salva no repositório
        return repository.save(contaBancaria);
    }

    public ContaBancaria update(Long id, ContaBancariaDTO contaBancariaDTO) {
        // Validações, se necessário
        if (contaBancariaDTO.descricao() == null || contaBancariaDTO.descricao().isBlank()) {
            throw new IllegalArgumentException("O nome do conta bancária é obrigatório");
        }
        if (contaBancariaDTO.conta() == null || contaBancariaDTO.conta().isBlank()) {
            throw new IllegalArgumentException("O código da conta bancária é obrigatório");
        }
        if (contaBancariaDTO.agencia() == null || contaBancariaDTO.agencia().isBlank()) {
            throw new IllegalArgumentException("A agência da conta bancária é obrigatória");
        }

        // Busca a conta bancária existente pelo ID
        ContaBancaria contaBancariaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        // Verifica se o banco associado existe
        Banco banco = bancoRepository.findById(contaBancariaDTO.idBanco())
                .orElseThrow(() -> new RuntimeException("Banco não encontrado"));

        // Atualiza os campos da entidade existente
        BeanUtils.copyProperties(contaBancariaDTO, contaBancariaExistente, "id", "banco");

        // Associa o banco à entidade
        contaBancariaExistente.setBanco(banco);

        // Salva as alterações no banco de dados
        return repository.save(contaBancariaExistente);
    }

}
