package com.polarplus.controllers;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.user.User;
import com.polarplus.dto.security.LoginRequestDTO;
import com.polarplus.dto.security.RegisterRequestDTO;
import com.polarplus.dto.security.ResponseDTO;
import com.polarplus.infra.security.TokenService;
import com.polarplus.repositories.EmpresaRepository;
import com.polarplus.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/validate-token")
    public ResponseEntity<?> getMethodName(@RequestParam String token) {
        try {
            String login = this.tokenService.validateToken(token);
            return ResponseEntity.ok().body(Map.of("isValid", true || (login.isBlank() && login.isEmpty())));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        try {
            User user = this.repository.findByEmail(body.email())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
            if (!passwordEncoder.matches(body.password(), user.getPassword())) {
                throw new RuntimeException("Usuário ou senha incorretos!");
            }
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user, token));

        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // @PreAuthorize("has('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body) {
        try {

            Optional<User> user = this.repository.findByEmail(body.email());
            if (user.isEmpty()) {
                User newUser = new User();
                newUser.setPassword(passwordEncoder.encode(body.password()));
                newUser.setEmail(body.email());
                newUser.setName(body.name());
                Empresa empresa = empresaRepository.findById(body.idEmpresa())
                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
                newUser.setEmpresa(empresa);

                this.repository.save(newUser);

                String token = this.tokenService.generateToken(newUser);
                return ResponseEntity.ok(new ResponseDTO(newUser, token));
            }
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}