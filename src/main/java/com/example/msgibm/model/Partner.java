package com.example.msgibm.model;

import com.example.msgibm.enums.Direction;
import com.example.msgibm.enums.FlowType;
import lombok.*;

import javax.persistence.*;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Partner {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String alias;

        @Column(nullable = false)
        private String type;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Direction direction;

        private String application;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private FlowType processedFlowType;

        private String description;
    }
