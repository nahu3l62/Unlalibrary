package com.unla.nahuel.services.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.unla.nahuel.entities.Carreras;
import com.unla.nahuel.entities.Perfiles;
import com.unla.nahuel.repositories.ICarreraRepository;
import com.unla.nahuel.services.ICarreraService;

@Service("carreraService")
public class CarreraService implements ICarreraService{
	
	@Autowired
	@Qualifier("carreraRepository")
	private ICarreraRepository carreraRepository;
	
	@Override
	public List<Carreras> getAll() {
		return carreraRepository.findAll();
	}
	
	@Override
	public void save(Carreras carrera) {
		carreraRepository.save(carrera);
	}

}
