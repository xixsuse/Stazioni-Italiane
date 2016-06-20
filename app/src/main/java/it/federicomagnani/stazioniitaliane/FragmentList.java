package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.MultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import it.federicomagnani.stazioniitaliane.Adapters.TrenoStazioneView;
import it.federicomagnani.stazioniitaliane.Models.Stazione;
import it.federicomagnani.stazioniitaliane.Models.Treno;
import it.federicomagnani.stazioniitaliane.Models.TrenoInStazione;

public class FragmentList extends Fragment {

    int section; //1 = Partenze. 2 = Arrivi.
    private ArrayList<TrenoInStazione> treni = new ArrayList<>();
    private MultiAdapter adapter;
    private ListView listView;
    private Stazione stazione;
    private SwipeRefreshLayout swipy;

    public FragmentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_list, container, false);

        //Preparo i dati
        Bundle bundle = this.getArguments();
        section = bundle.getInt("section", 0);
        stazione = new Stazione();
        stazione.id_stazione = bundle.getString("id_stazione");
        stazione.nome = bundle.getString("nome_stazione");

        //Preparo il lato grafico
        final ViewEventListener<TrenoInStazione> listener = new ViewEventListener<TrenoInStazione>() {
            @Override
            public void onViewEvent(int i, TrenoInStazione s, int i1, View view) {
                if (i == 1) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack("")
                            .replace(R.id.container_fragment, FragmentTratta.newInstance(new Treno(s.cod_stazione_origine, s.identificativo, s.numero_treno)), "fragment_corrente")
                            .commit();
                }
            }
        };

        listView = (ListView) v.findViewById(R.id.list_treni);
        adapter = SmartAdapter.items(treni).map(TrenoInStazione.class, TrenoStazioneView.class).listener(listener).into(listView);

        swipy = (SwipeRefreshLayout) v.findViewById(R.id.swipe_list);
        swipy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aggiornaDati();
            }
        });
        swipy.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipy.post(new Runnable() {
            @Override public void run() {
                swipy.setRefreshing(true);
            }
        });

        aggiornaDati();

        return v;
    }

    private void aggiornaDati() {
        String dove = "";
        if (section == 1) {
            dove = "partenze";
        } else  {
            dove = "arrivi";
        }

        AQuery aq = new AQuery(getActivity());
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        String url = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/"+dove+"/"+stazione.id_stazione+"/"+dateFormat.format(date).replace(" ", "%20");
        Log.d("url stazione", url);
        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                if (json != null) {
                    try {
                        treni.clear();
                        for (int i=0; i<json.length(); i++) {
                            treni.add(new TrenoInStazione(json.getJSONObject(i)));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Errore server #1", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Errore server #2 "+status.getMessage()+" "+status.getError(), Toast.LENGTH_SHORT).show();
                }
                swipy.setRefreshing(false);
            }
        });
    }

}
