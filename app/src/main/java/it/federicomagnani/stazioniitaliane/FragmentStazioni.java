package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.MultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import it.federicomagnani.stazioniitaliane.Adapters.StazioneView;
import it.federicomagnani.stazioniitaliane.Models.Stazione;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FragmentStazioni extends Fragment {

    private Stazione stazione;
    private FragmentPagerItemAdapter adapter;
    private List<Stazione> stazioni =  new ArrayList<>();
    private MultiAdapter adapter_favoriti;

    public FragmentStazioni() {
        // Required empty public constructor
    }

    public static FragmentStazioni newInstance() {
        return new FragmentStazioni();
    }

    public static FragmentStazioni newInstance(Stazione s) {
        FragmentStazioni f = new FragmentStazioni();
        f.stazione = s;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stazioni, container, false);

        AQuery aq = new AQuery(v);

        if (stazione != null) {
            adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), FragmentPagerItems.with(getContext())
                    .add("Partenze", FragmentList.class, new Bundler().putInt("section", 1).putString("nome_stazione", stazione.nome).putString("id_stazione", stazione.id_stazione).get())
                    .add("Arrivi", FragmentList.class, new Bundler().putInt("section", 2).putString("nome_stazione", stazione.nome).putString("id_stazione", stazione.id_stazione).get())
                    .create());

            ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);
            viewPager.setVisibility(VISIBLE);

            SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpagertab);
            viewPagerTab.setViewPager(viewPager);
            viewPagerTab.setVisibility(VISIBLE);

            aq.id(R.id.card_stazioni_preferite).gone();
        } else {
            final ViewEventListener<Stazione> listener = new ViewEventListener<Stazione>() {
                @Override
                public void onViewEvent(int i, Stazione s, int i1, View view) {
                    if (i == 1) {
                        getActivity().findViewById(R.id.card_stazioni_preferite).setVisibility(GONE);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack("")
                                .replace(R.id.container_fragment, FragmentStazioni.newInstance(s), "fragment_corrente")
                                .commit();
                    } else if (i == 2) {
                        //Aggiungo ai preferiti
                        List<Stazione> staziones = Select.from(Stazione.class).where(Condition.prop("idStazione").eq(s.id_stazione)).list();
                        staziones.get(0).preferita = !staziones.get(0).preferita;
                        staziones.get(0).save();
                        stazioni.get(i1).preferita = staziones.get(0).preferita;
                        adapter_favoriti.notifyDataSetChanged();
                    }
                }
            };
            stazioni.clear();
            ExpandableHeightListView stazioni_listview = (ExpandableHeightListView) v.findViewById(R.id.list_stazioni_preferite);
            stazioni = Select.from(Stazione.class).where(Condition.prop("preferita").eq("1")).list();
            adapter_favoriti = SmartAdapter.items(stazioni).map(Stazione.class, StazioneView.class).listener(listener).into(stazioni_listview);
            stazioni_listview.setExpanded(true);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stazione != null) {
            ((ActivityMain) getActivity()).getSupportActionBar().setTitle(stazione.nome);
            getActivity().findViewById(R.id.card_stazioni_preferite).setVisibility(GONE);
        } else {
            getActivity().findViewById(R.id.card_stazioni_preferite).setVisibility(VISIBLE);
            ((ActivityMain) getActivity()).getSupportActionBar().setTitle("Stazioni italiane");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.card_stazioni_preferite).setVisibility(GONE);
    }
}
