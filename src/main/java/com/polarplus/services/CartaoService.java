package com.polarplus.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Cartao;
import com.polarplus.domain.Empresa;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.repositories.CartaoRepository;
import com.polarplus.utils.PaginationUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartaoService {

    @Autowired
    private final CartaoRepository repository;

    @Autowired
    private final EmpresaContext empresaContext;

    public PaginationUtil.PaginatedResponse<Cartao> getAll(PaginationDTO pagination,
            FilterTermoDTO filters) {
        // Criar Pageable para a consulta paginada
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        Empresa empresa = empresaContext.getEmpresa();

        // Chamar o repositório para buscar os FormaPagamentos com o filtro e
        // paginação
        Page<Cartao> FormaPagamentosPage = repository.findByTermo(filters.termo(), empresa.getId(),
                pageable);

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                FormaPagamentosPage.getContent(), // Lista de itens da página
                FormaPagamentosPage.getPageable().getPageNumber(), // Página atual
                FormaPagamentosPage.getPageable().getPageSize(), // Tamanho da página
                (int) FormaPagamentosPage.getTotalElements() // Total de elementos encontrados
        );
    }

    public Cartao getOne(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        return repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
    }

    public Cartao insertOne(Cartao FormaPagamento) {
        if (FormaPagamento.getDescricao() == null || FormaPagamento.getDescricao().isBlank()) {
            throw new IllegalArgumentException("A descricao do cartão é obrigatório");
        }

        Empresa empresa = empresaContext.getEmpresa();

        Cartao newFormaPagamento = new Cartao();
        BeanUtils.copyProperties(FormaPagamento, newFormaPagamento);
        newFormaPagamento.setEmpresa(empresa);
        newFormaPagamento.setDescricao(FormaPagamento.getDescricao());

        return repository.save(newFormaPagamento);
    }

    public Cartao update(Long id, Cartao FormaPagamento) {
        if (FormaPagamento.getDescricao() == null || FormaPagamento.getDescricao().isBlank()) {
            throw new IllegalArgumentException("A descricao do cartão é obrigatório");
        }

        Cartao FormaPagamentoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Atualiza os campos permitidos
        FormaPagamentoExistente.setDescricao(FormaPagamento.getDescricao());

        return repository.save(FormaPagamentoExistente);
    }

    @Transactional
    public void delete(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        try {
            repository.deleteByIdAndEmpresa(id, empresa);
        } catch (Error e) {
            throw new IllegalArgumentException("Não foi possível remover esse cartão");
        }

    }

}
