
          MINI MERCADO - Sistema de Terminal

ESTRUTURA DO PROJETO:
  minimercado/
  ├── src/
  │   ├── MiniMercado.java     ← Ponto de entrada (main)
  │   ├── Produto.java         ← Classe Produto
  │   ├── Cliente.java         ← Classe Cliente
  │   ├── MenuProduto.java     ← CRUD de Produtos
  │   ├── MenuCliente.java     ← CRUD de Clientes
  │   ├── MenuCompra.java      ← Compras + Estoque
  │   └── Arquivo.java         ← Persistência CSV
  └── dados/
      ├── produtos.csv         ← Gerado automaticamente
      └── clientes.csv         ← Gerado automaticamente


  COMO COMPILAR E EXECUTAR (Terminal / CMD)


1. Abra o terminal na pasta "src/"
   cd caminho/para/minimercado/src

2. Compile todos os arquivos:
   javac -encoding UTF-8 *.java

3. Execute o sistema:
   java MiniMercado

OBS: A pasta "dados/" deve existir ou ser criada antes
     de executar. Os arquivos .csv são criados automaticamente.


  FUNCIONALIDADES


1. PRODUTOS
   - Cadastrar, listar, buscar, alterar, remover
   - Código gerado automaticamente (auto-incremento)

2. CLIENTES
   - Cadastrar, listar, buscar, alterar, remover
   - CPF validado (11 dígitos, único no sistema)

3. REALIZAR COMPRA
   - Identifica cliente por CPF (opcional)
   - Adiciona múltiplos produtos ao carrinho
   - Valida estoque antes de confirmar
   - Exibe cupom fiscal com total
   - Debita estoque automaticamente

4. CONTROLE DE ESTOQUE
   - Visualizar estoque atual
   - Alertar produtos com < 5 unidades
   - Repor estoque de qualquer produto

  PERSISTÊNCIA DE DADOS (CSV)

produtos.csv:
  codigo;nome;preco;estoque
  1;Coca-Cola;5.50;10

clientes.csv:
  cpf;nome;telefone;email
  12345678900;João;(11)99999-9999;joao@email.com

