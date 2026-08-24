package com.restaurante.api.controller;

import com.restaurante.api.entity.Mesa;
import com.restaurante.api.service.MesaService;
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
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    @GetMapping
    public ResponseEntity<List<Mesa>> listar() {
        return ResponseEntity.ok(mesaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mesaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Mesa> criar(@RequestBody Mesa mesa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaService.salvar(mesa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> atualizar(@PathVariable Long id, @RequestBody Mesa mesa) {
        return ResponseEntity.ok(mesaService.atualizar(id, mesa));
    }
}
