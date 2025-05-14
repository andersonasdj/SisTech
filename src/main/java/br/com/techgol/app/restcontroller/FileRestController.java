package br.com.techgol.app.restcontroller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgol.app.dto.DtoFile;

@RestController
@RequestMapping("/api/file")
public class FileRestController {
	
	@Value("${upload.dir.solicitacaos}")
	private String UPLOAD_DIR;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@ModelAttribute DtoFile uploadRequest){
		
		MultipartFile file = uploadRequest.file();
		String id = uploadRequest.id();
		System.out.println("ID: " + id);
		
		if(file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("o arquivo está vazio");
		}
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			File uploadDir = new File(UPLOAD_DIR+"/"+id+"/");
			
			if(!uploadDir.exists()) {
				boolean created = uploadDir.mkdirs();
				
				if(!created) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel criar o diretorio");
				}
			}
			
			File dest = new File(UPLOAD_DIR +"/"+ id +"/" + fileName);
			file.transferTo(dest);
			
			return ResponseEntity.ok("Arquivo enviado com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo!");
		}
	}

}
