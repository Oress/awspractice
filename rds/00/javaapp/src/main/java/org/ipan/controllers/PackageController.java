package org.ipan.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import org.ipan.dto.SendPackageCommandDTO;
import org.ipan.model.PackageSize;
import org.ipan.services.PackagesService;
import org.ipan.services.SendPackageCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PackageController {
    private final PackagesService packagesService;

    public PackageController(PackagesService packagesService) {
        this.packagesService = packagesService;
    }

    @PostMapping("sendPackage")
    @Transactional
    public ResponseEntity<?> sendPackage(@RequestBody SendPackageCommandDTO dto) {
        
        SendPackageCommand command = new SendPackageCommand();
        command.packageSize = PackageSize.valueOf(dto.packageSize);
        command.packgeWeight = BigDecimal.valueOf(dto.packgeWeight);
        command.receiverEmail = dto.receiverEmail;
        command.senderEmail = dto.senderEmail;

        packagesService.sendPackage(command);

        return ResponseEntity.ok().build();
    }
    
}
