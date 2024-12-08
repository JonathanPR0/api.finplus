package com.polarplus.infra.context;

import org.springframework.stereotype.Component;

import com.polarplus.domain.user.User;
import com.polarplus.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserContext {

    private final HttpServletRequest request;
    private final UserRepository repository; // Removido 'final' do repository

    public UserContext(HttpServletRequest request, UserRepository repository) {
        this.request = request;
        this.repository = repository; // Agora o repository é injetado corretamente pelo Spring
    }

    public User getUser() {
        Object idUserObj = request.getAttribute("id_user");
        if (idUserObj != null && idUserObj instanceof Long) {
            Long idUser = (Long) idUserObj;
            return repository.findById(idUser).orElse(null);
        }
        return null;
    }
}
