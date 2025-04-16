package com.polarplus.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Banco;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.repositories.BancoRepository;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BancoService {

    private final BancoRepository repository;

    public PaginationUtil.PaginatedResponse<Banco> getAll(PaginationDTO pagination,
            FilterTermoDTO filters) {

        // Configurar a paginação
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        // Verificar se o termo é válido e buscar os dados
        Page<Banco> bancosPage = repository.findByTermo(filters.termo(), pageable);

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                bancosPage.getContent(), // Lista de itens da página
                bancosPage.getPageable().getPageNumber(), // Página atual
                bancosPage.getPageable().getPageSize(), // Tamanho da página
                (int) bancosPage.getTotalElements() // Total de elementos encontrados
        );

    }

    public Banco getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco não encontrado"));
    }

    public Banco insertOne(Banco banco) {
        // Validações, se necessário
        if (banco.getNome() == null || banco.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do banco é obrigatório");
        }
        if (banco.getImg() == null || banco.getImg().isBlank()) {
            throw new IllegalArgumentException("A imagem do banco é obrigatória");
        }
        if (repository.existsByNome(banco.getNome())) {
            throw new IllegalArgumentException("Já existe um banco com este nome.");
        }
        if (repository.existsByCodigo(banco.getCodigo())) {
            throw new IllegalArgumentException("Já existe um banco com este código.");
        }

        // Salva no repositório
        return repository.save(banco);
    }

    public Banco update(Long id, Banco bancoAtualizado) {
        // Recupera o banco existente
        Banco bancoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco não encontrado"));

        // Atualiza os campos permitidos
        bancoExistente.setNome(bancoAtualizado.getNome());
        bancoExistente.setCodigo(bancoAtualizado.getCodigo());
        bancoExistente.setImg(bancoAtualizado.getImg());
        bancoExistente.setActive(bancoAtualizado.isActive());

        // Salva as alterações
        return repository.save(bancoExistente);
    }

}
