package com.restaurante.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdicionarItemRequest {

    @NotNull
    private Long produtoId;

    @NotNull
    @Min(1)
    private Integer quantidade;
}
