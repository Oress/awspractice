package org.ipan.services;

import java.util.Optional;
import java.util.UUID;

import org.ipan.model.Package;
import org.ipan.model.Person;
import org.ipan.repositories.PackageDataRepo;
import org.ipan.repositories.SenderDataRepo;
import org.ipan.repositories.SenderRepoComposite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SenderService {
    private final SenderRepoComposite senderRepo;

    public SenderService(SenderRepoComposite senderRepo) {
        this.senderRepo = senderRepo;
    }

    public Person createNewSender(CreateSenderCommand command) {
        Optional<Person> person = senderRepo.findByEmail(command.senderEmail);

        if (person.isPresent()) {
            throw new IllegalArgumentException("Person with the email %s already exists".formatted(command.senderEmail));
        }

        Person p = new Person();
        p.setFname(command.firstName);
        p.setLname(command.lastName);
        p.setEmail(command.senderEmail);
        senderRepo.save(p);

        return p;
    }

    
}
