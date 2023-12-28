package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.ClientDao;
import model.entities.Client;

import javax.xml.transform.Result;
import java.sql.*;

public class ClientDaoJDBC implements ClientDao {
    private Connection conn;

    public ClientDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Client obj) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT IGNORE INTO usuarios "
                            + "(username, password) "
                            + "VALUES "
                            + "(?,?)"
                    , Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, obj.getUsername());
            st.setString(2, obj.getPassword());


            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected :(");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Client authenticate(String username, String password) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM usuarios WHERE username = ? AND password = ?");
            st.setString(1, username);
            st.setString(2, password);
            rs = st.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                double money = rs.getDouble("money");

                return new Client(dbUsername, dbPassword, id, money);
            } else {
                return null;
            }
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public void autoDeposit(Client obj) throws SQLException {
        PreparedStatement st = null;
        try {
            if (obj.getId() == 0) {
                throw new DbException("Usuário não autenticado. Não é possível realizar o depósito.");
            }

            st = conn.prepareStatement(
                    "UPDATE usuarios "
                            + "SET money = ? "
                            + "WHERE id = ?"
            );
            st.setDouble(1, obj.getMoney());
            st.setInt(2, obj.getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Nenhum registro foi atualizado. Verifique se o ID do usuário é válido.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Client getById(int id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                double money = rs.getDouble("money");

                return new Client(username, password, id, money);
            } else {
                return null;
            }
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }


}
