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
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.MultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import it.federicomagnani.stazioniitaliane.Adapters.SoluzioneView;
import it.federicomagnani.stazioniitaliane.Adapters.TrenoStazioneView;
import it.federicomagnani.stazioniitaliane.Models.RicercaSoluzione;
import it.federicomagnani.stazioniitaliane.Models.Soluzione;
import it.federicomagnani.stazioniitaliane.Models.Stazione;
import it.federicomagnani.stazioniitaliane.Models.Treno;
import it.federicomagnani.stazioniitaliane.Models.TrenoInStazione;

public class FragmentElencoSoluzioni extends Fragment {

    private RicercaSoluzione soluzione;
    private MultiAdapter adapter;
    private ListView listView;
    private ArrayList<Soluzione> soluzioni = new ArrayList<>();
    private SwipeRefreshLayout swipy;

    public FragmentElencoSoluzioni() {
        // Required empty public constructor
    }

    public static FragmentElencoSoluzioni newInstance(RicercaSoluzione soluzione) {
        FragmentElencoSoluzioni f = new FragmentElencoSoluzioni();
        f.soluzione = soluzione;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_elenco_soluzioni, container, false);

        listView = (ListView) v.findViewById(R.id.list_elenco_soluzioni);

        AQuery aq = new AQuery(v);
        String url = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/soluzioniViaggioNew/"+soluzione.partenza.id_stazione+"/"+soluzione.arrivo.id_stazione+"/"+soluzione.getData();
        Log.d("url soluzione", url);
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    soluzioni.clear();
                    try {
                        JSONArray soluzioni_json = json.getJSONArray("soluzioni");
                        for (int i=0; i<soluzioni_json.length(); i++) {
                            soluzioni.add(new Soluzione(soluzioni_json.getJSONObject(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter = SmartAdapter.items(soluzioni).map(Soluzione.class, SoluzioneView.class).into(listView);
                } else {
                    Toast.makeText(getContext(), "Errore server #2 "+status.getMessage()+" "+status.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) getActivity()).getSupportActionBar().setTitle("Elenco soluzioni");
    }
}
