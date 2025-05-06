package com.oncontigo.api.profile.application.internal.outboundservices.commandservices;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.domain.exceptions.DoctorNotFoundException;
import com.oncontigo.api.profile.domain.exceptions.SameUserException;
import com.oncontigo.api.profile.domain.model.commands.CreateDoctorCommand;
import com.oncontigo.api.profile.domain.model.commands.DeleteDoctorCommand;
import com.oncontigo.api.profile.domain.model.commands.UpdateDoctorCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.services.DoctorCommandService;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorCommandServiceImpl implements DoctorCommandService {
    private final DoctorRepository doctorRepository;

    public DoctorCommandServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Long handle(CreateDoctorCommand command, User user) {
        var sameUser = doctorRepository.findByUser_Id(command.userId());
        if (sameUser.isPresent()) {
            throw new SameUserException(command.userId());
        }
        Doctor doctor = new Doctor(user);
        doctorRepository.save(doctor);
        return doctor.getId();
    }

    @Override
    public Optional<Doctor> handle(UpdateDoctorCommand command) {
        var dentist = doctorRepository.findById(command.id());
        if (dentist.isEmpty()) {
            return Optional.empty();
        }
        var dentistToUpdate = dentist.get();
        Doctor updatedDoctor = doctorRepository.save(dentistToUpdate.update(command));
        return Optional.of(updatedDoctor);
    }

    @Override
    public void handle(DeleteDoctorCommand command) {
        var dentist = doctorRepository.findById(command.id());
        if (dentist.isEmpty()) {
            throw new DoctorNotFoundException(command.id());
        }
        doctorRepository.delete(dentist.get());
    }
}