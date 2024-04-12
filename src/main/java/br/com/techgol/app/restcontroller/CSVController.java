package br.com.techgol.app.restcontroller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.services.CSVService;

@RestController
@RequestMapping("/api/csv")
public class CSVController {

  @Autowired
  CSVService fileService;
  
  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "solicitacoes-"+LocalDateTime.now().withNano(0)+".csv";
    InputStreamResource file = new InputStreamResource(fileService.loadNaoFinalizadas());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }
  
  
  @GetMapping("/relatorio/cliente/{id}/inicio/{inicio}/fim/{fim}")
  public ResponseEntity<Resource> getFileRelatorio(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim) {
    String filename = "relatorio-"+LocalDateTime.now().withNano(0)+".csv";
    InputStreamResource file = new InputStreamResource(fileService.loadRelatorioPorCliente(id, inicio, fim));

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }
  
  @GetMapping("/relatorio/funcionario/{id}/inicio/{inicio}/fim/{fim}")
  public ResponseEntity<Resource> getFileRelatorioPorFuncionario(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim) {
    String filename = "relatorio-"+LocalDateTime.now().withNano(0)+".csv";
    InputStreamResource file = new InputStreamResource(fileService.loadRelatorioPorFuncionario(id, inicio, fim));

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }
  
//  @PostMapping("/relatorio")
//  public ResponseEntity<Resource> getFileRelatorio(@RequestBody DtoDadosRelatorioCsv dto) {
//    String filename = "relatorio-"+LocalDateTime.now().withNano(0)+".csv";
//    InputStreamResource file = new InputStreamResource(fileService.loadRelatorio(dto));
//
//    return ResponseEntity.ok()
//        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//        .contentType(MediaType.parseMediaType("application/csv"))
//        .body(file);
//  }
  
}
