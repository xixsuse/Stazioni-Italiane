package it.federicomagnani.stazioniitaliane;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.MultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import it.federicomagnani.stazioniitaliane.Adapters.StazioneView;
import it.federicomagnani.stazioniitaliane.Models.Stazione;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialSearchView searchView;
    private MultiAdapter adapter_search;
    private List<Stazione> stazioni = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INIT toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        //INIT searchview
        final ViewEventListener<Stazione> listener = new ViewEventListener<Stazione>() {
            @Override
            public void onViewEvent(int i, Stazione s, int i1, View view) {
                if (i == 1) {
                    searchView.closeSearch();
                    getSupportActionBar().setTitle(s.nome);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, FragmentStazioni.newInstance(s), "fragment_corrente")
                            .addToBackStack("")
                            .commit();
                } else if (i == 2) {
                    //Aggiungo ai preferiti
                    List<Stazione> staziones = Select.from(Stazione.class).where(Condition.prop("idStazione").eq(s.id_stazione)).list();
                    staziones.get(0).preferita = !staziones.get(0).preferita;
                    staziones.get(0).save();
                    stazioni.get(i1).preferita = staziones.get(0).preferita;
                    adapter_search.notifyDataSetChanged();
                }
            }
        };
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.search_cursor);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                RelativeLayout search_layout = (RelativeLayout) findViewById(R.id.search_layout);
                search_layout.setVisibility(View.VISIBLE);
                Animation fadeIn = new AlphaAnimation(0,1);
                fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
                fadeIn.setDuration(100);
                search_layout.startAnimation(fadeIn);
            }
            @Override
            public void onSearchViewClosed() {
                RelativeLayout search_layout = (RelativeLayout) findViewById(R.id.search_layout);
                search_layout.setVisibility(View.GONE);
                Animation fadeIn = new AlphaAnimation(1,0);
                fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
                fadeIn.setDuration(100);
                search_layout.startAnimation(fadeIn);
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {
                  return false;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  stazioni.clear();
                  if (newText.length() > 0) {
                      stazioni = Stazione.find(Stazione.class, "nome LIKE ? ORDER BY preferita DESC, popolarita ASC", "%" + newText + "%");
                      if (stazioni.size() > 20) {
                          stazioni = stazioni.subList(0, 20);
                      }
                  }
                  if (stazioni.size() > 0) {
                      findViewById(R.id.txt_search_placeholder).setVisibility(View.GONE);
                  } else {
                      findViewById(R.id.txt_search_placeholder).setVisibility(View.VISIBLE);
                  }
                  ListView stazioni_listview = (ListView) findViewById(R.id.list_search_stazione);
                  adapter_search = SmartAdapter.items(stazioni).map(Stazione.class, StazioneView.class).listener(listener).into(stazioni_listview);
                  return false;
              }
        });

        //INIT drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Mando l'utente al primo fragment (Aggiornamento Stazioni)
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
            .replace(R.id.container_fragment, FragmentAggiornamento.newInstance(), "fragment_corrente")
            .commit();

    }

    @Override
    public void onBackPressed() {
        //Chiusura drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        //Chiusura Search
        if (searchView != null && searchView.isSearchOpen()) {
            searchView.closeSearch();
            return;
        }

        //Nessuno dei precedenti
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*
        if (id_stazione == R.id_stazione.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_stazione) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.container_fragment, FragmentStazioni.newInstance(), "fragment_corrente")
                    .commit();
        } else if (id == R.id.nav_viaggio) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.container_fragment, FragmentSoluzioni.newInstance(), "fragment_corrente")
                    .commit();
        }

        //Chiudo il drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
