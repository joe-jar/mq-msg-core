package com.example.msgibm.repository;

import com.example.msgibm.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {}
