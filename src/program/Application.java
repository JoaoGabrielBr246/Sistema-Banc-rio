package program;

import db.DB;
import model.dao.ClientDao;
import model.dao.DaoFactory;
import model.entities.Client;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Locale.setDefault(Locale.US);

        Client cliente = null;
        ClientDao clientDao = DaoFactory.createClientDao();

        System.out.print("Você já tem uma conta? (S/N) ");
        String haveAccount = sc.nextLine();

        if (haveAccount.equalsIgnoreCase("N")) {
            try (Connection connection = DB.getConnection()) {
                System.out.print("Digite o seu nome: ");
                String username = sc.nextLine();
                System.out.print("Digite a sua nova senha: ");
                String password = sc.nextLine();
                System.out.print("Digite novamente a senha: ");
                String password2 = sc.nextLine();

                if (password.equals(password2)) {
                    cliente = new Client(username, password, 0, 0.0);
                    clientDao.insert(cliente);
                    System.out.println("Cliente inserido com sucesso. Seu ID: " + cliente.getId());
                } else {
                    System.out.println("As senhas não estão iguais!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (haveAccount.equalsIgnoreCase("S")) {
            try (Connection connection = DB.getConnection()) {
                System.out.print("Digite o nome de usuário: ");
                String username = sc.nextLine();

                System.out.print("Digite a senha: ");
                String password = sc.nextLine();
                cliente = clientDao.authenticate(username, password);

                if (cliente != null) {
                    System.out.println("Login bem-sucedido! Seu ID: " + cliente.getId());
                    int op;
                    boolean menu = true;
                    while (menu) {
                        System.out.println("O que deseja realizar?");
                        System.out.println("1-) Depositar em sua conta");
                        System.out.println("2-) Depositar em outra conta");
                        System.out.println("3-) Verificar saldo atual");
                        System.out.println("4-) Pagar conta");
                        System.out.println("0-) Sair");

                        System.out.print("Digite a opção: ");
                        op = sc.nextInt();
                        sc.nextLine();
                        switch (op) {
                            case 1:
                                System.out.print("Digite o valor a ser depositado: ");
                                double depositAmount = sc.nextDouble();
                                sc.nextLine();

                                cliente.deposit(depositAmount);

                                try {
                                    clientDao.autoDeposit(cliente);
                                    System.out.println("Depósito realizado com sucesso. Novo saldo: " + cliente.getMoney());
                                } catch (SQLException e) {
                                    System.out.println("Erro ao realizar o depósito: " + e.getMessage());
                                }
                                break;
                            case 2:
                                System.out.print("Digite o ID da conta de destino: ");
                                int contaDestinoId = sc.nextInt();
                                sc.nextLine();

                                System.out.print("Digite o valor a ser transferido: ");
                                double valorTransferencia = sc.nextDouble();
                                sc.nextLine();

                                try {
                                    Client contaDestino = clientDao.getById(contaDestinoId);
                                    if (contaDestino != null && cliente.transferir(contaDestino, valorTransferencia)) {
                                        clientDao.autoDeposit(cliente);
                                        clientDao.autoDeposit(contaDestino);
                                        System.out.println("Transferência realizada com sucesso. Novo saldo: " + cliente.getMoney());
                                    } else {
                                        System.out.println("Erro ao realizar a transferência. Verifique se as contas " +
                                                "são válidas e se há saldo suficiente.");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Erro ao realizar a transferência: " + e.getMessage());
                                }
                                break;
                            case 3:
                                double saldoAtual = cliente.currentBalance();
                                DecimalFormat formato = new DecimalFormat("0.00");
                                String saldoFormatado = formato.format(saldoAtual);
                                System.out.println("Seu saldo atual é: R$" + saldoFormatado);
                                break;
                            case 4:
                                System.out.print("Digite o valor da conta: ");
                                double payment = sc.nextDouble();
                                try {
                                    if (cliente.paymentBill(payment)) {
                                        clientDao.autoDeposit(cliente);
                                        System.out.println("Conta paga com sucesso!");
                                    } else {
                                        System.out.println("Saldo insuficiente para pagar a conta :(");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Erro ao pagar a conta " + e.getMessage());
                                }
                                break;

                            case 0:
                                System.out.println("Encerrando programa...");
                                menu = false;
                                break;
                        }
                    }
                } else {
                    System.out.println("Usuário ou senha incorretos.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Opção inválida");
        }

        sc.close();
    }
}
