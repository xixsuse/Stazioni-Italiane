package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import it.federicomagnani.stazioniitaliane.Models.Stazione;
import it.federicomagnani.stazioniitaliane.Models.Tratta;
import it.federicomagnani.stazioniitaliane.Models.Treno;

public class FragmentTratta extends Fragment {

    private Treno treno;
    private Tratta tratta;

    public FragmentTratta() {
        // Required empty public constructor
    }

    public static FragmentTratta newInstance(Treno t) {
        FragmentTratta f = new FragmentTratta();
        f.treno = t;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_tratta, container, false);

        AQuery aq = new AQuery(v);
        String url = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/andamentoTreno/"+treno.codice_origine+"/"+treno.numero_treno;
        Log.d("url tratta", url);
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    tratta = new Tratta(json);
                    AQuery aq = new AQuery(getActivity());
                    aq.id(R.id.txt_tratta_origine).text(tratta.origine.nome);
                    aq.id(R.id.txt_tratta_destinazione).text(tratta.destinazione.nome);
                    if (tratta.ritardo > 0) {
                        aq.id(R.id.txt_tratta_ritardo).text("+ "+tratta.ritardo+" minuti");
                    } else {
                        aq.id(R.id.txt_tratta_ritardo).gone();
                    }
                    aq.id(R.id.txt_tratta_origine_orario).text(tratta.origine_orario);
                    aq.id(R.id.txt_tratta_destinazione_orario).text(tratta.destinazione_orario);
                    aq.id(R.id.txt_tratta_ultimorilevamento_orario).text(tratta.ultimo_rilevamento_orario);
                    aq.id(R.id.txt_tratta_ultimorilevamento_stazione).text(tratta.ultimo_rilevamento_stazione);
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
        ((ActivityMain) getActivity()).getSupportActionBar().setTitle(treno.codice_identificativo);
    }
}
