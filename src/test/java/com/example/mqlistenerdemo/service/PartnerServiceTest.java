package com.example.mqlistenerdemo.service;

import com.example.mqlistenerdemo.dto.PartnerDto;
import com.example.mqlistenerdemo.entity.Partner;
import com.example.mqlistenerdemo.exception.PartnerNotFoundException;
import com.example.mqlistenerdemo.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartnerServiceTest {

    @Mock
    private PartnerRepository partnerRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PartnerService partnerService;

    private PartnerDto sampleDto;
    private Partner sampleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleDto = PartnerDto.builder()
                .alias("MyPartner")
                .type("System")
                .direction(Partner.Direction.OUTBOUND)
                .application("App1")
                .processedFlowType(Partner.FlowType.MESSAGE)
                .description("Some description")
                .build();

        sampleEntity = Partner.builder()
                .id(1L)
                .alias(sampleDto.getAlias())
                .type(sampleDto.getType())
                .direction(sampleDto.getDirection())
                .application(sampleDto.getApplication())
                .processedFlowType(sampleDto.getProcessedFlowType())
                .description(sampleDto.getDescription())
                .build();
    }

    @Test
    void addPartner_shouldMapAndSave_andReturnDto() {
        when(modelMapper.map(sampleDto, Partner.class)).thenReturn(sampleEntity);
        when(partnerRepo.save(sampleEntity)).thenReturn(sampleEntity);
        when(modelMapper.map(sampleEntity, PartnerDto.class)).thenReturn(sampleDto);

        PartnerDto result = partnerService.addPartner(sampleDto);

        assertThat(result).isEqualTo(sampleDto);
        verify(partnerRepo).save(sampleEntity);
    }

    @Test
    void getAllPartners_shouldReturnMappedList() {
        when(partnerRepo.findAll()).thenReturn(List.of(sampleEntity));
        when(modelMapper.map(sampleEntity, PartnerDto.class)).thenReturn(sampleDto);

        List<PartnerDto> result = partnerService.getAllPartners();

        assertThat(result).hasSize(1).containsExactly(sampleDto);
    }

    @Test
    void deletePartner_whenFound_shouldDelete() {
        when(partnerRepo.findById(1L)).thenReturn(Optional.of(sampleEntity));

        partnerService.deletePartner(1L);

        verify(partnerRepo).delete(sampleEntity);
    }

    @Test
    void deletePartner_whenNotFound_shouldThrow() {
        when(partnerRepo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partnerService.deletePartner(999L))
                .isInstanceOf(PartnerNotFoundException.class)
                .hasMessageContaining("999");
    }
}
