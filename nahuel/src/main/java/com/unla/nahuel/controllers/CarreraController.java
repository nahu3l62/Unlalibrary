package com.unla.nahuel.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unla.nahuel.entities.Carreras;
import com.unla.nahuel.entities.Perfiles;
import com.unla.nahuel.entities.Usuario;
import com.unla.nahuel.helpers.ViewRouteHelper;
import com.unla.nahuel.services.ICarreraService;

@Controller
@RequestMapping("/carreras")
public class CarreraController {
	
	@Autowired
	@Qualifier("carreraService")
	private ICarreraService carreraService;
	
	
	@GetMapping("/")
	public String crear(Model model) {

		Carreras carrera = new Carreras();
		
		model.addAttribute("titulo", "Formulario: Nueva Carrera");
		model.addAttribute("carrera", carrera);


		return "carreras/crear";
	}

	@PostMapping("/")
	public String guardar(@Valid @ModelAttribute Carreras carrera,BindingResult result, Model model) {
		
		
		if(result.hasErrors()) {
		model.addAttribute("titulo", "Formulario: Nueva Carrera");
		model.addAttribute("carrera", carrera);
		System.out.println("Se encontraron Errores en el formulario!");
		return ViewRouteHelper.USUARIO_INDEX;
		}
		carreraService.save(carrera);
		
		System.out.println("Carrera creada con exito!");
		
		return "redirect:/carreras/";

	}
	
	
	@GetMapping("/lista")
	public String listarCarreras(Model model) {
		List<Carreras> listado = carreraService.getAll();
	
		model.addAttribute("titulo", "Lista de carreras");
		model.addAttribute("lista", listado);

		return "carreras/lista";
	}
	
	@GetMapping("/materia")
	public String materias(Model model) {
	
		model.addAttribute("titulo", "Materias de licenciatura en sistemas");

		return "materias/index";
	}
	
	@GetMapping("/materia/proyecto")
	public String proyecto(Model model) {
	
		model.addAttribute("titulo", "Licenciatura en sistemas");

		return "materias/proyecto";
	}
	
	
	
	
	
	

}
