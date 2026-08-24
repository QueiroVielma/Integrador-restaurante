package com.restaurante.api.service;

import com.restaurante.api.entity.Produto;
import com.restaurante.api.exception.NegocioException;
import com.restaurante.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Produto não encontrado: " + id));
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto dados) {
        Produto produto = buscarPorId(id);
        produto.setNome(dados.getNome());
        produto.setPreco(dados.getPreco());
        produto.setAtivo(dados.getAtivo());
        return produtoRepository.save(produto);
    }

    public void excluir(Long id) {
        produtoRepository.delete(buscarPorId(id));
    }
}
