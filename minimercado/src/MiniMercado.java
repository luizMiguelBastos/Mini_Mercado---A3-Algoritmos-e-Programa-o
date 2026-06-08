import java.util.Scanner;

public class MiniMercado {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Carrega dados persistidos ao iniciar
        MenuProduto.carregar();
        MenuCliente.carregar();

        System.out.println("====================================");
        System.out.println("    BEM-VINDO AO MINI MERCADO");
        System.out.println("====================================");
        System.out.println("Dados carregados com sucesso!");

        int opcao;
        do {
            System.out.println("\n====================================");
            System.out.println("          MENU PRINCIPAL");
            System.out.println("====================================");
            System.out.println("1. Produtos");
            System.out.println("2. Clientes");
            System.out.println("3. Realizar Compra");
            System.out.println("4. Controle de Estoque");
            System.out.println("0. Sair");
            System.out.println("====================================");
            System.out.print("Opção: ");

            opcao = lerInteiro(sc);

            switch (opcao) {
                case 1 -> MenuProduto.exibir(sc);
                case 2 -> MenuCliente.exibir(sc);
                case 3 -> MenuCompra.realizar(sc);
                case 4 -> MenuCompra.controleEstoque(sc);
                case 0 -> System.out.println("\nSistema encerrado. Até logo!");
                default -> System.out.println("[AVISO] Opção inválida! Digite um número do menu.");
            }

        } while (opcao != 0);

        sc.close();
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
}
