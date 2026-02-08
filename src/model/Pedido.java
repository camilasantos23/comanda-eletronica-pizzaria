package model;

import java.io.Serializable;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private Cliente cliente;
    private Pizza pizza;
    private Pagamento pagamento;
    private String tipoEntrega; // Delivery ou Retirada

    public Pedido(Pizza pizza, Pagamento pagto, Cliente cliente) {
        this.pizza = pizza;
        this.pagamento = pagto;
        this.cliente = cliente;
    }

    public String confirmarPedido() {
        return String.format("Pedido Confirmado!\nCliente: %s\nEndereço: %s\nTotal: R$ %.2f\nPagamento: %s",
                cliente.getNome(), cliente.getEndereco(), pizza.calcularTotal(), pagamento.getTipo());
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Pizza getPizza() {
        return pizza;
    }
}