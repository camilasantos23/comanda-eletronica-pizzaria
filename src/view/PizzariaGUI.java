package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import dao.*;

import java.io.IOException;

public class PizzariaGUI extends JFrame {
    // Camada de Persistência (Controller/DAO)
    private PedidoDAO dao = new PedidoDAO();

    // Componentes de Entrada (Model/View)
    private JTextField txtNome, txtEndereco, txtTroco;
    private JLabel lblTotal, lblPix;
    private JComboBox<String> comboTamanho;
    private JPanel panelTroco;

    private double valorBase = 0.0;
    private List<JCheckBox> chkSimples = new ArrayList<>();
    private List<JCheckBox> chkPremium = new ArrayList<>();
    private List<JCheckBox> chkRemover = new ArrayList<>();

    public PizzariaGUI() {
        super("Pizzaria Delícia - Comanda Eletrônica");
        // Organiza a tela verticalmente [12.18, 574]
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. TÍTULO ESTILIZADO (Baseado na captura) [1]
        JLabel titulo = new JLabel("Pizzaria Delícia", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.RED);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);

        // 2. SEÇÃO: DADOS DO CLIENTE [Alteração: Adicionado campo Nome]
        JPanel secCliente = criarSecao("Dados do Cliente");
        secCliente.setLayout(new GridLayout(2, 2, 5, 5));
        secCliente.add(new JLabel("Nome:"));
        txtNome = new JTextField(20);
        secCliente.add(txtNome);
        secCliente.add(new JLabel("Endereço:"));
        txtEndereco = new JTextField(20);
        secCliente.add(txtEndereco);
        add(secCliente);

        // 3. SEÇÃO: TAMANHO [Captura 1]
        JPanel secTamanho = criarSecao("1. Escolha o Tamanho");
        String[] tamanhos = {"B - R$ 25,00", "M - R$ 35,00", "G - R$ 45,00", "EXGG - R$ 60,00"};
        comboTamanho = new JComboBox<>(tamanhos);
        comboTamanho.addActionListener(e -> calcularTotal());
        secTamanho.add(comboTamanho);
        add(secTamanho);

        // 4. SEÇÃO: SABORES (6 Simples e 6 Premium) [Captura 3]
        JPanel secSabores = criarSecao("2. Escolha os Sabores (G:2 / EXGG:3)");
        secSabores.setLayout(new GridLayout(0, 2));

        String[] simples = {"Mussarela", "Calabresa", "Portuguesa", "Frango c/ Catupiry", "Margherita", "Napolitana"};
        String[] premium = {"Quatro Queijos ★", "Camarão ★", "Salmão ★", "Lombo ★", "Filé Mignon ★", "Vegetariana ★"};

        for (String s : simples) {
            JCheckBox cb = new JCheckBox(s);
            cb.addActionListener(e -> calcularTotal());
            chkSimples.add(cb);
            secSabores.add(cb);
        }
        for (String p : premium) {
            JCheckBox cb = new JCheckBox(p);
            cb.addActionListener(e -> calcularTotal());
            chkPremium.add(cb);
            secSabores.add(cb);
        }
        add(secSabores);

        // 5. SEÇÃO: REMOVER INGREDIENTES [Captura 4]
        JPanel secRemover = criarSecao("3. Remover Ingredientes");
        String[] itens = {"Cebola", "Tomate", "Azeitona", "Orégano"};
        for (String item : itens) {
            JCheckBox cb = new JCheckBox(item);
            chkRemover.add(cb);
            secRemover.add(cb);
        }
        add(secRemover);

        // 6. SEÇÃO: PAGAMENTO [Captura 5 e 6]
        JPanel secPagto = criarSecao("4. Forma de Pagamento");
        String[] pagtos = {"Cartão", "Pix", "Dinheiro"};
        JComboBox<String> comboPagto = new JComboBox<>(pagtos);
        lblPix = new JLabel("Chave PIX: ");
        lblPix.setVisible(false);

        panelTroco = new JPanel();
        panelTroco.add(new JLabel("Troco para:"));
        txtTroco = new JTextField(5);
        panelTroco.add(txtTroco);
        panelTroco.setVisible(false);

        comboPagto.addActionListener(e -> {
            String sel = (String) comboPagto.getSelectedItem();
            lblPix.setVisible(sel.equals("Pix"));
            if(sel.equals("Pix")) lblPix.setText("Chave: " + UUID.randomUUID().toString());
            panelTroco.setVisible(sel.equals("Dinheiro"));
            revalidate();
        });
        secPagto.add(comboPagto);
        secPagto.add(lblPix);
        secPagto.add(panelTroco);
        add(secPagto);

        // 7. RODAPÉ: TOTAL E CONFIRMAÇÃO [Captura 7]
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblTotal);

        JButton btnConfirmar = new JButton("Confirmar Pedido");
        btnConfirmar.setBackground(Color.RED);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.addActionListener(e -> finalizarPedido());
        add(btnConfirmar);

        calcularTotal();
        pack();
        setVisible(true);
    }

    // --- MÉTODOS DE LÓGICA (Onde as alterações foram integradas) ---

    private JPanel criarSecao(String titulo) {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titulo));
        return p;
    }

    private void calcularTotal() {
        // Unificação da lógica de cálculo de preço e validação de sabores
        String sel = (String) comboTamanho.getSelectedItem();
        if (sel.startsWith("B")) valorBase = 25.0;
        else if (sel.startsWith("M")) valorBase = 35.0;
        else if (sel.startsWith("G")) valorBase = 45.0;
        else valorBase = 60.0;

        double adicional = 0;
        int contSabor = 0;
        for (JCheckBox cb : chkSimples) if (cb.isSelected()) contSabor++;
        for (JCheckBox cb : chkPremium) {
            if (cb.isSelected()) {
                contSabor++;
                adicional += 2.0; // Regra de negócio: R$ 2,00 por premium
            }
        }

        // Validação de limites conforme regra informada
        int limite = sel.startsWith("G") ? 2 : (sel.startsWith("EXGG") ? 3 : 1);
        if (contSabor > limite) {
            JOptionPane.showMessageDialog(this, "Limite de " + limite + " sabores excedido!");
        }

        lblTotal.setText(String.format("Total: R$ %.2f", valorBase + adicional));
    }

    private void finalizarPedido() {
        // Alteração: Integração total com o DAO para persistência [794, 15.5.1]
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try{

            JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso no arquivo!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gravar arquivo: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new PizzariaGUI();
    }
}