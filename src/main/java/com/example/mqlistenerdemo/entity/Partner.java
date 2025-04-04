package com.example.mqlistenerdemo.entity;

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

    private String alias;

    private String type;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    private String application;

    @Enumerated(EnumType.STRING)
    private FlowType processedFlowType;

    private String description;

    public enum Direction {
        INBOUND,
        OUTBOUND
    }

    public enum FlowType {
        MESSAGE,
        ALERTING,
        NOTIFICATION
    }
}
