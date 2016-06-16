package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.MultiAdapter;
import it.federicomagnani.stazioniitaliane.Adapters.TrenoStazioneView;
import it.federicomagnani.stazioniitaliane.Models.TrenoStazione;

public class FragmentList extends Fragment {

    int section; //1 = Partenze. 2 = Arrivi.
    private ArrayList<TrenoStazione> treni = new ArrayList<>();
    private MultiAdapter adapter;
    private ListView listView;

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

        listView = (ListView) v.findViewById(R.id.list_treni);
        adapter = SmartAdapter.items(treni).map(TrenoStazione.class, TrenoStazioneView.class).into(listView);

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
        //String url = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/"+dove+"/"+FragmentStazioni.id_stazione+"/"+"Thu Jun 16 2016 00:07:54 GMT+0200 (CEST)";
        String url = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/"+dove+"/"+FragmentStazioni.id_stazione+"/"+dateFormat.format(date).replace(" ", "%20");
        Log.d("url stazione", url);
        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                if (json != null) {
                    try {
                        for (int i=0; i<json.length(); i++) {
                            treni.add(new TrenoStazione(json.getJSONObject(i)));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Errore server #1", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Errore server #2 "+status.getMessage()+" "+status.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
