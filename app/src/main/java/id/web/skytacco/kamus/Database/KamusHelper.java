package id.web.skytacco.kamus.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class KamusHelper {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static String ENGLISH = DatabaseContract.TABLE_EN_ID;
    private static String INDONESIA = DatabaseContract.TABLE_ID_EN;

    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

}
