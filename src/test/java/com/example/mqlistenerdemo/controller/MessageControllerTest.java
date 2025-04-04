package com.example.mqlistenerdemo.controller;

import com.example.mqlistenerdemo.dto.MessageDto;
import com.example.mqlistenerdemo.exception.MessageNotFoundException;
import com.example.mqlistenerdemo.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    void getMessages_shouldReturnList() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setId(1L);
        dto.setContent("Hello");
        dto.setSourceQueue("DEV.QUEUE.1");
        dto.setReceivedAt("2025-04-01T12:00:00");

        when(messageService.getAllMessages()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello"));
    }

    @Test
    void getMessageById_whenExists_shouldReturnMessage() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setId(1L);
        dto.setContent("Hello");
        dto.setSourceQueue("DEV.QUEUE.1");
        dto.setReceivedAt("2025-04-01T12:00:00");

        when(messageService.getMessageById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    void getMessageById_whenNotFound_shouldReturn404() throws Exception {
        when(messageService.getMessageById(999L)).thenThrow(new MessageNotFoundException(999L));

        mockMvc.perform(get("/api/messages/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Message not found with ID: 999"));
    }
}
