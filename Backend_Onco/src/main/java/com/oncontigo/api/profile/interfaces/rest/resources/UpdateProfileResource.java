package com.oncontigo.api.profile.interfaces.rest.resources;

import java.time.LocalDate;

public record UpdateProfileResource(
                                    String firstName,
                                    String lastName,
                                    String city,
                                    String country,
                                    LocalDate birthDate,
                                    String description,
                                    String photo,
                                    Integer experience,
                                    String dni,
                                    String phone) {
}