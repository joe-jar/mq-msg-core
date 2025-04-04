package com.example.mqlistenerdemo.repository;

import com.example.mqlistenerdemo.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {}
