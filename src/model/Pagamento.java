package model;
import java.io.Serializable;
import java.util.UUID;

public class Pagamento implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tipo; // Pix, Dinheiro, Cartão
    private double valorPago;

    public Pagamento(String selectedItem) {
        this.tipo = selectedItem;
    }

    public String processar(double total) {
        if (tipo.equalsIgnoreCase("Pix")) {
            return "Chave PIX Gerada: " + UUID.randomUUID().toString(); // Chave automática
        } else if (tipo.equalsIgnoreCase("Dinheiro")) {
            if (valorPago > total) return "Troco: R$ " + (valorPago - total);
            return "Pagamento exato em dinheiro.";
        }
        return "Pagamento via Cartão Confirmado.";
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
}