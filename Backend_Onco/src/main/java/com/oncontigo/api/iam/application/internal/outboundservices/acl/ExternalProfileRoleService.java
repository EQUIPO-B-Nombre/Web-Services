package com.oncontigo.api.iam.application.internal.outboundservices.acl;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalProfileRoleService {
    private final ProfilesContextFacade profilesContextFacade;

    public ExternalProfileRoleService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    public Long createPatient(Long userId, User user) {
        return profilesContextFacade.createPatient(userId, user);
    }

    public Long createDentist(Long userId, User user) {
        return profilesContextFacade.createDentist(userId, user);
    }

}
