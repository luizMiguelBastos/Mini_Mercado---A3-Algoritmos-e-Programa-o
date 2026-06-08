public class Produto {
    private int codigo;
    private String nome;
    private double preco;
    private int estoque;

    public Produto(int codigo, String nome, double preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    // Getters
    public int getCodigo()    { return codigo; }
    public String getNome()   { return nome; }
    public double getPreco()  { return preco; }
    public int getEstoque()   { return estoque; }

    // Setters
    public void setNome(String nome)       { this.nome = nome; }
    public void setPreco(double preco)     { this.preco = preco; }
    public void setEstoque(int estoque)    { this.estoque = estoque; }

    // Converte o produto para linha CSV
    public String toCSV() {
        return codigo + ";" + nome + ";" + preco + ";" + estoque;
    }

    // Cria um Produto a partir de uma linha CSV
    public static Produto fromCSV(String linha) {
        String[] partes = linha.split(";");
        int codigo     = Integer.parseInt(partes[0].trim());
        String nome    = partes[1].trim();
        double preco   = Double.parseDouble(partes[2].trim());
        int estoque    = Integer.parseInt(partes[3].trim());
        return new Produto(codigo, nome, preco, estoque);
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-25s | R$ %-8.2f | %-8d |",
                codigo, nome, preco, estoque);
    }
}
