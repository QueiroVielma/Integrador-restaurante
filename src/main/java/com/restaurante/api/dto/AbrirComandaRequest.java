package com.restaurante.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbrirComandaRequest {

    @NotNull
    private Long mesaId;

    @NotNull
    private Long usuarioId;
}
