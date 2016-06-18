package it.federicomagnani.stazioniitaliane.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.nlopez.smartadapters.views.BindableFrameLayout;
import it.federicomagnani.stazioniitaliane.Models.FermataTreno;
import it.federicomagnani.stazioniitaliane.Models.Stazione;
import it.federicomagnani.stazioniitaliane.R;


public class FermataTrenoView extends BindableFrameLayout<FermataTreno> {

    TextView nome_stazione;

    public FermataTrenoView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_fermata_treno;
    }

    @Override
    public void onViewInflated() {
        //Assegno le views
        nome_stazione = (TextView) getRootView().findViewById(R.id.txt_fermata_treno_stazione);
    }

    @Override
    public void bind(FermataTreno f) {
        nome_stazione.setText(f.stazione.nome);
    }

}