package com.example.mqlistenerdemo.repository;

import com.example.mqlistenerdemo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {}
