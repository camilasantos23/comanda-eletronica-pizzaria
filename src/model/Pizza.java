package model;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Pizza implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tamanho; // B, M, G, XGG
    private List<Sabor> sabores = new ArrayList<>();
    private List<String> ingredientesExcluidos = new ArrayList<>();

    public Pizza(String tamanho, double valorBase) { this.tamanho = tamanho; }

    public void adicionarSabor(Sabor sabor) {
        if (tamanho.equals("G") && sabores.size() >= 2) throw new IllegalArgumentException("G pode no máximo 2 sabores.");
        if (tamanho.equals("XGG") && sabores.size() >= 3) throw new IllegalArgumentException("XGG pode no máximo 3 sabores.");
        sabores.add(sabor);
    }

    public double getPrecoBase() {
        return switch (tamanho) {
            case "B" -> 25.0;
            case "M" -> 35.0;
            case "G" -> 45.0;
            case "XGG" -> 60.0;
            default -> 0.0;
        };
    }

    public double calcularTotal() {
        double total = getPrecoBase();
        for (Sabor s : sabores) total += s.getAdicional();
        return total;
    }

    public void adicionarSaborSimples(String s) {
    }

    public List<Sabor> getSabores() {

        return sabores;
    }

    public void setSabores(List<Sabor> sabores) {

        this.sabores = sabores;
    }

    public List<String> getIngredientesExcluidos() {

        return ingredientesExcluidos;
    }

    public void setIngredientesExcluidos(List<String> ingredientesExcluidos) {
        this.ingredientesExcluidos = ingredientesExcluidos;
    }

    public char[] getTamanho() {
        return tamanho.toCharArray();
    }
}