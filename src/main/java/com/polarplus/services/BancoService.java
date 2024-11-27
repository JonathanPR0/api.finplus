package com.polarplus.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polarplus.domain.Banco;
import com.polarplus.repositories.BancoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BancoService {

    private final BancoRepository repository;

    public Banco getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco não encontrado"));
    }

    public List<Banco> getAll() {
        return repository.findAll();
    }

    public Banco insertOne(Banco banco) {
        // Validações, se necessário
        if (banco.getNome() == null || banco.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do banco é obrigatório");
        }
        if (banco.getCodigo() == null || banco.getCodigo().isBlank()) {
            throw new IllegalArgumentException("O código do banco é obrigatório");
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
        bancoExistente.setActive(bancoAtualizado.isActive());

        // Salva as alterações
        return repository.save(bancoExistente);
    }

}
