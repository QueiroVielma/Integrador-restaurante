package com.restaurante.api.repository;

import com.restaurante.api.entity.Comanda;
import com.restaurante.api.entity.StatusComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    @Query("""
            SELECT DISTINCT c FROM Comanda c
            LEFT JOIN FETCH c.itens i
            LEFT JOIN FETCH i.produto
            WHERE c.statusComanda = :status
            """)
    List<Comanda> findByStatusComanda(@Param("status") StatusComanda status);

    @Query("""
            SELECT DISTINCT c FROM Comanda c
            LEFT JOIN FETCH c.itens i
            LEFT JOIN FETCH i.produto
            WHERE c.id = :id
            """)
    Optional<Comanda> findDetalheById(@Param("id") Long id);
}
