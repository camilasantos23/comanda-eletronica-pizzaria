package model;
import java.io.Serializable;

public class Cliente implements  Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String endereco;

    public Cliente(int id, String nome, String endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }


    public void setEndereco(String endereco) {
        this.endereco = endereco;

    }



    @Override
    public String toString(){
        return id + ";" + nome ;
    }


    public Object getEndereco() {
        return endereco;
    }



}