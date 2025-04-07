package com.example.msgibm.dto;

import com.example.msgibm.enums.Direction;
import com.example.msgibm.enums.FlowType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDto {


    private Long id;

    @NotBlank(message = "Alias is required")
    private String alias;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Direction is required")
    private Direction direction;

    private String application;

    @NotNull(message = "Processed Flow Type is required")
    private FlowType processedFlowType;

    @NotBlank(message = "Description is required")
    private String description;
}
