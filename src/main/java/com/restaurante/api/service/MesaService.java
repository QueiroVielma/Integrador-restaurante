package com.restaurante.api.service;

import com.restaurante.api.entity.Mesa;
import com.restaurante.api.exception.NegocioException;
import com.restaurante.api.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;

    public List<Mesa> listar() {
        return mesaRepository.findAll();
    }

    public Mesa buscarPorId(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Mesa não encontrada: " + id));
    }

    public Mesa salvar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public Mesa atualizar(Long id, Mesa dados) {
        Mesa mesa = buscarPorId(id);
        mesa.setNumero(dados.getNumero());
        mesa.setStatusMesa(dados.getStatusMesa());
        return mesaRepository.save(mesa);
    }
}
