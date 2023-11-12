package com.example.agendafirebaseteste;

import java.util.Objects;

public class Agenda {

    private int id;
    private String nome;
    private String telefone;

    public Agenda() {

    }

    public Agenda(int id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return id == agenda.id && Objects.equals(nome, agenda.nome) && Objects.equals(telefone, agenda.telefone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, telefone);
    }
}
