package com.oncontigo.api.iam.domain.services;

import com.oncontigo.api.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}