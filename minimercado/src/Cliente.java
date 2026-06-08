public class Cliente {
    private String cpf;
    private String nome;
    private String telefone;
    private String email;

    public Cliente(String cpf, String nome, String telefone, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters
    public String getCpf()       { return cpf; }
    public String getNome()      { return nome; }
    public String getTelefone()  { return telefone; }
    public String getEmail()     { return email; }

    // Setters
    public void setNome(String nome)          { this.nome = nome; }
    public void setTelefone(String telefone)  { this.telefone = telefone; }
    public void setEmail(String email)        { this.email = email; }

    // Converte o cliente para linha CSV
    public String toCSV() {
        return cpf + ";" + nome + ";" + telefone + ";" + email;
    }

    // Cria um Cliente a partir de uma linha CSV
    public static Cliente fromCSV(String linha) {
        String[] partes = linha.split(";");
        String cpf       = partes[0].trim();
        String nome      = partes[1].trim();
        String telefone  = partes[2].trim();
        String email     = partes[3].trim();
        return new Cliente(cpf, nome, telefone, email);
    }

    @Override
    public String toString() {
        return String.format("| %-14s | %-25s | %-15s | %-25s |",
                cpf, nome, telefone, email);
    }
}
