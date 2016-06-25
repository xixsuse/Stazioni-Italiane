package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.MultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import it.federicomagnani.stazioniitaliane.Adapters.StazioneView;
import it.federicomagnani.stazioniitaliane.Models.Stazione;

import static android.view.View.VISIBLE;

public class FragmentRicerca extends Fragment {

    private MultiAdapter adapter;
    private ExpandableHeightListView listView;
    private List<Stazione> stazioni = new ArrayList<>();
    private int tipo;

    public FragmentRicerca() {
        // Required empty public constructor
    }

    public static FragmentRicerca newInstance(int tipologia) {
        FragmentRicerca f = new FragmentRicerca();
        f.tipo = tipologia;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_ricerca, container, false);

        final ViewEventListener<Stazione> listener = new ViewEventListener<Stazione>() {
            @Override
            public void onViewEvent(int i, Stazione s, int i1, View view) {
                if (i == 1) {
                    s.id_stazione = Integer.parseInt(s.id_stazione.replace("S0", ""))+"";
                    if (tipo == 1) {
                        FragmentSoluzioni.soluzione.partenza = s;
                    } else if (tipo == 2) {
                        FragmentSoluzioni.soluzione.arrivo = s;
                    }
                    getActivity().getSupportFragmentManager().popBackStack();
                } else if (i == 2) {
                    //Aggiungo ai preferiti
                    List<Stazione> staziones = Select.from(Stazione.class).where(Condition.prop("idStazione").eq(s.id_stazione)).list();
                    staziones.get(0).preferita = !staziones.get(0).preferita;
                    staziones.get(0).save();
                    stazioni.get(i1).preferita = staziones.get(0).preferita;
                    adapter.notifyDataSetChanged();
                }
            }
        };

        listView = (ExpandableHeightListView) v.findViewById(R.id.list_ricerca);
        stazioni = Stazione.find(Stazione.class, "preferita = 1 ORDER BY popolarita ASC");
        adapter = SmartAdapter.items(stazioni).map(Stazione.class, StazioneView.class).listener(listener).into(listView);
        ((EditText) v.findViewById(R.id.txt_ricerca)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    stazioni.clear();
                    stazioni = Stazione.find(Stazione.class, "nome LIKE ? ORDER BY preferita DESC, popolarita ASC", "%" + s + "%");
                    if (stazioni.size() > 20) {
                        stazioni = stazioni.subList(0, 20);
                    }
                } else {
                    stazioni = Stazione.find(Stazione.class, "preferita = 1 ORDER BY popolarita ASC");
                }
                adapter = SmartAdapter.items(stazioni).map(Stazione.class, StazioneView.class).listener(listener).into(listView);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) getActivity()).getSupportActionBar().setTitle("Cerca la stazione");
    }
}
