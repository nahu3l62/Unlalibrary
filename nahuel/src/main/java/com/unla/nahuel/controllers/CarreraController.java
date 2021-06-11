package com.unla.nahuel.controllers;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.unla.nahuel.entities.Carreras;
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
	
	private static String UPLOADED_FOLDER = "C://archivos//";
	
	@GetMapping("/materia/proyecto")
	public String proyecto(Model model) throws IOException{
		
		
	
            List<File> files = Files.list(Paths.get(UPLOADED_FOLDER))
                        .map(Path::toFile)
                        .collect(Collectors.toList());
             
           
           Iterator<File> iterador = files.iterator();
           
           File file = new File(UPLOADED_FOLDER + "/" + "temperaturas.txt");
           while(iterador.hasNext()) {
        	   File prueba = iterador.next();
        	   System.out.println(prueba.getName());
           }
           
           //System.out.println(files.get(0).getName());
           model.addAttribute("titulo", "Licenciatura en sistemas");
           model.addAttribute("files", files);
           model.addAttribute("filess", file);
		
		//System.out.println(files);
		
		
		
		return "materias/proyecto";
	}
	
	
	/*@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public String submit(@RequestParam("file") File file, Model model) {
		
	    model.addAttribute("file", file.getName());
	    model.addAttribute("file2", file.getPath());
	    
	    System.out.println(file.getName());
	    return "materias/subida";
	}*/
	
	
	
	
	@PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Model model) throws IOException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Por favor seleccione un archivo a subir.");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "Se completo la subida del archivo '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
       
        
        
        return "redirect:/carreras/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "/materias/status";
    }
	


	
	
	

}
