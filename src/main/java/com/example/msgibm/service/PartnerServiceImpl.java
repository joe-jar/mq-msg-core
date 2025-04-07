package com.example.msgibm.service;

import com.example.msgibm.dto.PartnerDto;
import com.example.msgibm.exception.PartnerNotFoundException;
import com.example.msgibm.model.Partner;
import com.example.msgibm.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PartnerDto addPartner(PartnerDto dto) {
        Partner entity = modelMapper.map(dto, Partner.class);
        Partner saved = partnerRepo.save(entity);
        return modelMapper.map(saved, PartnerDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartnerDto> getAllPartners() {
        return partnerRepo.findAll().stream()
                .map(partner -> modelMapper.map(partner, PartnerDto.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletePartner(Long id) {
        Partner partner = partnerRepo.findById(id)
                .orElseThrow(() -> new PartnerNotFoundException(id));
        partnerRepo.delete(partner);
    }
}
