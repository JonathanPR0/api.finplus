package com.polarplus.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Categoria;
import com.polarplus.domain.Empresa;
import com.polarplus.domain.enums.TipoCategoria;
import com.polarplus.dto.CategoriaDTO;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FiltersCategoriaDTO;
import com.polarplus.infra.context.EmpresaContext;
import com.polarplus.repositories.CategoriaRepository;
import com.polarplus.utils.PaginationUtil;
import com.polarplus.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    @Autowired
    private final CategoriaRepository repository;
    @Autowired
    private final EmpresaContext empresaContext;

    public Categoria getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public PaginationUtil.PaginatedResponse<Categoria> getAll(PaginationDTO pagination,
            FiltersCategoriaDTO filters) {
        // Criar Pageable para a consulta paginada
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));
        TipoCategoria tipo = null; // Alterando para TipoCategoria em vez de String

        // Verificar se o tipo foi fornecido e é válido
        if (filters.tipo() != null && !filters.tipo().equals("all")) {
            try {
                // Verifica se o tipo é um valor válido dentro do enum
                tipo = TipoCategoria.valueOf(filters.tipo()); // Não precisa mais do .toString()
            } catch (IllegalArgumentException e) {
                // Tipo inválido, lança uma exceção com a mensagem adequada
                throw new IllegalArgumentException("Tipo inválido: " + filters.tipo());
            }
        }

        Empresa empresa = empresaContext.getEmpresa();

        Page<Categoria> categoriasPage = repository.findByNomeAndTipo(Utils.removerAcentos(filters.nome()), tipo,
                empresa.getId(), pageable);

        return new PaginationUtil.PaginatedResponse<>(
                categoriasPage.getContent(), // Lista de itens da página
                categoriasPage.getPageable().getPageNumber(), // Página atual
                categoriasPage.getPageable().getPageSize(), // Tamanho da página
                (int) categoriasPage.getTotalElements() // Total de elementos encontrados
        );
    }

    public Categoria insertOne(CategoriaDTO categoriaDTO) {
        if (categoriaDTO.nome() == null || categoriaDTO.nome().isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório");
        }
        if (categoriaDTO.descricao() == null || categoriaDTO.descricao().isBlank()) {
            throw new IllegalArgumentException("O descrição da categoria é obrigatório");
        }
        TipoCategoria tipo = null;
        try {
            tipo = TipoCategoria.valueOf(categoriaDTO.tipo());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("O tipo da categoria é inválido");
        }
        if (categoriaDTO.tipo() == null) {
            throw new IllegalArgumentException("O tipo da categoria é obrigatório");
        }
        if (repository.existsByDescricao(categoriaDTO.descricao())) {
            throw new IllegalArgumentException("Já existe uma descrição com este nome.");
        }
        Empresa empresa = empresaContext.getEmpresa();

        Categoria categoria = new Categoria();
        BeanUtils.copyProperties(categoriaDTO, categoria);
        categoria.setTipo(tipo);
        categoria.setEmpresa(empresa);
        return repository.save(categoria);
    }

    public Categoria update(Long id, CategoriaDTO categoriaDTO) {
        if (categoriaDTO.nome() == null || categoriaDTO.nome().isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório");
        }
        if (categoriaDTO.descricao() == null || categoriaDTO.descricao().isBlank()) {
            throw new IllegalArgumentException("O descrição da categoria é obrigatório");
        }

        Categoria categoriaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        BeanUtils.copyProperties(categoriaDTO, categoriaExistente, "id");
        return repository.save(categoriaExistente);
    }

}
