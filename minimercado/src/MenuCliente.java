import java.util.ArrayList;
import java.util.Scanner;

public class MenuCliente {

    private static final String ARQUIVO = "../dados/clientes.csv";
    private static ArrayList<Cliente> clientes = new ArrayList<>();

    // Carrega e retorna a lista (usada também pela compra)

    public static ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public static void carregar() {
        clientes = Arquivo.carregarClientes(ARQUIVO);
    }

    public static void salvar() {
        Arquivo.salvarClientes(ARQUIVO, clientes);
    }

    // Menu principal de Clientes

    public static void exibir(Scanner sc) {
        int opcao;
        do {
            System.out.println("\n====================================");
            System.out.println("           MENU CLIENTES");
            System.out.println("====================================");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente");
            System.out.println("4. Alterar Cliente");
            System.out.println("5. Remover Cliente");
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

    // Cadastrar

    private static void cadastrar(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("        CADASTRAR CLIENTE");
        System.out.println("====================================");

        System.out.print("Digite o CPF: ");
        String cpf = sc.nextLine().trim().replaceAll("[^0-9]", "");
        if (cpf.length() != 11) {
            System.out.println("[ERRO] CPF inválido. Informe 11 dígitos numéricos.");
            return;
        }
        if (encontrarPorCpf(cpf) != null) {
            System.out.println("[ERRO] CPF já cadastrado.");
            return;
        }

        System.out.print("Digite o Nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) { System.out.println("[ERRO] Nome não pode ser vazio."); return; }

        System.out.print("Digite o Telefone: ");
        String telefone = sc.nextLine().trim();

        System.out.print("Digite o Email: ");
        String email = sc.nextLine().trim();
        if (!email.contains("@")) {
            System.out.println("[AVISO] Email parece inválido, cadastrando assim mesmo...");
        }

        clientes.add(new Cliente(cpf, nome, telefone, email));
        salvar();
        System.out.println("Cliente cadastrado com sucesso!");
        pausar(sc);
    }

    // Listar

    private static void listar() {
        System.out.println("\n====================================");
        System.out.println("         LISTA DE CLIENTES");
        System.out.println("====================================");

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        System.out.println("+----------------+---------------------------+-----------------+---------------------------+");
        System.out.println("| CPF            | Nome                      | Telefone        | Email                     |");
        System.out.println("+----------------+---------------------------+-----------------+---------------------------+");
        for (Cliente c : clientes) {
            System.out.println(c);
        }
        System.out.println("+----------------+---------------------------+-----------------+---------------------------+");
    }

    // Buscar

    private static void buscar(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("         BUSCAR CLIENTE");
        System.out.println("====================================");
        System.out.print("Digite o CPF ou nome do cliente: ");
        String termo = sc.nextLine().trim().toLowerCase();

        boolean encontrou = false;
        System.out.println("+----------------+---------------------------+-----------------+---------------------------+");
        System.out.println("| CPF            | Nome                      | Telefone        | Email                     |");
        System.out.println("+----------------+---------------------------+-----------------+---------------------------+");
        for (Cliente c : clientes) {
            if (c.getCpf().contains(termo) || c.getNome().toLowerCase().contains(termo)) {
                System.out.println(c);
                encontrou = true;
            }
        }
        System.out.println("+----------------+---------------------------+-----------------+---------------------------+");

        if (!encontrou) {
            System.out.println("Nenhum cliente encontrado para: \"" + termo + "\"");
        }
        pausar(sc);
    }

    // Alterar

    private static void alterar(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("         ALTERAR CLIENTE");
        System.out.println("====================================");
        System.out.print("Digite o CPF do cliente: ");
        String cpf = sc.nextLine().trim().replaceAll("[^0-9]", "");

        Cliente encontrado = encontrarPorCpf(cpf);
        if (encontrado == null) {
            System.out.println("[ERRO] Cliente com CPF " + cpf + " não encontrado.");
            return;
        }

        System.out.println("Cliente atual: " + encontrado.getNome() +
                " | " + encontrado.getTelefone() + " | " + encontrado.getEmail());

        System.out.print("Novo nome (ENTER para manter): ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) encontrado.setNome(nome);

        System.out.print("Novo telefone (ENTER para manter): ");
        String telefone = sc.nextLine().trim();
        if (!telefone.isEmpty()) encontrado.setTelefone(telefone);

        System.out.print("Novo email (ENTER para manter): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) encontrado.setEmail(email);

        salvar();
        System.out.println("Cliente alterado com sucesso!");
        pausar(sc);
    }

    // Remover

    private static void remover(Scanner sc) {
        System.out.println("\n====================================");
        System.out.println("         REMOVER CLIENTE");
        System.out.println("====================================");
        System.out.print("Digite o CPF do cliente: ");
        String cpf = sc.nextLine().trim().replaceAll("[^0-9]", "");

        Cliente encontrado = encontrarPorCpf(cpf);
        if (encontrado == null) {
            System.out.println("[ERRO] Cliente com CPF " + cpf + " não encontrado.");
            return;
        }

        System.out.print("Confirmar remoção de \"" + encontrado.getNome() + "\"? (S/N): ");
        String confirm = sc.nextLine().trim().toUpperCase();
        if (confirm.equals("S")) {
            clientes.remove(encontrado);
            salvar();
            System.out.println("Cliente removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
        pausar(sc);
    }

    // Utilitários

    public static Cliente encontrarPorCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        for (Cliente c : clientes) {
            if (c.getCpf().equals(cpfLimpo)) return c;
        }
        return null;
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
