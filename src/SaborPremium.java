public class SaborPremium {

    private int id;
    private String nome;
    private double valorAdicional;

    public SaborPremium(int id, String nome, double valorAdicional) {
        this.id = id;
        this.nome = nome;
        this.valorAdicional = valorAdicional;
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

    public double getValorAdicional() {
        return valorAdicional;
    }

    public void setValorAdicional(double valorAdicional) {
        this.valorAdicional = valorAdicional;
    }

    @Override
    public String toString(){
        return id + ";" + nome + ";" + valorAdicional;
    }
}