package com.unla.nahuel.services;

import java.util.List;

import com.unla.nahuel.entities.Carreras;
import com.unla.nahuel.entities.Perfiles;



public interface ICarreraService {
	public List<Carreras> getAll();
	public void save(Carreras carrera);
	
	
}
