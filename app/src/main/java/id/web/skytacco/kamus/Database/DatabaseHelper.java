package id.web.skytacco.kamus.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static id.web.skytacco.kamus.Database.DatabaseContract.KamusColumns.DESCRIBE;
import static id.web.skytacco.kamus.Database.DatabaseContract.KamusColumns.WORD;
import static id.web.skytacco.kamus.Database.DatabaseContract.TABLE_EN_ID;
import static id.web.skytacco.kamus.Database.DatabaseContract.TABLE_ID_EN;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "repositorykamus";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ID_TO_EN = "CREATE TABLE " + TABLE_ID_EN +
            " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WORD + " TEXT NOT NULL, " +
            DESCRIBE + " TEXT NOT NULL);";
    private static final String CREATE_TABLE_EN_TO_ID = "CREATE TABLE " + TABLE_EN_ID +
            " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WORD + " TEXT NOT NULL, " +
            DESCRIBE + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EN_ID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ID_EN);
        onCreate(db);
    }
}
