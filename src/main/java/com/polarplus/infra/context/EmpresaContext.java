package com.polarplus.infra.context;

import org.springframework.stereotype.Component;

import com.polarplus.domain.Empresa;
import com.polarplus.repositories.EmpresaRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class EmpresaContext {

    private final HttpServletRequest request;
    private final EmpresaRepository repository; // Removido 'final' do repository

    public EmpresaContext(HttpServletRequest request, EmpresaRepository repository) {
        this.request = request;
        this.repository = repository; // Agora o repository é injetado corretamente pelo Spring
    }

    public Empresa getEmpresa() {
        Object idEmpresaObj = request.getAttribute("id_empresa");
        if (idEmpresaObj != null && idEmpresaObj instanceof Long) {
            Long idEmpresa = (Long) idEmpresaObj;
            return repository.findById(idEmpresa).orElse(null);
        }
        return null;
    }
}
