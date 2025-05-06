package com.oncontigo.api.profile.interfaces.rest.transform;

import com.oncontigo.api.profile.domain.model.aggregates.Profile;
import com.oncontigo.api.profile.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile profile) {
        return new ProfileResource(
                profile.getId(),
                profile.getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getCity(),
                profile.getCountry(),
                profile.getBirthDate(),
                profile.getDescription(),
                profile.getPhoto(),
                profile.getExperience(),
                profile.getDni(),
                profile.getPhone()
        );
    }
}