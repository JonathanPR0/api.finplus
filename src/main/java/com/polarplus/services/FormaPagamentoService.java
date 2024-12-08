package com.polarplus.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.FormaPagamento;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.repositories.FormaPagamentoRepository;
import com.polarplus.utils.PaginationUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormaPagamentoService {

    @Autowired
    private final FormaPagamentoRepository repository;

    @Autowired
    private final EmpresaContext empresaContext;

    public PaginationUtil.PaginatedResponse<FormaPagamento> getAll(PaginationDTO pagination,
            FilterTermoDTO filters) {
        // Criar Pageable para a consulta paginada
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        Empresa empresa = empresaContext.getEmpresa();

        // Chamar o repositório para buscar os formaPagamentos com o filtro e
        // paginação
        Page<FormaPagamento> formaPagamentosPage = repository.findByTermo(filters.termo(), empresa.getId(),
                pageable);

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                formaPagamentosPage.getContent(), // Lista de itens da página
                formaPagamentosPage.getPageable().getPageNumber(), // Página atual
                formaPagamentosPage.getPageable().getPageSize(), // Tamanho da página
                (int) formaPagamentosPage.getTotalElements() // Total de elementos encontrados
        );
    }

    public FormaPagamento getOne(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        return repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrado"));
    }

    public FormaPagamento insertOne(FormaPagamento formaPagamento) {
        if (formaPagamento.getNome() == null || formaPagamento.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da forma de pagamento é obrigatório");
        }

        Empresa empresa = empresaContext.getEmpresa();

        FormaPagamento newFormaPagamento = new FormaPagamento();
        BeanUtils.copyProperties(formaPagamento, newFormaPagamento);
        newFormaPagamento.setEmpresa(empresa);
        newFormaPagamento.setNome(formaPagamento.getNome());

        return repository.save(newFormaPagamento);
    }

    public FormaPagamento update(Long id, FormaPagamento formaPagamento) {
        if (formaPagamento.getNome() == null || formaPagamento.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da forma de pagamento é obrigatório");
        }

        FormaPagamento formaPagamentoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        // Atualiza os campos permitidos
        formaPagamentoExistente.setNome(formaPagamento.getNome());

        return repository.save(formaPagamentoExistente);
    }

    @Transactional
    public void delete(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        try {
            repository.deleteByIdAndEmpresa(id, empresa);
        } catch (Error e) {
            throw new IllegalArgumentException("Não foi possível remover essa forma de pagamento");
        }

    }

}
