package model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Client implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private Integer id;
    private Double money;

    public Client(String username, String password, Integer id, Double money) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Client() {

    }

    public void deposit(double amount) {
        this.money += amount;
    }

    public boolean transferir(Client contaDestino, double valorTransferencia) {
        if (this.money >= valorTransferencia && valorTransferencia > 0) {
            this.setMoney(this.money - valorTransferencia);
            contaDestino.setMoney(contaDestino.getMoney() + valorTransferencia);
            return true;
        } else {
            return false;
        }
    }

    public double currentBalance() {
        return this.money;
    }

    public boolean paymentBill(double value) {
        if (this.money >= value) {
            this.setMoney(this.money - value);
            return true;
        } else {
            return false;
        }
    }
}
