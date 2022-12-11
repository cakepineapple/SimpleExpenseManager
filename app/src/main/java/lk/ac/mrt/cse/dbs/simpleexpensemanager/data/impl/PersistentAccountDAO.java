package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private final SQLiteDatabase DB;

    public PersistentAccountDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.DB = dbHelper.getWritableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor = DB.rawQuery("SELECT account_number FROM Accounts", null);
        List<String> accountNos = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return accountNos;
        }
        while (cursor.moveToNext()) {
            accountNos.add(cursor.getString(0));
        }
        cursor.close();
        return accountNos;
    }

    @Override
    public List<Account> getAccountsList() {
        String accountNumber, bankName, accountHolderName;
        double balance;
        Account account;
        Cursor cursor = DB.rawQuery("SELECT * FROM Accounts", null);
        List<Account> accounts = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return accounts;
        }
        while (cursor.moveToNext()) {
            accountNumber = cursor.getString(0);
            bankName = cursor.getString(1);
            accountHolderName = cursor.getString(2);
            balance = cursor.getDouble(3);
            account = new Account(accountNumber, bankName, accountHolderName, balance);
            accounts.add(account);
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String accountNumber, bankName, accountHolderName;
        double balance;
        Cursor cursor = DB.rawQuery("SELECT * FROM Accounts WHERE account_number=?", new String[] {accountNo});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            accountNumber = cursor.getString(0);
            bankName = cursor.getString(1);
            accountHolderName = cursor.getString(2);
            balance = cursor.getDouble(3);
            cursor.close();
            return new Account(accountNumber, bankName, accountHolderName, balance);
        }
        cursor.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_number", account.getAccountNo());
        contentValues.put("bank_name", account.getBankName());
        contentValues.put("account_holder_name", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());

        this.DB.insert("Accounts", null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = DB.rawQuery("SELECT * FROM Accounts WHERE account_number=?", new String[] {accountNo});
        if (cursor.getCount() > 0) {
            DB.delete("Accounts", "account_number=?", new String[] {accountNo});
            cursor.close();
            return;
        }
        cursor.close();
        String msg = "Account " + accountNo + " does not exist.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_number", account.getAccountNo());
        contentValues.put("bank_name", account.getBankName());
        contentValues.put("account_holder_name", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());

        DB.update("Accounts", contentValues, "account_number=?", new String[]{accountNo});
    }
}
