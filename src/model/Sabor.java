package model;

public abstract class Sabor {
    private String nome;
    public Sabor(String nome) { this.nome = nome; }
    public String getNome() { return nome; }
    public abstract double getAdicional();
}