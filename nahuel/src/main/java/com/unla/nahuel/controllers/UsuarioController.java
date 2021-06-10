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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unla.nahuel.entities.Perfiles;
import com.unla.nahuel.entities.Usuario;
import com.unla.nahuel.helpers.ViewRouteHelper;
import com.unla.nahuel.services.IPerfilesService;
import com.unla.nahuel.services.IUsuarioService;



@Controller
@RequestMapping("/usuarios")

public class UsuarioController {

	@Autowired
	@Qualifier("usuarioService")
	private IUsuarioService usuarioService;

	@Autowired
	@Qualifier("perfilesService")
	private IPerfilesService perfilesService;
	

	@GetMapping("/")
	public String crear(Model model) {

		Usuario usuario = new Usuario();
		List<Perfiles> listaPerfiles = perfilesService.getAll();
		
		List<Perfiles> perfiles = new ArrayList<Perfiles>();
		for (Perfiles p : listaPerfiles) {
			if (p.isDeshabilitado() == true) {
				perfiles.add(p);
			}
		}
		model.addAttribute("titulo", "Formulario: Registrarse");
		model.addAttribute("usuario", usuario);
		model.addAttribute("perfiles", perfiles);

		return ViewRouteHelper.USUARIO_INDEX;
	}

	@PostMapping("/")
	public String guardar(@Valid @ModelAttribute Usuario usuario,BindingResult result, Model model,RedirectAttributes attribute) {
		List<Perfiles> listaPerfiles = perfilesService.getAll();
		List<Perfiles> perfiles = new ArrayList<Perfiles>();
		for (Perfiles p : listaPerfiles) {
			if (p.isDeshabilitado() == true) {
				perfiles.add(p);}
			}
		String dni = String.valueOf(usuario.getDocumento());
		if(usuario.getDocumento()==0) {
		FieldError error = new FieldError("usuario", "documento", "El DNI de la persona no puede ser 0");
		result.addError(error);
		}
		if(dni.length()<8 || dni.length()>8) {
			FieldError error = new FieldError("usuario", "documento", "Los caracteres del DNI deben ser 7 como minimo y 8 como maximo");
			result.addError(error);
		}
		
		if(usuarioService.findByDni(usuario.getDocumento())!=null && usuarioService.findByDni(usuario.getDocumento()).getId()!=usuario.getId()) {
			FieldError error = new FieldError("usuario", "documento", "Ya existe una persona con ese DNI");
			result.addError(error);
		}
		if(usuarioService.findByEmail(usuario.getCorreoElectronico())!=null && usuarioService.findByEmail(usuario.getCorreoElectronico()).getId()!=usuario.getId()) {
			FieldError error = new FieldError("usuario", "correoElectronico", "Ya existe una persona con ese correo electronico");
			result.addError(error);
		}
		if(usuarioService.findByUsername(usuario.getNombreDeUsuario())!=null && usuarioService.findByUsername(usuario.getNombreDeUsuario()).getId()!=usuario.getId()) {
			FieldError error = new FieldError("usuario", "nombreDeUsuario", "Ya existe una persona con ese nombre de usuario");
			result.addError(error);
		}
		if(result.hasErrors()) {
		model.addAttribute("titulo", "Formulario: Nuevo Usuario");
		model.addAttribute("usuario", usuario);
		model.addAttribute("perfiles", listaPerfiles);
		System.out.println("Se encontraron Errores en el formulario!");
		return ViewRouteHelper.USUARIO_INDEX;
		}
		usuario.setDeshabilitado(false);
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		String contrasenaEncriptada = pe.encode(usuario.getContrasena());
		usuario.setContrasena(contrasenaEncriptada);
		usuarioService.save(usuario);
		attribute.addFlashAttribute("success","Usuario registrado con exito");
		System.out.println("Usuario guardado con exito!");
		return "redirect:/usuarios/";

	}

	@GetMapping("/lista")
	public String listarClientes(Model model) {
		List<Usuario> listado = usuarioService.getAll();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		for (Usuario u : listado) {
			if (u.isDeshabilitado() == true) {
				usuarios.add(u);
			}
		}
		model.addAttribute("titulo", "Lista de usuarios activos");
		model.addAttribute("lista", usuarios);
		return ViewRouteHelper.USUARIO_LISTA;
	}
	
	@GetMapping("/lista/inactivo")
	public String listarClientesInactivos(Model model) {
		List<Usuario> listado = usuarioService.getAll();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		for (Usuario u : listado) {
			if (u.isDeshabilitado() == false) {
				usuarios.add(u);
			}
		}
		model.addAttribute("titulo", "Lista de usuarios inactivos");
		model.addAttribute("lista", usuarios);
		return "usuario/lista2";
	}

	@GetMapping("lista/edit/{id}")
	public String editar(@PathVariable("id") long id, Model model) {
		Usuario usuario1 = usuarioService.buscar(id);
		List<Perfiles> listaPerfiles = perfilesService.getAll();
		model.addAttribute("titulo", "Editar usuario");
		model.addAttribute("usuario", usuario1);
		model.addAttribute("perfiles", listaPerfiles);
		return ViewRouteHelper.USUARIO_FORM;
	}
	
	@GetMapping("lista/delete/{id}")
	public String eliminar(@PathVariable("id") long id) {
		Usuario u = usuarioService.buscar(id);
		u.setDeshabilitado(false);
		usuarioService.save(u);
		System.out.println("Usuario deshabilitado con exito");
		return ViewRouteHelper.ROUTE_REDIRECT;
	}
	
	@GetMapping("lista/activar/{id}")
	public String activar(@PathVariable("id") long id) {
		Usuario u = usuarioService.buscar(id);
		u.setDeshabilitado(true);
		usuarioService.save(u);
		System.out.println("Usuario habilitado con exito");
		return "redirect:/usuarios/lista/inactivo";
	}

}