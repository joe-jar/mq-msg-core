package com.example.mqlistenerdemo.service;

import com.example.mqlistenerdemo.controller.PartnerController;
import com.example.mqlistenerdemo.dto.PartnerDto;
import com.example.mqlistenerdemo.entity.Partner;
import com.example.mqlistenerdemo.exception.PartnerNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartnerController.class)
class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPartners_shouldReturnList() throws Exception {
        PartnerDto dto = PartnerDto.builder()
                .alias("Test")
                .type("Internal")
                .direction(Partner.Direction.INBOUND)
                .application("App1")
                .processedFlowType(Partner.FlowType.ALERTING)
                .description("Test partner")
                .build();

        when(partnerService.getAllPartners()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/partners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alias").value("Test"));
    }

    @Test
    void createPartner_shouldReturnCreatedPartner() throws Exception {
        PartnerDto dto = PartnerDto.builder()
                .alias("New Partner")
                .type("API")
                .direction(Partner.Direction.OUTBOUND)
                .application("NotifyApp")
                .processedFlowType(Partner.FlowType.NOTIFICATION)
                .description("Alert endpoint")
                .build();

        when(partnerService.addPartner(any())).thenReturn(dto);

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias").value("New Partner"));
    }

    @Test
    void deletePartner_whenFound_shouldReturnNoContent() throws Exception {
        doNothing().when(partnerService).deletePartner(1L);

        mockMvc.perform(delete("/api/partners/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deletePartner_whenNotFound_shouldReturn404() throws Exception {
        doThrow(new PartnerNotFoundException(123L)).when(partnerService).deletePartner(123L);

        mockMvc.perform(delete("/api/partners/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Partner not found with ID: 123"));
    }
}
