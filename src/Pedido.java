public class Pedido {

    private int id;
    private Pizza pizza;
    private Pagamento pagamento;
    private Cliente cliente;
    private double valorTotal;

    public Pedido(Pizza pizza, Pagamento pagamento, Cliente cliente) {
        this.id = id;
        this.pizza = pizza;
        this.pagamento = pagamento;
        this.cliente = cliente;
        this.valorTotal = pizza.calcularValorTotal();
    }

    public void finalizarPedido() {
        this.valorTotal = pizza.calcularValorTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}
