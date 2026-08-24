package com.restaurante.api.controller;

import com.restaurante.api.dto.AbrirComandaRequest;
import com.restaurante.api.dto.AdicionarItemRequest;
import com.restaurante.api.entity.Comanda;
import com.restaurante.api.service.ComandaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
@RequiredArgsConstructor
public class ComandaController {

    private final ComandaService comandaService;

    @PostMapping
    public ResponseEntity<Comanda> abrir(@Valid @RequestBody AbrirComandaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comandaService.abrir(request));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<Comanda> adicionarItem(
            @PathVariable Long id,
            @Valid @RequestBody AdicionarItemRequest request
    ) {
        return ResponseEntity.ok(comandaService.adicionarItem(id, request));
    }

    @GetMapping({ "", "/abertas" })
    public ResponseEntity<List<Comanda>> listarAbertas() {
        return ResponseEntity.ok(comandaService.listarAbertas());
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<Comanda> fechar(@PathVariable Long id) {
        return ResponseEntity.ok(comandaService.fechar(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comanda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comandaService.buscarPorId(id));
    }
}
