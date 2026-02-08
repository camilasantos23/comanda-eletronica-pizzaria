package view;

import dao.PedidoDAO;
import model.*;
import exception.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.IOException;

public class PizzariaGUI extends JFrame {
    private PedidoDAO dao = new PedidoDAO();
    private JTextField txtNome, txtEndereco, txtTroco;
    private JLabel lblTotal, lblPix;
    private JComboBox<String> comboTamanho, comboPagto;
    private JCheckBox chkPrecisaTroco; // Nova CheckBox para troco [12.10.1]
    private JPanel panelTroco;

    private double valorBase = 0.0;
    private double adicionalPremium = 0.0;

    private List<JCheckBox> chkSimples = new ArrayList<>();
    private List<JCheckBox> chkPremium = new ArrayList<>();
    private List<JCheckBox> chkRemover = new ArrayList<>();

    public PizzariaGUI() {
        super("Pizzaria Express - Comanda Eletrônica");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. TÍTULO
        JLabel titulo = new JLabel("Pizzaria Express", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.RED);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);

        // 2. DADOS DO CLIENTE [12.6]
        JPanel secCliente = criarSecao("Dados do Cliente");
        secCliente.setLayout(new GridLayout(2, 2, 5, 5));
        secCliente.add(new JLabel("Nome:"));
        txtNome = new JTextField(20);
        secCliente.add(txtNome);
        secCliente.add(new JLabel("Endereço:"));
        txtEndereco = new JTextField(20);
        secCliente.add(txtEndereco);
        add(secCliente);

        // 3. TAMANHO [12.11]
        JPanel secTamanho = criarSecao("1. Escolha o Tamanho");
        String[] tamanhos = {"B - R$ 25,00", "M - R$ 35,00", "G - R$ 45,00", "EXGG - R$ 60,00"};
        comboTamanho = new JComboBox<>(tamanhos);
        comboTamanho.addActionListener(e -> tentarCalcular());
        secTamanho.add(comboTamanho);
        add(secTamanho);

        // 4. SABORES
        JPanel secSabores = criarSecao("2. Escolha os Sabores (G:2 / EXGG:3)");
        secSabores.setLayout(new GridLayout(0, 2));
        String[] simples = {"Mussarela", "Calabresa", "Portuguesa", "Frango", "Margherita", "Napolitana"};
        String[] premium = {"Quatro Queijos ★", "Camarão ★", "Salmão ★", "Lombo ★", "Filé Mignon ★", "Vegetariana ★"};

        for (String s : simples) {
            JCheckBox cb = new JCheckBox(s);
            cb.addActionListener(e -> tentarCalcular());
            chkSimples.add(cb);
            secSabores.add(cb);
        }
        for (String p : premium) {
            JCheckBox cb = new JCheckBox(p);
            cb.addActionListener(e -> tentarCalcular());
            chkPremium.add(cb);
            secSabores.add(cb);
        }
        add(secSabores);

        // 5. REMOVER INGREDIENTES
        JPanel secRemover = criarSecao("3. Remover Ingredientes");
        String[] itens = {"Cebola", "Tomate", "Azeitona", "Orégano"};
        for (String item : itens) {
            JCheckBox cb = new JCheckBox(item);
            chkRemover.add(cb);
            secRemover.add(cb);
        }
        add(secRemover);

        // 6. PAGAMENTO E LÓGICA DE TROCO [12.11, 12.10.1]
        JPanel secPagto = criarSecao("4. Forma de Pagamento");
        String[] pagtos = {"Cartão", "Pix", "Dinheiro"};
        comboPagto = new JComboBox<>(pagtos);

        lblPix = new JLabel("Chave PIX: ");
        lblPix.setVisible(false);

        chkPrecisaTroco = new JCheckBox("Precisa de troco?");
        chkPrecisaTroco.setVisible(false);

        panelTroco = new JPanel();
        panelTroco.add(new JLabel("Pagar com: R$"));
        txtTroco = new JTextField(5);
        panelTroco.add(txtTroco);
        panelTroco.setVisible(false);

        comboPagto.addActionListener(e -> {
            String sel = (String) comboPagto.getSelectedItem();
            boolean eDinheiro = sel.equals("Dinheiro");
            lblPix.setVisible(sel.equals("Pix"));
            if(sel.equals("Pix")) lblPix.setText("Chave: " + UUID.randomUUID().toString());

            chkPrecisaTroco.setVisible(eDinheiro);
            if (!eDinheiro) {
                panelTroco.setVisible(false);
                chkPrecisaTroco.setSelected(false);
            }
            revalidate();
            repaint();
        });

        chkPrecisaTroco.addActionListener(e -> {
            panelTroco.setVisible(chkPrecisaTroco.isSelected());
            revalidate();
        });

        secPagto.add(comboPagto);
        secPagto.add(lblPix);
        secPagto.add(chkPrecisaTroco);
        secPagto.add(panelTroco);
        add(secPagto);

        // 7. RODAPÉ
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

        tentarCalcular();
        pack();
        setVisible(true);
    }

    private void tentarCalcular() {
        try {
            calcularTotal();
        } catch (LimiteSaboresExcedidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void calcularTotal() throws LimiteSaboresExcedidoException {
        String sel = (String) comboTamanho.getSelectedItem();
        if (sel.startsWith("B")) valorBase = 25.0;
        else if (sel.startsWith("M")) valorBase = 35.0;
        else if (sel.startsWith("G")) valorBase = 45.0;
        else valorBase = 60.0;

        adicionalPremium = 0;
        int contSabor = 0;
        for (JCheckBox cb : chkSimples) if (cb.isSelected()) contSabor++;
        for (JCheckBox cb : chkPremium) {
            if (cb.isSelected()) {
                contSabor++;
                adicionalPremium += 2.0;
            }
        }

        int limite = sel.startsWith("G") ? 2 : (sel.startsWith("EXGG") ? 3 : 1);
        if (contSabor > limite) {
            throw new LimiteSaboresExcedidoException("O tamanho " + sel + " permite apenas " + limite + " sabores!");
        }

        lblTotal.setText(String.format("Total: R$ %.2f", valorBase + adicionalPremium));
    }

    private void finalizarPedido() {
        try {
            if (txtNome.getText().trim().isEmpty()) throw new CampoObrigatorioException("Nome é obrigatório!");
            if (txtEndereco.getText().trim().isEmpty()) throw new CampoObrigatorioException("Endereço é obrigatório!");

            double totalGeral = valorBase + adicionalPremium;
            String infoTroco = "Não precisa.";

            // Lógica de Cálculo do Troco [2.7, 11.3]
            if (comboPagto.getSelectedItem().equals("Dinheiro") && chkPrecisaTroco.isSelected()) {
                if (txtTroco.getText().isEmpty()) throw new CampoObrigatorioException("Informe o valor para o troco!");

                double valorPago = Double.parseDouble(txtTroco.getText().replace(",", "."));
                if (valorPago < totalGeral) {
                    throw new IllegalArgumentException("Valor pago é insuficiente!");
                }
                infoTroco = String.format("R$ %.2f", valorPago - totalGeral);
            }

            // Instanciação e Persistência [15.5.1]
            Cliente cliente = new Cliente(1, txtNome.getText(), "");
            cliente.setEndereco(txtEndereco.getText());
            Pizza pizza = new Pizza((String) comboTamanho.getSelectedItem(), totalGeral);

            List<String> removidos = new ArrayList<>();
            for (JCheckBox cb : chkRemover) if (cb.isSelected()) removidos.add(cb.getText());
            pizza.setIngredientesExcluidos(removidos);

            Pedido pedido = new Pedido(pizza, new Pagamento((String) comboPagto.getSelectedItem()), cliente);
            dao.salvar(pedido);

            exibirConfirmacao(pedido, infoTroco);

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Digite um valor numérico válido para o troco!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirConfirmacao(Pedido pedido, String troco) {
        JDialog dialog = new JDialog(this, "Pedido Recebido", true);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Carregamento e Redimensionamento do Ícone
        ImageIcon iconOriginal = new ImageIcon(getClass().getResource("/image/verificado.png"));
        // Redimensiona para 60x60 pixels (ou o tamanho que preferir) para não ocupar a tela toda [13.2]
        Image imagemRedimensionada = iconOriginal.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon iconEscalado = new ImageIcon(imagemRedimensionada);

        JLabel lblIcon = new JLabel(iconEscalado);
        // Garante que o JLabel do ícone fique no centro horizontal do BoxLayout [12.18]
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblIcon);

        mainPanel.add(Box.createVerticalStrut(10));


        JLabel lblStatus = new JLabel("Pedido Confirmado!");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 18));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStatus);
        // 3. Frase "Seu pedido foi recebido com sucesso"
        JLabel lblSub = new JLabel("Seu pedido foi recebido com sucesso");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        // Garante que a frase também fique centralizada [1]
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblSub);

        mainPanel.add(new JSeparator());


        // CONSTRUÇÃO DO RECIBO [12.20]
        JTextArea recibo = new JTextArea();
        recibo.setEditable(false);
        recibo.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        sb.append("CLIENTE: ").append(pedido.getCliente().getNome()).append("\n");
        sb.append("ENDEREÇO: ").append(pedido.getCliente().getEndereco()).append("\n");
        sb.append("------------------------------\n");
        sb.append("PIZZA: ").append(pedido.getPizza().getTamanho()).append("\n");
        sb.append("SABORES SELECIONADOS:\n");
        for (JCheckBox cb : chkSimples) if (cb.isSelected()) sb.append(" - ").append(cb.getText()).append("\n");
        for (JCheckBox cb : chkPremium) if (cb.isSelected()) sb.append(" - ").append(cb.getText()).append(" (★)\n");

        if (!pedido.getPizza().getIngredientesExcluidos().isEmpty()) {
            sb.append("REMOVER: ").append(String.join(", ", pedido.getPizza().getIngredientesExcluidos())).append("\n");
        }

        sb.append("\nPAGAMENTO: ").append(pedido.getPagamento().getTipo()).append("\n");
        if (pedido.getPagamento().getTipo().equals("Dinheiro")) {
            sb.append("TROCO: ").append(troco).append("\n");
        }

        sb.append("\nTOTAL A PAGAR: ").append(lblTotal.getText());
        recibo.setText(sb.toString());

        mainPanel.add(new JScrollPane(recibo));
        JButton btnOk = new JButton("Novo Pedido");
        btnOk.addActionListener(e -> dialog.dispose());
        mainPanel.add(btnOk);

        dialog.add(mainPanel);
        dialog.setSize(350, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel criarSecao(String titulo) {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titulo));
        return p;
    }

    public static void main(String[] args) { new PizzariaGUI(); }
}