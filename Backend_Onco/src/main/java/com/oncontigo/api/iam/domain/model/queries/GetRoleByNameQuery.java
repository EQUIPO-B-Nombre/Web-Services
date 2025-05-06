package com.oncontigo.api.iam.domain.model.queries;

import com.oncontigo.api.iam.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles name) {
}