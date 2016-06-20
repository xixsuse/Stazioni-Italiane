package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;

import io.nlopez.smartadapters.adapters.MultiAdapter;
import it.federicomagnani.stazioniitaliane.Models.RicercaSoluzione;

public class FragmentSoluzioni extends Fragment {

    private MultiAdapter adapter;
    private ListView listView;
    public static RicercaSoluzione soluzione = new RicercaSoluzione();

    public FragmentSoluzioni() {
        // Required empty public constructor
    }

    public static FragmentSoluzioni newInstance() {
        return new FragmentSoluzioni();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_soluzioni, container, false);

        AQuery aq = new AQuery(v);
        aq.id(R.id.txt_soluzioni_partenza).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.container_fragment, FragmentRicerca.newInstance(1), "fragment_corrente")
                        .commit();
            }
        });
        aq.id(R.id.txt_soluzioni_arrivo).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.container_fragment, FragmentRicerca.newInstance(2), "fragment_corrente")
                        .commit();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) getActivity()).getSupportActionBar().setTitle("Cerca viaggio");
        if (soluzione.partenza != null) {
            ((TextView) getActivity().findViewById(R.id.txt_soluzioni_partenza)).setText(soluzione.partenza.nome);
        }
        if (soluzione.arrivo != null) {
            ((TextView) getActivity().findViewById(R.id.txt_soluzioni_arrivo)).setText(soluzione.arrivo.nome);
        }
    }
}
