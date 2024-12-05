package com.polarplus.services.cr;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.cr.FormaDeRecebimentoCR;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.repositories.cr.FormasDeRecebimentoCRRepository;
import com.polarplus.utils.PaginationUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormaDeRecebimentoCRService {

    @Autowired
    private final FormasDeRecebimentoCRRepository repository;

    @Autowired
    private final EmpresaContext empresaContext;

    public PaginationUtil.PaginatedResponse<FormaDeRecebimentoCR> getAll(PaginationDTO pagination,
            FilterTermoDTO filters) {
        // Criar Pageable para a consulta paginada
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        Empresa empresa = empresaContext.getEmpresa();

        // Chamar o repositório para buscar os formaDeRecebimentos com o filtro e
        // paginação
        Page<FormaDeRecebimentoCR> formaDeRecebimentosPage = repository.findByTermo(filters.termo(), empresa.getId(),
                pageable);

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                formaDeRecebimentosPage.getContent(), // Lista de itens da página
                formaDeRecebimentosPage.getPageable().getPageNumber(), // Página atual
                formaDeRecebimentosPage.getPageable().getPageSize(), // Tamanho da página
                (int) formaDeRecebimentosPage.getTotalElements() // Total de elementos encontrados
        );
    }

    public FormaDeRecebimentoCR getOne(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        return repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("FormaDeRecebimento não encontrado"));
    }

    public FormaDeRecebimentoCR insertOne(FormaDeRecebimentoCR formaDeRecebimento) {
        if (formaDeRecebimento.getNome() == null || formaDeRecebimento.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do formaDeRecebimento é obrigatório");
        }

        Empresa empresa = empresaContext.getEmpresa();

        FormaDeRecebimentoCR newFormaDeRecebimento = new FormaDeRecebimentoCR();
        BeanUtils.copyProperties(formaDeRecebimento, newFormaDeRecebimento);
        newFormaDeRecebimento.setEmpresa(empresa);
        newFormaDeRecebimento.setNome(formaDeRecebimento.getNome());

        return repository.save(newFormaDeRecebimento);
    }

    public FormaDeRecebimentoCR update(Long id, FormaDeRecebimentoCR formaDeRecebimento) {
        if (formaDeRecebimento.getNome() == null || formaDeRecebimento.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do formaDeRecebimento é obrigatório");
        }

        FormaDeRecebimentoCR formaDeRecebimentoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de Recebimento não encontrada"));

        // Atualiza os campos permitidos
        formaDeRecebimentoExistente.setNome(formaDeRecebimento.getNome());

        return repository.save(formaDeRecebimentoExistente);
    }

    @Transactional
    public void delete(Long id) {
        Empresa empresa = empresaContext.getEmpresa();
        repository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new RuntimeException("Forma de recebimento não encontrada"));

        try {
            repository.deleteByIdAndEmpresa(id, empresa);
        } catch (Error e) {
            throw new IllegalArgumentException("Não foi possível remover essa forma de recebimento");
        }

    }

}
