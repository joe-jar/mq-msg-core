package com.example.mqlistenerdemo.controller;

import com.example.mqlistenerdemo.dto.PartnerDto;
import com.example.mqlistenerdemo.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<PartnerDto>> getPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners());
    }

    @PostMapping
    public ResponseEntity<PartnerDto> createPartner(@Valid @RequestBody PartnerDto dto) {
        PartnerDto created = partnerService.addPartner(dto);
        return ResponseEntity
                .created(URI.create("/api/partners")) // You can append `+ created.getId()` if ID is available
                .body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.ok().build();
    }
}
