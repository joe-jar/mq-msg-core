package com.example.msgibm.service;

import com.example.msgibm.dto.MessageDto;
import com.example.msgibm.exception.MessageNotFoundException;
import com.example.msgibm.model.Message;
import com.example.msgibm.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void saveMessage(String content, String sourceQueue) {
        Message message = Message.builder()
                .content(content)
                .sourceQueue(sourceQueue)
                .receivedAt(LocalDateTime.now())
                .build();

        messageRepo.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getAllMessages(Pageable pageable) {
        return messageRepo.findAll(pageable)
                .map(entity -> modelMapper.map(entity, MessageDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto getMessageById(Long id) {
        Message message = messageRepo.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        return modelMapper.map(message, MessageDto.class);
    }
}
