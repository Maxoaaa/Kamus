package id.web.skytacco.kamus.Prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import id.web.skytacco.kamus.R;

public class AppPreference {
    private SharedPreferences prefs;
    private Context context;

    public AppPreference(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public Boolean getFirstRun() {
        String key = context.getResources().getString(R.string.first_run);
        return prefs.getBoolean(key, true);
    }

    public void setFirstRun(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        String key = context.getResources().getString(R.string.first_run);
        editor.putBoolean(key, input);
        editor.apply();
    }
}
