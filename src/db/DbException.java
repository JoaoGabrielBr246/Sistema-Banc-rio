package db;

// Classe de exceção personalizada para tratamento de erros relacionados ao banco de dados
public class DbException extends RuntimeException {
    // Número de versão para fins de serialização (pode ser ignorado)
    private static final long serialVersionUID = 1L;

    // Construtor da classe que recebe uma mensagem de erro como parâmetro
    public DbException(String msg) {
        // Chama o construtor da superclasse (RuntimeException) com a mensagem de erro
        super(msg);
    }
}
