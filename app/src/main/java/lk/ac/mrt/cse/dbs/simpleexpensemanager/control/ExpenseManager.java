/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * The ExpenseManager acts as the mediator when performing transactions. This is an abstract class with an abstract
 * method to setup the DAO objects depending on the implementation.
 */
public abstract class ExpenseManager implements Serializable {
    private AccountDAO accountsHolder;
    private TransactionDAO transactionsHolder;

    public List<String> getAccountNumbersList() {
        return accountsHolder.getAccountNumbersList();
    }

    public void updateAccountBalance(String accountNo, int day, int month, int year, ExpenseType expenseType,
                                     String amount) throws InvalidAccountException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date transactionDate = calendar.getTime();

        if (!amount.isEmpty()) {
            double amountVal = Double.parseDouble(amount);
            transactionsHolder.logTransaction(transactionDate, accountNo, expenseType, amountVal);
            accountsHolder.updateBalance(accountNo, expenseType, amountVal);
        }
    }

    public List<Transaction> getTransactionLogs() {
        return transactionsHolder.getPaginatedTransactionLogs(10);
    }

    public void addAccount(String accountNo, String bankName, String accountHolderName, double initialBalance) {
        Account account = new Account(accountNo, bankName, accountHolderName, initialBalance);
        accountsHolder.addAccount(account);
    }

    public AccountDAO getAccountsDAO() {
        return accountsHolder;
    }

    public void setAccountsDAO(AccountDAO accountDAO) {
        this.accountsHolder = accountDAO;
    }

    public TransactionDAO getTransactionsDAO() {
        return transactionsHolder;
    }

    public void setTransactionsDAO(TransactionDAO transactionDAO) {
        this.transactionsHolder = transactionDAO;
    }

    public abstract void setup(Context context) throws ExpenseManagerException;
}
