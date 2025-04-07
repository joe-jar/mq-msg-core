package com.example.msgibm.service;

import com.example.msgibm.dto.PartnerDto;

import java.util.List;

public interface PartnerService {
    PartnerDto addPartner(PartnerDto dto);
    List<PartnerDto> getAllPartners();
    void deletePartner(Long id);
}
