package com.restaurante.api.service;

import com.restaurante.api.dto.AbrirComandaRequest;
import com.restaurante.api.dto.AdicionarItemRequest;
import com.restaurante.api.entity.Comanda;
import com.restaurante.api.entity.ItemPedido;
import com.restaurante.api.entity.Mesa;
import com.restaurante.api.entity.Produto;
import com.restaurante.api.entity.StatusComanda;
import com.restaurante.api.entity.StatusMesa;
import com.restaurante.api.entity.Usuario;
import com.restaurante.api.exception.NegocioException;
import com.restaurante.api.repository.ComandaRepository;
import com.restaurante.api.repository.MesaRepository;
import com.restaurante.api.repository.ProdutoRepository;
import com.restaurante.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final MesaRepository mesaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public Comanda abrir(AbrirComandaRequest request) {
        Mesa mesa = mesaRepository.findById(request.getMesaId())
                .orElseThrow(() -> new NegocioException("Mesa não encontrada: " + request.getMesaId()));

        if (mesa.getStatusMesa() == StatusMesa.OCUPADA) {
            throw new NegocioException("Mesa já está ocupada");
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new NegocioException("Usuário não encontrado: " + request.getUsuarioId()));

        mesa.setStatusMesa(StatusMesa.OCUPADA);
        mesaRepository.save(mesa);

        Comanda comanda = new Comanda();
        comanda.setMesa(mesa);
        comanda.setUsuario(usuario);
        comanda.setStatusComanda(StatusComanda.ABERTA);
        comanda.setDataAbertura(LocalDateTime.now());
        comanda.setTotal(0.0);

        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda adicionarItem(Long comandaId, AdicionarItemRequest request) {
        Comanda comanda = buscarPorId(comandaId);

        if (comanda.getStatusComanda() != StatusComanda.ABERTA) {
            throw new NegocioException("Só é possível adicionar itens em comanda ABERTA");
        }

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new NegocioException("Produto não encontrado: " + request.getProdutoId()));

        if (Boolean.FALSE.equals(produto.getAtivo())) {
            throw new NegocioException("Produto inativo: " + produto.getNome());
        }

        ItemPedido item = new ItemPedido();
        item.setComanda(comanda);
        item.setProduto(produto);
        item.setQuantidade(request.getQuantidade());
        item.setPrecoUnitario(produto.getPreco());

        comanda.getItens().add(item);
        atualizarTotal(comanda);

        return comandaRepository.save(comanda);
    }

    @Transactional(readOnly = true)
    public List<Comanda> listarAbertas() {
        return comandaRepository.findByStatusComanda(StatusComanda.ABERTA);
    }

    @Transactional
    public Comanda fechar(Long comandaId) {
        Comanda comanda = buscarPorId(comandaId);

        if (comanda.getStatusComanda() != StatusComanda.ABERTA) {
            throw new NegocioException("Comanda não está ABERTA");
        }

        comanda.setStatusComanda(StatusComanda.PAGA);

        Mesa mesa = comanda.getMesa();
        mesa.setStatusMesa(StatusMesa.LIVRE);
        mesaRepository.save(mesa);

        return comandaRepository.save(comanda);
    }

    public Comanda buscarPorId(Long id) {
        return comandaRepository.findDetalheById(id)
                .orElseThrow(() -> new NegocioException("Comanda não encontrada: " + id));
    }

    private void atualizarTotal(Comanda comanda) {
        double total = comanda.getItens().stream()
                .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                .sum();
        comanda.setTotal(total);
    }
}
