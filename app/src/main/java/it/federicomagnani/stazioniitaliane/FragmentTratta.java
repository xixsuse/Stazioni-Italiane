package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import it.federicomagnani.stazioniitaliane.Adapters.FermataTrenoView;
import it.federicomagnani.stazioniitaliane.Models.FermataTreno;
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
        if (treno == null) {
            return v;
        }

        final ViewEventListener<FermataTreno> listener = new ViewEventListener<FermataTreno>() {
            @Override
            public void onViewEvent(int i, FermataTreno f, int i1, View view) {
                if (i == 1) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack("")
                            .replace(R.id.container_fragment, FragmentStazioni.newInstance(f.stazione), "fragment_corrente")
                            .commit();
                }
            }
        };

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
                    if (tratta.ritardo > 1) {
                        aq.id(R.id.txt_tratta_ritardo).text("+ " + tratta.ritardo + " minuti");
                        aq.id(R.id.card_tratta_ritardo).visible();
                    } else if (tratta.ritardo < -1) {
                        CardView cardView = (CardView) getActivity().findViewById(R.id.card_tratta_ritardo);
                        cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
                        aq.id(R.id.txt_tratta_ritardo).text("- " + Math.abs(tratta.ritardo) + " minuti");
                        aq.id(R.id.card_tratta_ritardo).visible();
                    } else if (tratta.ritardo == 0) {
                        CardView cardView = (CardView) getActivity().findViewById(R.id.card_tratta_ritardo);
                        cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
                        aq.id(R.id.txt_tratta_ritardo).text("in orario");
                        aq.id(R.id.card_tratta_ritardo).visible();
                    } else {
                        aq.id(R.id.card_tratta_ritardo).gone();
                    }
                    aq.id(R.id.txt_tratta_origine_orario).text(tratta.origine_orario);
                    aq.id(R.id.txt_tratta_destinazione_orario).text(tratta.destinazione_orario);

                    aq.id(R.id.txt_tratta_ultimorilevamento).text(tratta.ultimo_rilevamento_orario+" a "+tratta.ultimo_rilevamento_stazione);
                    if (tratta.ultimo_rilevamento_orario.equals("--")) {
                        aq.id(R.id.card_tratta_ultimorilevamento).gone();
                    } else {
                        aq.id(R.id.card_tratta_ultimorilevamento).visible();
                    }

                    ListView stazioni_listview = (ListView) getActivity().findViewById(R.id.list_tratta);
                    SmartAdapter.items(tratta.fermate).map(FermataTreno.class, FermataTrenoView.class).listener(listener).into(stazioni_listview);

                    if (tratta.is_soppresso) {
                        aq.id(R.id.txt_tratta_ritardo).text("soppresso");
                    }
                    if (tratta.messaggio.length() > 0) {
                        aq.id(R.id.txt_tratta_messaggio).text(tratta.messaggio);
                        aq.id(R.id.card_tratta_messaggio).visible();
                    } else {
                        aq.id(R.id.card_tratta_messaggio).gone();
                    }
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
