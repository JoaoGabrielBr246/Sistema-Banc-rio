package model.dao;

import model.entities.Client;

import java.sql.SQLException;

public interface ClientDao {
    void insert(Client obj) throws SQLException;

    Client authenticate(String username, String password) throws SQLException;

    void autoDeposit(Client obj) throws SQLException;

    public Client getById(int id) throws SQLException;
}
