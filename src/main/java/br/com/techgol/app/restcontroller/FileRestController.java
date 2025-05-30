package br.com.techgol.app.restcontroller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.services.FuncionarioService;
import br.com.techgol.app.services.SolicitacaoService;

@RestController
@RequestMapping("/api/file")
public class FileRestController {
	
	@Autowired
	FuncionarioService service;
	
	@Autowired
	SolicitacaoService solicitacaoService;
	
	@Value("${upload.dir}")
	private String UPLOAD_DIR;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@ModelAttribute DtoFile uploadRequest){
		
		MultipartFile file = uploadRequest.file();
		Long id = uploadRequest.id();
		
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
	public ResponseEntity<String> perfilUploadFile(@ModelAttribute DtoFile uploadRequest) {
	    Funcionario funcionario = service.buscaPorNome(
	        ((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario()
	    );

	    if (!uploadRequest.id().equals(funcionario.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a alterar este perfil.");
	    }

	    MultipartFile file = uploadRequest.file();
	    Long id = uploadRequest.id();
	    String contentType = file.getContentType();
	    String originalFileName = file.getOriginalFilename();

	    if (!isValidImage(contentType, originalFileName)) {
	        return ResponseEntity.badRequest().body("Somente arquivos JPEG e PNG são permitidos");
	    }

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("O arquivo está vazio");
	    }

	    try {
	        File uploadDir = new File(UPLOAD_DIR + "/perfil/" + id + "/");
	        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar o diretório");
	        }

	        // Limpa arquivos existentes
	        Files.list(uploadDir.toPath())
	            .filter(Files::isRegularFile)
	            .forEach(path -> {
	                try {
	                    Files.delete(path);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            });

	        BufferedImage originalImage = ImageIO.read(file.getInputStream());
	        if (originalImage == null) {
	            return ResponseEntity.badRequest().body("Arquivo inválido.");
	        }

	        // Cria nova imagem RGB com fundo branco (para remover transparência)
	        BufferedImage newImage = new BufferedImage(
	            originalImage.getWidth(),
	            originalImage.getHeight(),
	            BufferedImage.TYPE_INT_RGB
	        );
	        Graphics2D g = newImage.createGraphics();
	        g.setColor(Color.WHITE); // fundo branco
	        g.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
	        g.drawImage(originalImage, 0, 0, null);
	        g.dispose();

	        // Salva como JPEG com compressão
	        String finalFileName = "perfil_" + id + ".jpg";
	        Path destino = Paths.get(uploadDir.getAbsolutePath(), finalFileName);

	        try (OutputStream os = Files.newOutputStream(destino)) {
	            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
	            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
	            writer.setOutput(ios);

	            ImageWriteParam param = writer.getDefaultWriteParam();
	            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	            param.setCompressionQuality(0.2f); // 0.0 = baixa qualidade, 1.0 = alta

	            writer.write(null, new IIOImage(newImage, null, null), param);
	            ios.close();
	            writer.dispose();
	        }

	        service.atualizaImagem(funcionario.getId(), "/perfil/" + id + "/" + finalFileName);
	        return ResponseEntity.ok("Arquivo enviado com sucesso!");

	    } catch (IOException e) {
	        e.printStackTrace();
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
	
	
	
	@PostMapping("/solicitacao/upload")
	public ResponseEntity<String> solicitacaoUploadFile(@ModelAttribute DtoFile uploadRequest) {
	    
	    Solicitacao solicitacao = solicitacaoService.buscarPorId(uploadRequest.id());
	    
	    if (!uploadRequest.id().equals(solicitacao.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a alterar esta solicitacao.");
	    }

	    MultipartFile file = uploadRequest.file();
	    Long id = uploadRequest.id();
	    String contentType = file.getContentType();
	    String originalFileName = file.getOriginalFilename();

	    if (!isValidImage(contentType, originalFileName)) {
	        return ResponseEntity.badRequest().body("Somente arquivos JPEG e PNG são permitidos");
	    }

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("O arquivo está vazio");
	    }

	    try {
	        File uploadDir = new File(UPLOAD_DIR + "/solicitacoes/" + id + "/");
	        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar o diretório");
	        }

	        // Limpa arquivos existentes
	        Files.list(uploadDir.toPath())
	            .filter(Files::isRegularFile)
	            .forEach(path -> {
	                try {
	                    Files.delete(path);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            });

	        BufferedImage originalImage = ImageIO.read(file.getInputStream());
	        if (originalImage == null) {
	            return ResponseEntity.badRequest().body("Arquivo inválido.");
	        }

	        // Cria nova imagem RGB com fundo branco (para remover transparência)
	        BufferedImage newImage = new BufferedImage(
	            originalImage.getWidth(),
	            originalImage.getHeight(),
	            BufferedImage.TYPE_INT_RGB
	        );
	        Graphics2D g = newImage.createGraphics();
	        g.setColor(Color.WHITE); // fundo branco
	        g.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
	        g.drawImage(originalImage, 0, 0, null);
	        g.dispose();

	        // Salva como JPEG com compressão
	        String finalFileName = "solicitacao_" + id + ".jpg";
	        Path destino = Paths.get(uploadDir.getAbsolutePath(), finalFileName);

	        try (OutputStream os = Files.newOutputStream(destino)) {
	            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
	            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
	            writer.setOutput(ios);

	            ImageWriteParam param = writer.getDefaultWriteParam();
	            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	            param.setCompressionQuality(0.5f); // 0.0 = baixa qualidade, 1.0 = alta

	            writer.write(null, new IIOImage(newImage, null, null), param);
	            ios.close();
	            writer.dispose();
	        }

	        solicitacaoService.atualizaArquivo(solicitacao.getId(), "/solicitacoes/" + id + "/" + finalFileName);
	        return ResponseEntity.ok("Arquivo enviado com sucesso!");

	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo!");
	    }
	}
	
	
	@GetMapping("/solicitacao/{id}")
	public ResponseEntity<Resource> baixarAnexo(@PathVariable Long id) {
		
	    try {
	        Solicitacao solicitacao = solicitacaoService.buscarPorId(id);

	        // Caminho completo do anexo
	        Path caminhoArquivo = Paths.get(UPLOAD_DIR).resolve(UPLOAD_DIR + solicitacao.getAnexo()).normalize();
	        Resource recurso = new UrlResource(caminhoArquivo.toUri());

	        if (!recurso.exists() || !recurso.isReadable()) {
	            return ResponseEntity.notFound().build();
	        }

	        String contentType = Files.probeContentType(caminhoArquivo);
	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
	            .body(recurso);

	    } catch (MalformedURLException e) {
	        return ResponseEntity.badRequest().build();
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}


}
