import java.io.*;
import java.util.ArrayList;

public class Arquivo {

    // PRODUTOS

    public static ArrayList<Produto> carregarProdutos(String caminho) {
        ArrayList<Produto> lista = new ArrayList<>();
        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            return lista; // retorna lista vazia se o arquivo ainda não existe
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { // pula cabeçalho
                    primeiraLinha = false;
                    continue;
                }
                if (!linha.isBlank()) {
                    lista.add(Produto.fromCSV(linha));
                }
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao carregar produtos: " + e.getMessage());
        }

        return lista;
    }

    public static void salvarProdutos(String caminho, ArrayList<Produto> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(caminho, false))) {
            pw.println("codigo;nome;preco;estoque");
            for (Produto p : lista) {
                pw.println(p.toCSV());
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao salvar produtos: " + e.getMessage());
        }
    }

    // CLIENTES

    public static ArrayList<Cliente> carregarClientes(String caminho) {
        ArrayList<Cliente> lista = new ArrayList<>();
        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                if (!linha.isBlank()) {
                    lista.add(Cliente.fromCSV(linha));
                }
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao carregar clientes: " + e.getMessage());
        }

        return lista;
    }

    public static void salvarClientes(String caminho, ArrayList<Cliente> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(caminho, false))) {
            pw.println("cpf;nome;telefone;email");
            for (Cliente c : lista) {
                pw.println(c.toCSV());
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao salvar clientes: " + e.getMessage());
        }
    }
}
