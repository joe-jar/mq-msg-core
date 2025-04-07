package com.example.msgibm.controller;

import com.example.msgibm.dto.PartnerDto;
import com.example.msgibm.enums.Direction;
import com.example.msgibm.enums.FlowType;
import com.example.msgibm.service.PartnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PartnerController.class)
class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPartners_shouldReturnList() throws Exception {
        List<PartnerDto> partners = Arrays.asList(
                new PartnerDto(1L, "Alias1", "Type1", Direction.INBOUND, "App1", FlowType.MESSAGE, "Description1"),
                new PartnerDto(2L, "Alias2", "Type2", Direction.OUTBOUND, "App2", FlowType.ALERTING, "Description2")
        );

        when(partnerService.getAllPartners()).thenReturn(partners);

        mockMvc.perform(get("/mq/api/partners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(partners.size()))
                .andExpect(jsonPath("$[0].alias").value("Alias1"))
                .andExpect(jsonPath("$[1].alias").value("Alias2"));
    }

    @Test
    void createPartner_ok() throws Exception {
        PartnerDto newPartner = new PartnerDto(null, "New", "X", Direction.OUTBOUND, null, FlowType.ALERTING, "desc");
        PartnerDto savedPartner = new PartnerDto(1L, "New", "X", Direction.OUTBOUND, null, FlowType.ALERTING, "desc");

        when(partnerService.addPartner(any(PartnerDto.class))).thenReturn(savedPartner);

        mockMvc.perform(post("/mq/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPartner)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.alias").value("New"));
    }

    @Test
    void deletePartner_ok() throws Exception {
        doNothing().when(partnerService).deletePartner(1L);

        mockMvc.perform(delete("/mq/api/partners/1"))
                .andExpect(status().isNoContent());
    }

}
