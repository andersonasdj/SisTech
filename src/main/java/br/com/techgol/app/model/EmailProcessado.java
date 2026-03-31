package br.com.techgol.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EmailProcessado {

    @Id
    private String messageId;

    private LocalDateTime dataProcessamento;

}