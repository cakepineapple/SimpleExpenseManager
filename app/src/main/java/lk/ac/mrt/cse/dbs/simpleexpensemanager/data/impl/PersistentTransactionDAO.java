package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private final SQLiteDatabase DB;

    public PersistentTransactionDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.DB = dbHelper.getWritableDatabase();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        ContentValues contentValues = new ContentValues();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


        contentValues.put("date", df.format(transaction.getDate()));
        contentValues.put("account_number", transaction.getAccountNo());
        contentValues.put("expense_type", transaction.getExpenseType().toString());
        contentValues.put("amount", transaction.getAmount());

        DB.insert("Transactions", null, contentValues);
    }


    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor cursor = DB.rawQuery("SELECT * FROM Transactions", null);
        List<Transaction> transactions = new ArrayList<>();
        if (cursor.getCount() == 0) return transactions;
        cursor.moveToNext();
        do {
            String dateString = cursor.getString(0);
            Date date;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(dateString);
            } catch (ParseException e) {
                continue;
            }
            String accountNo = cursor.getString(1);
            ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(2));
            double balance = cursor.getDouble(3);
            Transaction transaction = new Transaction(date, accountNo, expenseType, balance);
            transactions.add(transaction);
        } while (cursor.moveToNext());
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();
        if (limit >= transactions.size()) {
            return transactions;
        }
        return transactions.subList(transactions.size() - limit, transactions.size());
    }
}
