package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    // TODO: retrieve all accounts from the account table
    // Returns list of account objs creased in DB
    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sqlString = "SELECT * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(new Account(
                    resultSet.getInt("account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
                ));
            }

            return accounts;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    // TODO: retrievbe an account from the account table by account_id
    // Returns account obj if one exist, otherwise returns null
    public Account getAccountByID(int user_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sqlString = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(
                    resultSet.getInt("account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
                );
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // TODO: retrievbe an account from the account table by username
    // Returns account obj if one exist, otherwise returns null
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sqlString = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(
                    resultSet.getInt("account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
                );
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    // Adds new account to the account table. Returns the added account id
    // if added successfully, otherwise returns 0
    public int addAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sqlString = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            
            return resultSet.getInt("account_id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }
    
}
