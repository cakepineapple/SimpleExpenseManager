package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

import android.content.Context;

public class PersistentExpenseManager extends ExpenseManager {

    public PersistentExpenseManager(Context context) {
        setup(context);
    }

    @Override
    public void setup(Context context)  {
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(context);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);

        // If no account exists, creates a default account
        if (persistentAccountDAO.getAccountNumbersList().isEmpty()) {
            Account defaultAcc = new Account("Default", "Default Bank", "Default name", 0.0);
            persistentAccountDAO.addAccount(defaultAcc);
        }

    }
}
