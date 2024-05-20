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
public class PackagesService {
    private final SenderRepoComposite senderRepo;
    private final PackageDataRepo packageDataRepo;

    public PackagesService(SenderRepoComposite senderRepo, PackageDataRepo packageDataRepo) {
        this.senderRepo = senderRepo;
        this.packageDataRepo = packageDataRepo;
    }

    public void sendPackage(SendPackageCommand command) {
        Optional<Person> receiverOpt = senderRepo.findByEmail(command.receiverEmail);
        Optional<Person> senderOpt = senderRepo.findByEmail(command.senderEmail);

        if (receiverOpt.isEmpty()) {
            throw new IllegalArgumentException("Receiver %s is not valid".formatted(command.receiverEmail));
        }

        if (senderOpt.isEmpty()) {
            throw new IllegalArgumentException("Sender %s is not valid".formatted(command.senderEmail));
        }

        Person receiver = receiverOpt.get();
        Person sender = senderOpt.get();

        Package p = new Package();
        p.setReceiver(receiver);
        p.setSender(sender);
        p.setPackageSize(command.packageSize);
        p.setPackgeWeight(command.packgeWeight);

        packageDataRepo.save(p);
    }
}
