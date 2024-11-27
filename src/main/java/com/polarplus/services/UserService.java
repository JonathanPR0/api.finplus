package com.polarplus.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.polarplus.domain.user.User;
import com.polarplus.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User getOne(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}