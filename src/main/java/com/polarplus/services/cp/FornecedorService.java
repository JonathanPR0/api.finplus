package com.polarplus.services.cp;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.cp.Fornecedor;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.repositories.cp.FornecedorRepository;
import com.polarplus.utils.PaginationUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    @Autowired
    private final FornecedorRepository repository;

    @Autowired
    private final EmpresaContext empresaContext;

    public PaginationUtil.PaginatedResponse<Fornecedor> getAll(PaginationDTO pagination,
            FilterTermoDTO filters) {
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        Empresa empresa = empresaContext.getEmpresa();

        Page<Fornecedor> fornecedorsPage = repository.findByTermo(filters.termo(), empresa.getId(), pageable);

        return new PaginationUtil.PaginatedResponse<>(
                fornecedorsPage.getContent(), // Lista de itens da página
                fornecedorsPage.getPageable().getPageNumber(), // Página atual
                fornecedorsPage.getPageable().getPageSize(), // Tamanho da página
                (int) fornecedorsPage.getTotalElements() // Total de elementos encontrados
        );
    }

    public Fornecedor getOne(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        return repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
    }

    public Fornecedor insertOne(Fornecedor fornecedor) {
        if (fornecedor.getNome() == null || fornecedor.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do fornecedor é obrigatório");
        }

        Empresa empresa = empresaContext.getEmpresa();

        Fornecedor newFornecedor = new Fornecedor();
        BeanUtils.copyProperties(fornecedor, newFornecedor);
        newFornecedor.setEmpresa(empresa);

        return repository.save(newFornecedor);
    }

    public Fornecedor update(Long id, Fornecedor fornecedor) {
        if (fornecedor.getNome() == null || fornecedor.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do fornecedor é obrigatório");
        }

        Fornecedor fornecedorExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        fornecedorExistente.setNome(fornecedor.getNome());
        return repository.save(fornecedorExistente);
    }

    @Transactional
    public void delete(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        try {
            repository.deleteByIdAndEmpresa(id, empresa);
        } catch (Error e) {
            throw new IllegalArgumentException("Não foi possível remover esse fornecedor");
        }

    }

}
