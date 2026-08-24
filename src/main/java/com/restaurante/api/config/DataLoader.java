package com.restaurante.api.config;

import com.restaurante.api.entity.Mesa;
import com.restaurante.api.entity.Produto;
import com.restaurante.api.entity.Role;
import com.restaurante.api.entity.StatusMesa;
import com.restaurante.api.entity.Usuario;
import com.restaurante.api.repository.MesaRepository;
import com.restaurante.api.repository.ProdutoRepository;
import com.restaurante.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final MesaRepository mesaRepository;
    private final ProdutoRepository produtoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            usuarioRepository.save(admin);
        }

        if (mesaRepository.count() == 0) {
            for (int numero = 1; numero <= 5; numero++) {
                Mesa mesa = new Mesa();
                mesa.setNumero(numero);
                mesa.setStatusMesa(StatusMesa.LIVRE);
                mesaRepository.save(mesa);
            }
        }

        if (produtoRepository.count() == 0) {
            Produto suco = new Produto();
            suco.setNome("Suco de laranja");
            suco.setPreco(8.50);
            suco.setAtivo(true);

            Produto hamburguer = new Produto();
            hamburguer.setNome("Hambúrguer");
            hamburguer.setPreco(28.00);
            hamburguer.setAtivo(true);

            produtoRepository.save(suco);
            produtoRepository.save(hamburguer);
        }
    }
}
