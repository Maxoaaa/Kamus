package id.web.skytacco.kamus;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.skytacco.kamus.Adapter.KamusAdapter;
import id.web.skytacco.kamus.Database.KamusHelper;
import id.web.skytacco.kamus.Model.KamusModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.InpSearch)
    SearchView InpSearch;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;

    KamusHelper mKamusHelper;
    KamusAdapter adapter;
    ArrayList<KamusModel> mKamusModel = new ArrayList<>();
    //kalau true berarti user memilih bhs inggris ke indo
    private boolean chooseLang = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        //FragmentManager frm = getSupportFragmentManager();
        mKamusHelper = new KamusHelper(this);
        InpSearch.onActionViewExpanded();
        InpSearch.setOnQueryTextListener(this);
        showRecyclerList();
        DatadiProses();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void showRecyclerList() {
        adapter = new KamusAdapter();
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        rvCategory.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //Fragment fragment = null;
        //Bundle bundle = new Bundle();
        //String title;
        if (id == R.id.engtoin) {
            chooseLang = true;
            //fragment = null;
            DatadiProses();
        }
        if (id == R.id.intoeng) {
            chooseLang = false;
            //fragment = null;
            DatadiProses();
        }
        if (id == R.id.nav_share) {
            Intent si = new Intent(android.content.Intent.ACTION_SEND);
            si.setType("text/plain");
            si.putExtra(android.content.Intent.EXTRA_SUBJECT, "Handoyo Oficial");
            si.putExtra(android.content.Intent.EXTRA_TEXT, "Dapatkan Informasi tentang Aplikasi Lainnya. Kunjungi https://skytacco.web.id/ \n atau hubungi email REALTH99@GMAIL.COM");
            startActivity(Intent.createChooser(si, "Share via"));
        }
       /* if (id == R.id.nav_about) {
            title = "About Us";
            fragment = new AboutFragment();
            bundle.putString(AboutFragment.EXTRAS, title);
            fragment.setArguments(bundle);
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.id_content_main, fragment)
                    .addToBackStack(null)
                    .commit();
        }*/
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        DatadiProses(keyword);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String keyword) {
        DatadiProses(keyword);
        return false;
    }

    private void DatadiProses() {
        DatadiProses("");
    }

    private void DatadiProses(String keyword) {
        try {
            mKamusHelper.open();
            if (keyword.isEmpty()) {
                mKamusModel = mKamusHelper.getAllData(chooseLang);
            } else {
                mKamusModel = mKamusHelper.getDataByName(keyword, chooseLang);
            }
            String hintWord;
            if (chooseLang) {
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.ento_in));
                hintWord = getResources().getString(R.string.find_word2);
            } else {
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.into_en));
                hintWord = getResources().getString(R.string.find_word);
            }
            InpSearch.setQueryHint(hintWord);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mKamusHelper.close();
        }
        adapter.replaceAll(mKamusModel);
    }
}
