package feup.cm.traintickets.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by mercurius on 06/04/17.
 */

public class SQLiteManager extends SQLiteOpenHelper {

    /**
     * Properties
     */
    public final static String DATABASE_NAME = "traintickets";
    public final static int DATABASE_VERSION = 4;

    InputStream sql;

    /**
     * Constructors
     */
    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        int i = context.getResources().getIdentifier("db", "raw", context.getPackageName());
        sql = context.getResources().openRawResource(i);
    }

    public SQLiteManager(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteManager(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Scanner scanner = new Scanner(sql);
            while (scanner.hasNext()) {
                String sql = scanner.nextLine().trim();
                sqLiteDatabase.execSQL(sql);
            }
        } catch (Exception e) {
            Log.d("SQLite", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("Tickets", "Upgrading database, which will destroy all old data");
        sqLiteDatabase.execSQL("drop table tickets");
        onCreate(sqLiteDatabase);
    }
}
