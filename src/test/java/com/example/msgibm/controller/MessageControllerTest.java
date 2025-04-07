package com.example.msgibm.controller;

import com.example.msgibm.dto.MessageDto;
import com.example.msgibm.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    void getMessages_shouldReturnPage() throws Exception {
        MessageDto dto = MessageDto.builder().id(1L).content("Msg").build();
        when(messageService.getAllMessages(any()))
                .thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/mq/api/messages?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("Msg"));
    }

    @Test
    void getMessageById_shouldReturnOk() throws Exception {
        MessageDto dto = MessageDto.builder().id(1L).content("test").build();
        when(messageService.getMessageById(1L)).thenReturn(dto);

        mockMvc.perform(get("/mq/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("test"));
    }
}
