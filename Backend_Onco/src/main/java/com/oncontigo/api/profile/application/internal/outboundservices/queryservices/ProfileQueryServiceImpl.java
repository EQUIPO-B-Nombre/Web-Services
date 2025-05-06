package com.oncontigo.api.profile.application.internal.outboundservices.queryservices;

import com.oncontigo.api.profile.domain.model.aggregates.Profile;
import com.oncontigo.api.profile.domain.model.queries.GetAllProfilesQuery;
import com.oncontigo.api.profile.domain.model.queries.GetProfileByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetProfileByUserIdQuery;
import com.oncontigo.api.profile.domain.services.ProfileQueryService;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.id());
    }

    @Override
    public List<Profile> handle(GetAllProfilesQuery query) {
        return profileRepository.findAll();
    }

    @Override
    public Optional<Profile> handle(GetProfileByUserIdQuery query) {
        return profileRepository.findByUser_Id(query.userId());
    }
}