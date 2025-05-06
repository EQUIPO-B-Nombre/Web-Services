package com.oncontigo.api.profile.domain.services;

import com.oncontigo.api.profile.domain.model.commands.CreateProfileCommand;
import com.oncontigo.api.profile.domain.model.commands.DeleteProfileCommand;
import com.oncontigo.api.profile.domain.model.commands.UpdateProfileCommand;
import com.oncontigo.api.profile.domain.model.aggregates.Profile;

import java.util.Optional;

public interface ProfileCommandService {
    Long handle(CreateProfileCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
    void handle(DeleteProfileCommand command);
}