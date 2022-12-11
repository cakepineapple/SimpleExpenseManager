package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " +
            "Accounts(" +
            "account_number TEXT PRIMARY KEY," +
            "bank_name TEXT," +
            "account_holder_name TEXT," +
            "balance REAL)";

    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " +
            "Transactions(" +
            "date TEXT," +
            "account_number TEXT," +
            "expense_type TEXT," +
            "amount REAL," +
            "FOREIGN KEY (account_number) " +
            "REFERENCES Accounts(account_number))";

    public DBHelper(Context context) {
        super(context, "200525R.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(CREATE_ACCOUNTS_TABLE);
        DB.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS Accounts");
        DB.execSQL("DROP TABLE IF EXISTS Transactions");
    }
}
