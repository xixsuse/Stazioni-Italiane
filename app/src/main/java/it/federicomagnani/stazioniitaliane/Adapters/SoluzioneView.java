package it.federicomagnani.stazioniitaliane.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.views.BindableFrameLayout;
import it.federicomagnani.stazioniitaliane.Models.Soluzione;
import it.federicomagnani.stazioniitaliane.Models.Stazione;
import it.federicomagnani.stazioniitaliane.Models.Veicolo;
import it.federicomagnani.stazioniitaliane.R;


public class SoluzioneView extends BindableFrameLayout<Soluzione> {

    TextView nome, cambi;
    ExpandableHeightListView list_veicoli;

    public SoluzioneView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_soluzione;
    }

    @Override
    public void onViewInflated() {
        //Assegno le views
        nome = (TextView) getRootView().findViewById(R.id.txt_item_soluzione_durata);
        cambi = (TextView) getRootView().findViewById(R.id.txt_item_soluzione_cambi);
        list_veicoli = (ExpandableHeightListView) getRootView().findViewById(R.id.list_item_soluzione_list);
    }

    @Override
    public void bind(Soluzione s) {
        nome.setText("Durata "+s.durata);
        cambi.setText((s.veicoli.size()-1)+((s.veicoli.size()-1) == 1 ? " cambio" : " cambi"));
        SmartAdapter.items(s.veicoli).map(Veicolo.class, VeicoloView.class).into(list_veicoli);
        list_veicoli.setExpanded(true);
    }

}