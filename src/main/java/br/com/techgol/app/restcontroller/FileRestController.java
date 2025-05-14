package br.com.techgol.app.restcontroller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgol.app.dto.DtoFile;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.services.FuncionarioService;

@RestController
@RequestMapping("/api/file")
public class FileRestController {
	
	@Autowired
	FuncionarioService service;
	
	@Value("${upload.dir}")
	private String UPLOAD_DIR;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@ModelAttribute DtoFile uploadRequest){
		
		MultipartFile file = uploadRequest.file();
		String id = uploadRequest.id();
		
		if(file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("o arquivo está vazio");
		}
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			File uploadDir = new File(UPLOAD_DIR+"/solicitacoes/"+id+"/");
			
			if(!uploadDir.exists()) {
				boolean created = uploadDir.mkdirs();
				
				if(!created) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel criar o diretorio");
				}
			}
			
			File dest = new File(UPLOAD_DIR +"/solicitacoes/"+ id +"/" + fileName);
			file.transferTo(dest);
			
			return ResponseEntity.ok("Arquivo enviado com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo!");
		}
	}
	
	@PostMapping("/perfil/upload")
	public ResponseEntity<String> perfilUploadFile(@ModelAttribute DtoFile uploadRequest){
		
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(uploadRequest.id().equals(funcionario.getId().toString())) {
			MultipartFile file = uploadRequest.file();
			String id = uploadRequest.id();

			String contentType = file.getContentType();
			String fileNameTest = file.getOriginalFilename();
			
			if(!isValidImage(contentType, fileNameTest)) {
				return ResponseEntity.badRequest().body("Somente arquivos JPEG e PNG são permitidos");
			}
			
			if(file.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("o arquivo está vazio");
			}
			
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			try {
				File uploadDir = new File(UPLOAD_DIR+"/perfil/"+id+"/");
				
				if(!uploadDir.exists()) {
					boolean created = uploadDir.mkdirs();
					
					if(!created) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel criar o diretorio");
					}
				}
				Path diretoriParaLimpar = Paths.get(uploadDir.toString());
				
				Files.list(diretoriParaLimpar)
				.filter(Files::isRegularFile)
				.forEach(path -> {
					try {
						Files.delete(path);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				
				File dest = new File(UPLOAD_DIR +"/perfil/"+ id +"/" + fileName);
				file.transferTo(dest);
				
				service.atualizaImagem(funcionario.getId(), "/perfil/"+ id +"/" + fileName);
				
				return ResponseEntity.ok("Arquivo enviado com sucesso!");
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo!");
			}
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo!");
		}
	}
	
	private boolean isValidImage(String contentType, String fileName) {
		return (contentType != null && (
				contentType.equalsIgnoreCase("image/jpeg") ||
				contentType.equalsIgnoreCase("image/png")
				)) &&
				(fileName  != null && (
						fileName.toLowerCase().endsWith(".jpg") ||
						fileName.toLowerCase().endsWith(".jpeg") ||
						fileName.toLowerCase().endsWith(".png")
						));
	}
	
	@GetMapping("/perfil/{id}")
	public ResponseEntity<Resource> exibirImagem(@PathVariable Long id){
		
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(id == funcionario.getId()) {
			Path diretorioImagens = Paths.get(UPLOAD_DIR);
			
			try {
				Path caminhoArquivo = diretorioImagens.resolve(diretorioImagens + funcionario.getCaminhoFoto()).normalize();
				Resource recurso = new UrlResource(caminhoArquivo.toUri());
				
				if(!recurso.exists()) {
					return ResponseEntity.notFound().build();
				}
				
				String contentType = Files.probeContentType(caminhoArquivo);
				
				if(contentType == null) {
					contentType = "application/octet-stream";
				}
				
				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(contentType))
						.header("Content-Disposition", "inline; filename=\"" + recurso.getFilename() + "\"")
						.body(recurso);
				
			} catch (MalformedURLException e) {
				return ResponseEntity.badRequest().build();
			} catch (Exception e) {
				return ResponseEntity.internalServerError().build();
			}
		}else {
			return ResponseEntity.internalServerError().build();
		}
		
	}

}
