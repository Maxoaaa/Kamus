package id.web.skytacco.kamus.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import id.web.skytacco.kamus.Model.KamusModel;

import static android.provider.BaseColumns._ID;
import static id.web.skytacco.kamus.Database.DatabaseContract.KamusColumns.DESCRIBE;
import static id.web.skytacco.kamus.Database.DatabaseContract.KamusColumns.WORD;

public class KamusHelper {
    private static String T_ENGLISH = DatabaseContract.TABLE_EN_ID;
    private static String T_INDONESIA = DatabaseContract.TABLE_ID_EN;
    private Context context;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase database;

    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper open() throws SQLException {
        mDatabaseHelper = new DatabaseHelper(context);
        database = mDatabaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDatabaseHelper.close();
    }

    private Cursor cursorDataByName(String keyword, boolean chooseLang) {
        String DATABASE_TABLE = chooseLang ? T_ENGLISH : T_INDONESIA;
        return database.rawQuery("SELECT * " +
                        "FROM " + DATABASE_TABLE +
                        " WHERE " + WORD +
                        " LIKE '%" + keyword.trim() + "%'",
                null);
    }

    public ArrayList<KamusModel> getDataByName(String keyword, boolean chooseLang) {
        KamusModel mKamusModel;
        ArrayList<KamusModel> list = new ArrayList<>();
        Cursor cursor = cursorDataByName(keyword, chooseLang);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                mKamusModel = new KamusModel();
                mKamusModel.setId((cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns._ID))));
                mKamusModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.WORD)));
                mKamusModel.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.DESCRIBE)));
                list.add(mKamusModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return list;
    }

    private Cursor cursorAllData(boolean chooseLang) {
        String NAMA_TABLE = chooseLang ? T_ENGLISH : T_INDONESIA;
        return database.rawQuery("SELECT * " +
                        "FROM " + NAMA_TABLE +
                        " ORDER BY " + _ID + " ASC ",
                null);
    }

    public ArrayList<KamusModel> getAllData(boolean chooseLang) {
        KamusModel mKamusModel;
        ArrayList<KamusModel> list = new ArrayList<>();
        Cursor cursor = cursorAllData(chooseLang);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                mKamusModel = new KamusModel();
                mKamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns._ID)));
                mKamusModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.WORD)));
                mKamusModel.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.DESCRIBE)));
                list.add(mKamusModel);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return list;
    }

    //Gunakan method ini untuk memulai sesi query transaction
    /*public void beginTransaction() { database.beginTransaction(); }*/
    //Gunakan method ini jika query transaction berhasil, jika error jangan panggil method ini
    /*public void setTransactionSuccess() { database.setTransactionSuccessful();}*/
    //Gunakan method ini untuk mengakhiri sesi query transaction
    /*public void endTransaction() { database.endTransaction(); }*/
    public void insertTransaction(ArrayList<KamusModel> mKamusModel, boolean chooseLang) {
        String TABLE_NAME = chooseLang ? T_ENGLISH : T_INDONESIA;
        String sql = "INSERT INTO " + TABLE_NAME +
                " (" +
                WORD + ", " +
                DESCRIBE +
                ") VALUES (?, ?)";
        database.beginTransaction();

        SQLiteStatement statement = database.compileStatement(sql);
        for (int i = 0; i < mKamusModel.size(); i++) {
            statement.bindString(1, mKamusModel.get(i).getWord());
            statement.bindString(2, mKamusModel.get(i).getDescription());
            statement.execute();
            statement.clearBindings();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public long insert(KamusModel mKamusModel, boolean chooseLang) {
        String DATABASE_TABLE = chooseLang ? T_ENGLISH : T_INDONESIA;
        ContentValues mCtValues = new ContentValues();
        mCtValues.put(DatabaseContract.KamusColumns.WORD, mKamusModel.getWord());
        mCtValues.put(DatabaseContract.KamusColumns.DESCRIBE, mKamusModel.getDescription());
        return database.insert(DATABASE_TABLE, null, mCtValues);
    }

    public void update(KamusModel mKamusModel, boolean chooseLang) {
        String DATABASE_TABLE = chooseLang ? T_ENGLISH : T_INDONESIA;
        ContentValues mCtValues = new ContentValues();
        mCtValues.put(WORD, mKamusModel.getWord());
        mCtValues.put(DESCRIBE, mKamusModel.getDescription());
        database.update(DATABASE_TABLE, mCtValues, _ID + "= '" + mKamusModel.getId() + "'", null);
    }

    public void delete(int id, boolean chooseLang) {
        String DATABASE_TABLE = chooseLang ? T_ENGLISH : T_INDONESIA;
        database.delete(DATABASE_TABLE, _ID + " = '" + id + "'", null);
    }
}
