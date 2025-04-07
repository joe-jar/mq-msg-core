package com.example.msgibm.service;

import com.example.msgibm.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    void saveMessage(String content, String sourceQueue);
    Page<MessageDto> getAllMessages(Pageable pageable);
    MessageDto getMessageById(Long id);
}
