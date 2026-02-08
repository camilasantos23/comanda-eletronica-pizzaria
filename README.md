
🍕 Pizzaria Express - Comanda Eletrônica

📝 Visão Geral

O projeto consiste em uma aplicação desktop para automação de comandas de pizzaria, permitindo a gestão completa de pedidos, desde a identificação do cliente até seu endereço, forma de pagamento, escolha do sabor da pizza e tamanho.

🛠 Tecnologias Utilizadas

• Linguagem: Java SE 8.
• Interface Gráfica: Java Swing (javax.swing).
• Persistência: Serialização de Objetos em arquivos binários (.ser).
• Versionamento: Git e GitHub.

🚀 Funcionalidades Principais

1. Gestão de Clientes e Entrega
• Captura de nome e endereço de entrega.
• Validação de campos obrigatórios via exceções personalizadas para evitar dados inconsistentes.

2. Customização da Pizza
• Seleção de Tamanhos: Suporte para tamanhos B, M, G e EXGG com preços dinâmicos.
• Escolha de Sabores: Catálogo dividido entre Sabores Simples e Sabores Premium (com taxa adicional de R$ 2,00 por sabor).
• Remoção de Ingredientes: Opção para excluir itens como cebola, azeitona e tomate, armazenados em uma lista específica no modelo da pizza.

3. Sistema de Pagamento Inteligente
• Opções: Cartão, Pix e Dinheiro.
• Pix: Geração automática de chave aleatória (UUID) na interface.
• Dinheiro: Campo dinâmico para cálculo de troco.
4. Confirmação e Recibo Visual
• Após a confirmação, o sistema exibe um recibo detalhado em um diálogo personalizado.
• O recibo inclui ícone visual de sucesso, resumo da pizza, lista de sabores, ingredientes removidos, endereço de entrega e valor total.

🏗 Arquitetura e Regras de Negócio

Padrão MVC
• Model: Classes Cliente, Pizza, Pedido e Pagamento. Todas implementam Serializable para permitir a gravação em arquivo.
• View: PizzariaGUI, responsável por toda a interação visual e tratamento de eventos.
• DAO (Controller): PedidoDAO, que gerencia a persistência dos objetos Pedido no arquivo pedidos.ser.

Validações e Exceções

O sistema utiliza um pacote dedicado de exception para tratar regras de domínio:
• Limite de Sabores: Pizzas tamanho G permitem até 2 sabores; EXGG permitem até 3. Caso excedido, o sistema lança LimiteSaboresExcedidoException.
• Campos Vazios: Lança CampoObrigatorioException se o nome ou endereço não forem preenchidos.

💾 Persistência de Dados

Em conformidade com os requisitos do projeto, os dados não são perdidos ao fechar a aplicação. O sistema utiliza ObjectOutputStream para salvar o estado completo do pedido, garantindo que o objeto Cliente (com seu endereço) e os detalhes da Pizza sejam preservados.
