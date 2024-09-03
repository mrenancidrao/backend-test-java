package com.backendtestjava.model.dtos;

import com.backendtestjava.model.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
