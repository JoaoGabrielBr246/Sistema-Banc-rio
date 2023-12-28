
package db;

// Classe que estende RuntimeException e representa uma exceção específica para integridade de banco de dados
public class DbIntigrityException extends RuntimeException {
    // Número de versão para fins de serialização (pode ser ignorado)
    private static final long serialVersionUID = 1L;

    // Construtor da classe que recebe uma mensagem de erro como parâmetro
    public DbIntigrityException(String msg) {
        // Chama o construtor da superclasse (RuntimeException) com a mensagem de erro
        super(msg);
    }
}
