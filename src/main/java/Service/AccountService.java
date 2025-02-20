package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Retrieve all accounts from DB
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    // Retrueve account by its id. Returns account obj if record with such account_id
    // exists, null otherwise
    public Account getAccountByID(int account_id) {
        return accountDAO.getAccountByID(account_id);
    }

    // Retrueve account by its username. Returns account obj if record with such account_id
    // exists, null otherwise
    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }

    // Add account to a DB
    public Account addAccount(Account enteredAccount) {
        Account account = getAccountByUsername(enteredAccount.getUsername());
        
        if (account == null) {
            int account_id = accountDAO.addAccount(enteredAccount);
            return accountDAO.getAccountByID(account_id);
        } else {
            return null;
        }
    }

    // Check if account data matches with DB records
    // Returns account obj from the DB containing account_id if passwords mathces,
    // otherwise returns null
    public Account checkPasswordMatch(Account enteredAccount) {
        Account account = getAccountByUsername(enteredAccount.getUsername());
        if (account != null && enteredAccount.getPassword().equals(account.getPassword())) {
            return account;
        } else {
            return null;
        }
    }

}
