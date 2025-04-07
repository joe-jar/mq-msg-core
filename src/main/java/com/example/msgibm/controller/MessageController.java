package com.example.msgibm.controller;

import com.example.msgibm.dto.MessageDto;
import com.example.msgibm.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mq/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<Page<MessageDto>> getMessages(Pageable pageable) {
        Page<MessageDto> messages = messageService.getAllMessages(pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }
}

