package com.polarplus.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarplus.domain.Banco;
import com.polarplus.domain.ContaBancaria;
import com.polarplus.dto.ContaBancariaDTO;
import com.polarplus.repositories.BancoRepository;
import com.polarplus.repositories.ContaBancariaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    @Autowired
    private final ContaBancariaRepository repository;

    @Autowired
    private final BancoRepository bancoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ContaBancaria getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
    }

    public List<ContaBancaria> getAll() {
        return repository.findAll();
    }

    public ContaBancaria insertOne(ContaBancariaDTO contaBancariaDTO) {
        // Validações, se necessário
        if (contaBancariaDTO.getDescricao() == null || contaBancariaDTO.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O nome do conta bancária é obrigatório");
        }
        if (contaBancariaDTO.getConta() == null || contaBancariaDTO.getConta().isBlank()) {
            throw new IllegalArgumentException("O código da conta bancária é obrigatório");
        }
        if (contaBancariaDTO.getAgencia() == null || contaBancariaDTO.getAgencia().isBlank()) {
            throw new IllegalArgumentException("A agência da conta bancária é obrigatória");
        }
        if (repository.existsByDescricao(contaBancariaDTO.getDescricao())) {
            throw new IllegalArgumentException("Já existe um conta bancária com este nome.");
        }

        Banco banco = BancoRepository.findById(contaBancaria.idBanco())
                .orElseThrow(() -> new RuntimeException("Banco não encontrado"));
        ContaBancaria contaBancaria = modelMapper.INSTANCE.toContaBancaria(request);
        contaBancaria.setBanco(banco);
        // Salva no repositório
        return repository.save(contaBancaria);
    }

    public ContaBancaria update(Long id, ContaBancaria contaBancariaAtualizado) {
        // Recupera o contaBancaria existente
        ContaBancaria contaBancariaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrado"));

        // Atualiza os campos permitidos
        contaBancariaExistente.setBanco(contaBancariaAtualizado.getBanco());

        contaBancariaExistente.setAgencia(contaBancariaAtualizado.getAgencia());
        contaBancariaExistente.setDvAgencia(contaBancariaAtualizado.getDvAgencia());

        contaBancariaExistente.setConta(contaBancariaAtualizado.getConta());
        contaBancariaExistente.setDvConta(contaBancariaAtualizado.getDvConta());

        contaBancariaExistente.setDescricao(contaBancariaAtualizado.getDescricao());

        // Salva as alterações
        return repository.save(contaBancariaExistente);
    }

}
