package com.example.mqlistenerdemo.service;

import com.example.mqlistenerdemo.dto.MessageDto;
import com.example.mqlistenerdemo.entity.Message;
import com.example.mqlistenerdemo.exception.MessageNotFoundException;
import com.example.mqlistenerdemo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepo;
    private final ModelMapper modelMapper;

    @Transactional
    public void saveMessage(String content, String sourceQueue) {
        Message message = Message.builder()
                .content(content)
                .sourceQueue(sourceQueue)
                .receivedAt(LocalDateTime.now())
                .build();
        messageRepo.save(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> getAllMessages() {
        return messageRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MessageDto getMessageById(Long id) {
        return messageRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    private MessageDto toDto(Message entity) {
        MessageDto dto = modelMapper.map(entity, MessageDto.class);
        dto.setReceivedAt(entity.getReceivedAt().toString());
        return dto;
    }
}
