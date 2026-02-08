import java.util.ArrayList;
import java.util.List;

public class Pizza {

    private int id;
    private String tamanho;
    private double valorBase;
    private List<SaborPremium> saboresPremium;

    public Pizza(String tamanho, double valorBase) {
        this.id = id;
        this.tamanho = tamanho;
        this.valorBase = valorBase;
        this.saboresPremium = new ArrayList<>();
    }

    public void adicionarSaborPremium(SaborPremium sabor) {
        saboresPremium.add(sabor);
    }

    public double calcularValorTotal(){
        double total = valorBase;
        for (SaborPremium sabor: saboresPremium) {
            total += sabor.getValorAdicional();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public List<SaborPremium> getSaboresPremium() {
        return saboresPremium;
    }
}
