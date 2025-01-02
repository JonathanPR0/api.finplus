package com.polarplus.dto.security;

import com.polarplus.domain.user.User;

public record ResponseDTO(User user, String token) {
}
