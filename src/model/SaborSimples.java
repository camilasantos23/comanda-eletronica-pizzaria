package model;

public class SaborSimples extends Sabor {
    public SaborSimples(String nome) { super(nome); }
    @Override public double getAdicional() {
        return 0.0;
    }
}