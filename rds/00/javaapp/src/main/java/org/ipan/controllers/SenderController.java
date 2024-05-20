package org.ipan.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.ipan.dto.CreateNewSenderCommandDto;
import org.ipan.services.CreateSenderCommand;
import org.ipan.services.SenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;


@RestController
public class SenderController {
    private final SenderService senderService;

    public SenderController(SenderService senderService) {
        this.senderService = senderService;
    }

    @PutMapping("sender")
    @Transactional
    public ResponseEntity<?> putMethodName(@RequestBody CreateNewSenderCommandDto dto) {
        CreateSenderCommand command = new CreateSenderCommand();
        command.firstName = dto.firstName;
        command.lastName = dto.lastName;
        command.senderEmail = dto.senderEmail;
        this.senderService.createNewSender(command);
        
        return ResponseEntity.ok().build();
    }
}
