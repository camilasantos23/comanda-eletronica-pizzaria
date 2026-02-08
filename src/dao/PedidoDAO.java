package dao;
import model.Pedido;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private final String FILE_PATH = "pedidos.ser";

    // Create (Salvar Pedido) [3, 6]
    public void salvar(Pedido pedido) throws IOException {
        List<Pedido> pedidos = listarTodos();
        pedidos.add(pedido);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(pedidos); // Grava a lista atualizada [6]
        }
    }

    // Read (Listar Pedidos) [6, 7]
    @SuppressWarnings("unchecked")
    public List<Pedido> listarTodos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Pedido>) in.readObject(); // Desserializa a lista [6, 8]
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
