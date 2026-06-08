import java.util.ArrayList;
import java.util.Scanner;

public class MenuCompra {

    public static void realizar(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("        REALIZAR COMPRA");
        System.out.println("====================================");

        // Identificação do cliente
        Cliente clienteAtual = null;
        System.out.print("Você é cliente cadastrado? (S/N): ");
        String resposta = sc.nextLine().trim().toUpperCase();

        if (resposta.equals("S")) {
            System.out.print("Digite o seu CPF: ");
            String cpf = sc.nextLine().trim();
            clienteAtual = MenuCliente.encontrarPorCpf(cpf);
            if (clienteAtual == null) {
                System.out.println("[AVISO] CPF não encontrado. Prosseguindo como cliente não identificado.");
            } else {
                System.out.println("Bem-vindo(a), " + clienteAtual.getNome() + "!");
            }
        }

        // Arrays para itens do carrinho
        int[]    codigosCarrinho    = new int[100];
        int[]    quantCarrinho      = new int[100];
        double[] subtotaisCarrinho  = new double[100];
        String[] nomesCarrinho      = new String[100];
        int      totalItens         = 0;

        // Loop de adição de produtos
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- Adicionar produto ao carrinho ---");
            System.out.print("Código do produto (0 para finalizar): ");
            int codigo;
            try {
                codigo = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Código inválido.");
                continue;
            }

            if (codigo == 0) break;

            Produto produto = MenuProduto.encontrarPorCodigo(codigo);
            if (produto == null) {
                System.out.println("[ERRO] Produto com código " + codigo + " não encontrado.");
                continue;
            }

            System.out.println("Produto: " + produto.getNome() +
                    " | Preço: R$ " + String.format("%.2f", produto.getPreco()) +
                    " | Estoque: " + produto.getEstoque());

            System.out.print("Quantidade desejada: ");
            int quantidade;
            try {
                quantidade = Integer.parseInt(sc.nextLine().trim());
                if (quantidade <= 0) {
                    System.out.println("[ERRO] Quantidade deve ser maior que zero.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Quantidade inválida.");
                continue;
            }

            // Verifica estoque considerando o que já foi adicionado no carrinho
            int jaNoCarrinho = 0;
            for (int i = 0; i < totalItens; i++) {
                if (codigosCarrinho[i] == codigo) {
                    jaNoCarrinho = quantCarrinho[i];
                    break;
                }
            }
            int disponivelReal = produto.getEstoque() - jaNoCarrinho;

            if (quantidade > disponivelReal) {
                System.out.println("[ERRO] Estoque insuficiente! Disponível: " + disponivelReal);
                continue;
            }

            // Verifica se produto já está no carrinho para somar
            boolean jaAdicionado = false;
            for (int i = 0; i < totalItens; i++) {
                if (codigosCarrinho[i] == codigo) {
                    quantCarrinho[i]     += quantidade;
                    subtotaisCarrinho[i]  = quantCarrinho[i] * produto.getPreco();
                    System.out.printf("Item atualizado: %dx %s%n", quantCarrinho[i], produto.getNome());
                    jaAdicionado = true;
                    break;
                }
            }

            if (!jaAdicionado) {
                codigosCarrinho[totalItens]   = codigo;
                nomesCarrinho[totalItens]     = produto.getNome();
                quantCarrinho[totalItens]     = quantidade;
                subtotaisCarrinho[totalItens] = quantidade * produto.getPreco();
                totalItens++;
                System.out.printf("Adicionado: %dx %s%n", quantidade, produto.getNome());
            }

            System.out.print("Adicionar mais produto? (S/N): ");
            String mais = sc.nextLine().trim().toUpperCase();
            if (!mais.equals("S")) continuar = false;
        }

        // Verifica se carrinho está vazio
        if (totalItens == 0) {
            System.out.println("Compra cancelada. Nenhum produto no carrinho.");
            pausar(sc);
            return;
        }

        //  Exibe cupom / recibo
        System.out.println("\n====================================");
        System.out.println("           CUPOM FISCAL");
        System.out.println("====================================");
        if (clienteAtual != null) {
            System.out.println("Cliente: " + clienteAtual.getNome() +
                    " | CPF: " + clienteAtual.getCpf());
            System.out.println("------------------------------------");
        }
        System.out.printf("%-20s %5s %10s %10s%n", "Produto", "Qtd", "Unit.", "Subtotal");
        System.out.println("------------------------------------");

        double totalFinal = 0;
        for (int i = 0; i < totalItens; i++) {
            Produto p = MenuProduto.encontrarPorCodigo(codigosCarrinho[i]);
            System.out.printf("%-20s %5d  R$%7.2f  R$%7.2f%n",
                    nomesCarrinho[i],
                    quantCarrinho[i],
                    p.getPreco(),
                    subtotaisCarrinho[i]);
            totalFinal += subtotaisCarrinho[i];
        }

        System.out.println("------------------------------------");
        System.out.printf("TOTAL:                         R$%7.2f%n", totalFinal);
        System.out.println("====================================");

        //Confirma e debita do estoque
        System.out.print("Confirmar compra? (S/N): ");
        String conf = sc.nextLine().trim().toUpperCase();
        if (!conf.equals("S")) {
            System.out.println("Compra cancelada pelo cliente.");
            pausar(sc);
            return;
        }

        // Debita estoque
        for (int i = 0; i < totalItens; i++) {
            Produto p = MenuProduto.encontrarPorCodigo(codigosCarrinho[i]);
            if (p != null) {
                p.setEstoque(p.getEstoque() - quantCarrinho[i]);
            }
        }
        MenuProduto.salvar();

        System.out.println("\nCompra realizada com sucesso! Obrigado pela preferência.");
        pausar(sc);
    }

    // Controle de Estoque

    public static void controleEstoque(Scanner sc) {
        int opcao;
        do {
            System.out.println("\n====================================");
            System.out.println("       CONTROLE DE ESTOQUE");
            System.out.println("====================================");
            System.out.println("1. Visualizar estoque atual");
            System.out.println("2. Produtos com estoque baixo (< 5)");
            System.out.println("3. Repor estoque de um produto");
            System.out.println("0. Voltar");
            System.out.println("====================================");
            System.out.print("Opção: ");

            opcao = lerInteiro(sc);

            switch (opcao) {
                case 1 -> MenuProduto.exibirListagem();
                case 2 -> listarEstoqueBaixo();
                case 3 -> reporEstoque(sc);
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("[AVISO] Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void listarEstoqueBaixo() {
        System.out.println("\n====================================");
        System.out.println("      PRODUTOS COM ESTOQUE BAIXO");
        System.out.println("====================================");
        System.out.println("+-------+---------------------------+------------+----------+");
        System.out.println("| Cód.  | Nome                      | Preço      | Estoque  |");
        System.out.println("+-------+---------------------------+------------+----------+");

        boolean encontrou = false;
        for (Produto p : MenuProduto.getProdutos()) {
            if (p.getEstoque() < 5) {
                System.out.println(p);
                encontrou = true;
            }
        }
        System.out.println("+-------+---------------------------+------------+----------+");

        if (!encontrou) {
            System.out.println("Nenhum produto com estoque crítico (< 5 unidades).");
        }
    }

    private static void reporEstoque(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("         REPOR ESTOQUE");
        System.out.println("====================================");
        System.out.print("Código do produto: ");
        int codigo = lerInteiro(sc);

        Produto p = MenuProduto.encontrarPorCodigo(codigo);
        if (p == null) {
            System.out.println("[ERRO] Produto não encontrado.");
            return;
        }

        System.out.println("Produto: " + p.getNome() + " | Estoque atual: " + p.getEstoque());
        System.out.print("Quantidade a adicionar: ");
        int qtd = lerInteiro(sc);

        if (qtd <= 0) {
            System.out.println("[ERRO] Quantidade inválida.");
            return;
        }

        p.setEstoque(p.getEstoque() + qtd);
        MenuProduto.salvar();
        System.out.println("Estoque atualizado! Novo estoque: " + p.getEstoque());
        pausar(sc);
    }

    // Utilitários

    private static int lerInteiro(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("[ERRO] Digite um número válido: ");
            }
        }
    }

    private static void pausar(Scanner sc) {
        System.out.print("Pressione ENTER para continuar...");
        sc.nextLine();
    }
}
