package com.example.msgibm.service;

import com.example.msgibm.dto.MessageDto;
import com.example.msgibm.model.Message;
import com.example.msgibm.exception.MessageNotFoundException;
import com.example.msgibm.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMessage_shouldPersistMessage() {
        // given
        String content = "Test Message";
        String sourceQueue = "QUEUE.1";

        // when
        messageService.saveMessage(content, sourceQueue);

        // then
        verify(messageRepository).save(argThat(msg ->
                msg.getContent().equals(content) &&
                        msg.getSourceQueue().equals(sourceQueue) &&
                        msg.getReceivedAt() != null
        ));
    }

    @Test
    void getAllMessages_ok() {
        // given
        Message message = Message.builder()
                .id(1L)
                .content("Hello")
                .sourceQueue("Q1")
                .receivedAt(LocalDateTime.now())
                .build();

        MessageDto dto = MessageDto.builder()
                .id(1L)
                .content("Hello")
                .sourceQueue("Q1")
                .receivedAt(message.getReceivedAt())
                .build();

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Message> page = new PageImpl<>(List.of(message));

        when(messageRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(dto); // 👈 avoid NPE

        // when
        Page<MessageDto> result = messageService.getAllMessages(pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("Hello", result.getContent().get(0).getContent());
    }

    @Test
    void getMessageById_ok() {
        // given
        Message message = Message.builder()
                .id(5L)
                .content("Info")
                .sourceQueue("DEV.QUEUE.1")
                .receivedAt(LocalDateTime.now())
                .build();

        MessageDto dto = MessageDto.builder()
                .id(5L)
                .content("Info")
                .sourceQueue("DEV.QUEUE.1")
                .receivedAt(message.getReceivedAt())
                .build();

        when(messageRepository.findById(5L)).thenReturn(Optional.of(message));
        when(modelMapper.map(message, MessageDto.class)).thenReturn(dto); // 👈 avoid NPE

        // when
        MessageDto result = messageService.getMessageById(5L);

        // then
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Info", result.getContent());
    }

    @Test
    void getMessageById_whenNotFound_shouldThrowException() {
        // given
        when(messageRepository.findById(999L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(MessageNotFoundException.class, () -> messageService.getMessageById(999L));
    }
}
