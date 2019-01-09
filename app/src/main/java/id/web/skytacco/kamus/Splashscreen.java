package id.web.skytacco.kamus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.skytacco.kamus.Database.KamusHelper;
import id.web.skytacco.kamus.Model.KamusModel;
import id.web.skytacco.kamus.Prefs.AppPreference;

public class Splashscreen extends AppCompatActivity {
    @BindView(R.id.progressBar)
    ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        ButterKnife.bind(this);

        new DatadiProses().execute();
    }

    public ArrayList<KamusModel> preLoadRaw(int selection) {
        ArrayList<KamusModel> list = new ArrayList<>();
        String line;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(selection);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                KamusModel mKamusModel;
                mKamusModel = new KamusModel(splitstr[0], splitstr[1]);
                list.add(mKamusModel);
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @SuppressLint("StaticFieldLeak")
    private class DatadiProses extends AsyncTask<Void, Integer, Void> {
        KamusHelper mKamusHelper;
        AppPreference mAppPref;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            mKamusHelper = new KamusHelper(Splashscreen.this);
            mAppPref = new AppPreference(Splashscreen.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = mAppPref.getFirstRun();
            if (firstRun) {
                ArrayList<KamusModel> dictEnglish = preLoadRaw(R.raw.english_indonesia);
                ArrayList<KamusModel> dictIndonesia = preLoadRaw(R.raw.indonesia_english);

                progress = 30;
                publishProgress((int) progress);

                mKamusHelper.open();

                Double progressMaxInsert = 100.0;
                int totalProgress = dictEnglish.size() + dictIndonesia.size();
                Double progressDiff = (progressMaxInsert - progress) / totalProgress;

                mKamusHelper.insertTransaction(dictEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                mKamusHelper.insertTransaction(dictIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                mKamusHelper.close();
                mAppPref.setFirstRun(false);

                publishProgress((int) maxprogress);
            } else {
                try {
                    synchronized (this) {
                        //this.wait(2000);

                        publishProgress(50);

                        //this.wait(1100);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                    Log.e("Tag", "Error :" + e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pgsBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(Splashscreen.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
