import java.util.ArrayList;
import java.util.Scanner;

public class MenuProduto {

    private static final String ARQUIVO = "../dados/produtos.csv";
    private static ArrayList<Produto> produtos = new ArrayList<>();

    //  Carrega e retorna a lista (usada também pela compra)

    public static ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public static void carregar() {
        produtos = Arquivo.carregarProdutos(ARQUIVO);
    }

    public static void salvar() {
        Arquivo.salvarProdutos(ARQUIVO, produtos);
    }

    //  Menu principal de Produtos

    public static void exibir(Scanner sc) {
        int opcao;
        do {
            System.out.println("\n====================================");
            System.out.println("           MENU PRODUTOS");
            System.out.println("====================================");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Buscar Produto");
            System.out.println("4. Alterar Produto");
            System.out.println("5. Remover Produto");
            System.out.println("0. Voltar");
            System.out.println("====================================");
            System.out.print("Opção: ");

            opcao = lerInteiro(sc);

            switch (opcao) {
                case 1 -> cadastrar(sc);
                case 2 -> listar();
                case 3 -> buscar(sc);
                case 4 -> alterar(sc);
                case 5 -> remover(sc);
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("[AVISO] Opção inválida!");
            }
        } while (opcao != 0);
    }

    //  Cadastrar

    private static void cadastrar(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("        CADASTRAR PRODUTO");
        System.out.println("====================================");

        int codigo = gerarProximoCodigo();
        System.out.println("Código gerado automaticamente: " + codigo);

        System.out.print("Digite o Nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) { System.out.println("[ERRO] Nome não pode ser vazio."); return; }

        System.out.print("Digite o Preço (ex: 5.50): ");
        double preco;
        try {
            preco = Double.parseDouble(sc.nextLine().replace(",", ".").trim());
            if (preco < 0) { System.out.println("[ERRO] Preço não pode ser negativo."); return; }
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Preço inválido.");
            return;
        }

        System.out.print("Digite o Estoque inicial: ");
        int estoque;
        try {
            estoque = Integer.parseInt(sc.nextLine().trim());
            if (estoque < 0) { System.out.println("[ERRO] Estoque não pode ser negativo."); return; }
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Quantidade inválida.");
            return;
        }

        produtos.add(new Produto(codigo, nome, preco, estoque));
        salvar();
        System.out.println("Produto cadastrado com sucesso!");
        pausar(sc);
    }

    // Listar (público, chamado pelo controle de estoque)

    public static void exibirListagem() {
        listar();
    }

    // Listar

    private static void listar() {
        System.out.println("\n====================================");
        System.out.println("         LISTA DE PRODUTOS");
        System.out.println("====================================");

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("+-------+---------------------------+------------+----------+");
        System.out.println("| Cód.  | Nome                      | Preço      | Estoque  |");
        System.out.println("+-------+---------------------------+------------+----------+");
        for (Produto p : produtos) {
            System.out.println(p);
        }
        System.out.println("+-------+---------------------------+------------+----------+");
    }

    // Buscar

    private static void buscar(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("         BUSCAR PRODUTO");
        System.out.println("====================================");
        System.out.print("Digite o código ou nome do produto: ");
        String termo = sc.nextLine().trim().toLowerCase();

        boolean encontrou = false;
        System.out.println("+-------+---------------------------+------------+----------+");
        System.out.println("| Cód.  | Nome                      | Preço      | Estoque  |");
        System.out.println("+-------+---------------------------+------------+----------+");
        for (Produto p : produtos) {
            if (String.valueOf(p.getCodigo()).equals(termo) ||
                    p.getNome().toLowerCase().contains(termo)) {
                System.out.println(p);
                encontrou = true;
            }
        }
        System.out.println("+-------+---------------------------+------------+----------+");

        if (!encontrou) {
            System.out.println("Nenhum produto encontrado para: \"" + termo + "\"");
        }
        pausar(sc);
    }

    // Alterar

    private static void alterar(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("         ALTERAR PRODUTO");
        System.out.println("====================================");
        System.out.print("Digite o código do produto: ");
        int codigo = lerInteiro(sc);

        Produto encontrado = encontrarPorCodigo(codigo);
        if (encontrado == null) {
            System.out.println("[ERRO] Produto com código " + codigo + " não encontrado.");
            return;
        }

        System.out.println("Produto atual: " + encontrado.getNome() +
                " | R$ " + String.format("%.2f", encontrado.getPreco()) +
                " | Estoque: " + encontrado.getEstoque());

        System.out.print("Novo nome (ENTER para manter): ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) encontrado.setNome(nome);

        System.out.print("Novo preço (ENTER para manter): ");
        String precoStr = sc.nextLine().replace(",", ".").trim();
        if (!precoStr.isEmpty()) {
            try {
                double novoPreco = Double.parseDouble(precoStr);
                if (novoPreco >= 0) encontrado.setPreco(novoPreco);
                else System.out.println("[AVISO] Preço inválido, mantendo o anterior.");
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Preço inválido, mantendo o anterior.");
            }
        }

        System.out.print("Novo estoque (ENTER para manter): ");
        String estoqueStr = sc.nextLine().trim();
        if (!estoqueStr.isEmpty()) {
            try {
                int novoEstoque = Integer.parseInt(estoqueStr);
                if (novoEstoque >= 0) encontrado.setEstoque(novoEstoque);
                else System.out.println("[AVISO] Estoque inválido, mantendo o anterior.");
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Estoque inválido, mantendo o anterior.");
            }
        }

        salvar();
        System.out.println("Produto alterado com sucesso!");
        pausar(sc);
    }

    // Remover

    private static void remover(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("         REMOVER PRODUTO");
        System.out.println("====================================");
        System.out.print("Digite o código do produto: ");
        int codigo = lerInteiro(sc);

        Produto encontrado = encontrarPorCodigo(codigo);
        if (encontrado == null) {
            System.out.println("[ERRO] Produto com código " + codigo + " não encontrado.");
            return;
        }

        System.out.print("Confirmar remoção de \"" + encontrado.getNome() + "\"? (S/N): ");
        String confirm = sc.nextLine().trim().toUpperCase();
        if (confirm.equals("S")) {
            produtos.remove(encontrado);
            salvar();
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
        pausar(sc);
    }

    // Utilitários

    public static Produto encontrarPorCodigo(int codigo) {
        for (Produto p : produtos) {
            if (p.getCodigo() == codigo) return p;
        }
        return null;
    }

    private static int gerarProximoCodigo() {
        int max = 0;
        for (Produto p : produtos) {
            if (p.getCodigo() > max) max = p.getCodigo();
        }
        return max + 1;
    }

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
