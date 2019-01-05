package id.web.skytacco.kamus.Database;

import android.provider.BaseColumns;

class DatabaseContract {
    static String TABLE_EN_ID = "eng_ke_indo";
    static String TABLE_ID_EN = "indo_ke_eng";

    static final class KamusColumns implements BaseColumns {
        static String WORD = "katanya";
        static String DESCRIBE = "translate_dari_kata_tersebut";
    }
}
