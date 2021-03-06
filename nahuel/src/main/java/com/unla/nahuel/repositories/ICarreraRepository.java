package com.unla.nahuel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.nahuel.entities.Carreras;

@Repository("carreraRepository")
public interface ICarreraRepository extends JpaRepository<Carreras, Long> {

}
