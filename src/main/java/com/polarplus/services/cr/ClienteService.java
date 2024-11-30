package com.polarplus.services.cr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.polarplus.domain.cr.Cliente;
import com.polarplus.domain.enums.TipoCliente;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.cr.FiltersClientesDTO;
import com.polarplus.repositories.cr.ClienteRepository;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    @Autowired
    private final ClienteRepository repository;

    public PaginationUtil.PaginatedResponse<Cliente> getAll(PaginationDTO pagination,
            FiltersClientesDTO filters) {
        // Criar Pageable para a consulta paginada
        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(Sort.Direction.fromString(pagination.getDirection()), pagination.getSortBy()));

        // Chamar o repositório para buscar os clientes com o filtro e paginação
        Page<Cliente> clientesPage = repository.findByTermo(filters.termo(), pageable);

        // Retornar os resultados paginados
        return new PaginationUtil.PaginatedResponse<>(
                clientesPage.getContent(), // Lista de itens da página
                clientesPage.getPageable().getPageNumber(), // Página atual
                clientesPage.getPageable().getPageSize(), // Tamanho da página
                (int) clientesPage.getTotalElements() // Total de elementos encontrados
        );
    }

    public Cliente getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrada"));
    }

    public Cliente insertOne(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório");
        }
        if (cliente.getEndereco() == null || cliente.getEndereco().isBlank()) {
            throw new IllegalArgumentException("O endereço do cliente é obrigatório");
        }
        if (cliente.getTipo() == null) {
            throw new IllegalArgumentException("O tipo de cliente é obrigatório");
        }
        if (cliente.getTipo().equals(TipoCliente.empresa) && cliente.getCnpj() == null) {
            throw new IllegalArgumentException("CNPJ é obrigatório para clientes de tipo 'empresa'");
        }
        if (cliente.getTipo().equals(TipoCliente.pessoa) && cliente.getNumero() == null) {
            throw new IllegalArgumentException("Número é obrigatório para clientes de tipo 'pessoa'");
        }

        if (cliente.getCnpj() != null && repository.existsByCnpj(cliente.getCnpj())) {
            throw new IllegalArgumentException("Já existe um cliente com este CNPJ.");
        }
        if (cliente.getNumero() != null && repository.existsByNumero(cliente.getNumero())) {
            throw new IllegalArgumentException("Já existe um cliente com este número.");
        }
        if (repository.existsByEndereco(cliente.getEndereco())) {
            throw new IllegalArgumentException("Já existe um cliente com este endereço.");
        }

        return repository.save(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório");
        }
        if (cliente.getEndereco() == null || cliente.getEndereco().isBlank()) {
            throw new IllegalArgumentException("O endereço do cliente é obrigatório");
        }
        if (cliente.getTipo() == null) {
            throw new IllegalArgumentException("O tipo de cliente é obrigatório");
        }
        if (cliente.getTipo().equals(TipoCliente.empresa) && cliente.getCnpj() == null) {
            throw new IllegalArgumentException("CNPJ é obrigatório para clientes de tipo 'empresa'");
        }
        if (cliente.getTipo().equals(TipoCliente.pessoa) && cliente.getNumero() == null) {
            throw new IllegalArgumentException("Número é obrigatório para clientes de tipo 'pessoa'");
        }

        return repository.save(cliente);
    }

}
