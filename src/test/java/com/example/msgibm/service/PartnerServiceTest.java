package com.example.msgibm.service;

import com.example.msgibm.dto.PartnerDto;
import com.example.msgibm.model.Partner;
import com.example.msgibm.enums.Direction;
import com.example.msgibm.enums.FlowType;
import com.example.msgibm.exception.PartnerNotFoundException;
import com.example.msgibm.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartnerServiceTest {

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PartnerService partnerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPartner_ok() {
        // given
        PartnerDto input = PartnerDto.builder()
                .alias("Test")
                .type("TypeA")
                .description("Some description")
                .direction(Direction.INBOUND)
                .processedFlowType(FlowType.MESSAGE)
                .build();

        Partner entity = Partner.builder()
                .alias("Test")
                .type("TypeA")
                .description("Some description")
                .direction(Direction.INBOUND)
                .processedFlowType(FlowType.MESSAGE)
                .build();

        Partner savedEntity = Partner.builder()
                .id(1L)
                .alias("Test")
                .type("TypeA")
                .description("Some description")
                .direction(Direction.INBOUND)
                .processedFlowType(FlowType.MESSAGE)
                .build();

        PartnerDto expected = PartnerDto.builder()
                .id(1L)
                .alias("Test")
                .type("TypeA")
                .description("Some description")
                .direction(Direction.INBOUND)
                .processedFlowType(FlowType.MESSAGE)
                .build();

        // when
        when(modelMapper.map(input, Partner.class)).thenReturn(entity);
        when(partnerRepository.save(entity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, PartnerDto.class)).thenReturn(expected);

        PartnerDto result = partnerService.addPartner(input);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getAlias());
        verify(partnerRepository).save(entity);
    }

    @Test
    void getAllPartners_ok() {
        // given
        Partner partner = Partner.builder()
                .id(1L)
                .alias("Alpha")
                .type("X")
                .description("desc")
                .direction(Direction.OUTBOUND)
                .processedFlowType(FlowType.ALERTING)
                .build();

        PartnerDto dto = PartnerDto.builder()
                .id(1L)
                .alias("Alpha")
                .type("X")
                .description("desc")
                .direction(Direction.OUTBOUND)
                .processedFlowType(FlowType.ALERTING)
                .build();

        when(partnerRepository.findAll()).thenReturn(List.of(partner));
        when(modelMapper.map(partner, PartnerDto.class)).thenReturn(dto);

        // when
        List<PartnerDto> result = partnerService.getAllPartners();

        // then
        assertEquals(1, result.size());
        assertEquals("Alpha", result.get(0).getAlias());
    }

    @Test
    void deletePartner_ok() {
        // given
        Partner partner = Partner.builder().id(42L).alias("Z").build();
        when(partnerRepository.findById(42L)).thenReturn(Optional.of(partner));

        // when
        partnerService.deletePartner(42L);

        // then
        verify(partnerRepository).delete(partner);
    }

    @Test
    void deletePartner_whenNotFound_shouldThrowException() {
        // given
        when(partnerRepository.findById(404L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(PartnerNotFoundException.class, () -> partnerService.deletePartner(404L));
    }
}
