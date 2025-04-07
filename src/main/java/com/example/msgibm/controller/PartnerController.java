package com.example.msgibm.controller;

import com.example.msgibm.dto.PartnerDto;
import com.example.msgibm.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mq/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<PartnerDto>> getPartners() {
        List<PartnerDto> list = partnerService.getAllPartners();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<PartnerDto> createPartner(@RequestBody @Valid PartnerDto dto) {
        PartnerDto created = partnerService.addPartner(dto);
        URI location = URI.create("/api/partners/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
    }
}
