package com.oncontigo.api.iam.application.internal.commandservices;

import static org.mockito.Mockito.*;

import com.oncontigo.api.iam.domain.model.commands.SeedRolesCommand;
import com.oncontigo.api.iam.domain.model.valueobjects.Roles;
import com.oncontigo.api.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.junit.jupiter.api.Test;

class RoleCommandServiceImplTest {
    // Verify all roles from Roles enum are created when none exist in the database
    @Test
    public void test_handle_creates_all_roles_when_none_exist() {
        // Arrange
        RoleRepository roleRepository = mock(RoleRepository.class);
        when(roleRepository.existsByName(any(Roles.class))).thenReturn(false);

        RoleCommandServiceImpl roleCommandService = new RoleCommandServiceImpl(roleRepository);
        SeedRolesCommand command = new SeedRolesCommand();

        // Act
        roleCommandService.handle(command);

        // Assert
        for (Roles role : Roles.values()) {
            verify(roleRepository).existsByName(role);
            verify(roleRepository).save(argThat(r -> r.getName() == role));
        }
    }
}