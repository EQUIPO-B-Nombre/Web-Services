package com.oncontigo.api.profile.interfaces.rest.resources;

import java.math.BigDecimal;

public record DoctorResource(Long id,
                             Long userId,
                             BigDecimal experience) {
}
