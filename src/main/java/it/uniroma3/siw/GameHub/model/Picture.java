package it.uniroma3.siw.GameHub.model;


import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String nome;
    @NotNull
    private byte[] foto;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public byte[] getFoto() {
        return foto;
    }
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getImgData() {
        return Base64.getMimeEncoder().encodeToString(foto);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(foto);
        result = prime * result + Objects.hash(nome);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Picture other = (Picture) obj;
        return Arrays.equals(foto, other.foto) && Objects.equals(nome, other.nome);
    }


}
