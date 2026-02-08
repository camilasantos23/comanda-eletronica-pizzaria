package model;

public class SaborPremium extends Sabor {
    public SaborPremium(int i, String nome, double v) { super(nome); }
    @Override public double getAdicional() {
        return 2.0;
    } // Taxa de R$ 2,00
}