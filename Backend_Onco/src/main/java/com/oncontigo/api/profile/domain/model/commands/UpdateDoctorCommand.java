package com.oncontigo.api.profile.domain.model.commands;

import java.math.BigDecimal;

public record UpdateDoctorCommand(Long id, BigDecimal experience) {
}
