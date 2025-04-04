package com.example.mqlistenerdemo.service;

import com.example.mqlistenerdemo.dto.PartnerDto;
import com.example.mqlistenerdemo.entity.Partner;
import com.example.mqlistenerdemo.exception.PartnerNotFoundException;
import com.example.mqlistenerdemo.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepo;
    private final ModelMapper modelMapper;

    @Transactional
    public PartnerDto addPartner(PartnerDto dto) {
        Partner entity = modelMapper.map(dto, Partner.class);
        Partner saved = partnerRepo.save(entity);
        return modelMapper.map(saved, PartnerDto.class);
    }

    @Transactional(readOnly = true)
    public List<PartnerDto> getAllPartners() {
        return partnerRepo.findAll().stream()
                .map(partner -> modelMapper.map(partner, PartnerDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePartner(Long id) {
        Partner partner = partnerRepo.findById(id)
                .orElseThrow(() -> new PartnerNotFoundException(id));
        partnerRepo.delete(partner);
    }
}
