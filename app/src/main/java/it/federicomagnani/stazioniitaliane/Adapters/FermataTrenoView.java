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

    TextView nome_stazione, orario_arrivo, orario_partenza, ritardo_partenza, ritardo_arrivo, binario;
    ImageView simbolo_stazione, simbolo_binario;
    View segmento_precedente, segmento_successivo;
    Context context;

    public FermataTrenoView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_fermata_treno;
    }

    @Override
    public void onViewInflated() {
        //Assegno le views
        nome_stazione = (TextView) getRootView().findViewById(R.id.txt_fermata_treno_stazione);
        orario_arrivo = (TextView) getRootView().findViewById(R.id.txt_fermata_treno_orario_arrivo);
        orario_partenza = (TextView) getRootView().findViewById(R.id.txt_fermata_treno_orario_partenza);
        ritardo_arrivo = (TextView) getRootView().findViewById(R.id.txt_fermata_treno_ritardo_arrivo);
        ritardo_partenza = (TextView) getRootView().findViewById(R.id.txt_fermata_treno_ritardo_partenza);
        binario = (TextView) getRootView().findViewById(R.id.txt_fermata_treno_binario);

        simbolo_stazione = (ImageView) getRootView().findViewById(R.id.img_fermata_treno_simbolo);
        simbolo_binario = (ImageView) getRootView().findViewById(R.id.img_fermata_treno_binario);

        segmento_precedente = getRootView().findViewById(R.id.txt_fermata_treno_segmento_precedente);
        segmento_successivo = getRootView().findViewById(R.id.txt_fermata_treno_segmento_successivo);
    }

    @Override
    public void bind(FermataTreno f) {
        nome_stazione.setText(f.stazione.nome);
        orario_arrivo.setText(f.data_arrivo_programmata);
        orario_partenza.setText(f.data_partenza_programmata);

        if (f.ritardo_arrivo > 0) {
            ritardo_arrivo.setText("+ "+f.ritardo_arrivo);
            ritardo_arrivo.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (f.ritardo_arrivo < 0) {
            ritardo_arrivo.setText("- "+Math.abs(f.ritardo_arrivo));
            ritardo_arrivo.setTextColor(getResources().getColor(R.color.colorAccent));
        } else if (f.treno_arrivato && !f.is_origine) {
            ritardo_arrivo.setTextColor(getResources().getColor(R.color.colorAccent));
            ritardo_arrivo.setText("0");
        } else {
            ritardo_arrivo.setText("");
        }

        if (f.ritardo_partenza > 0) {
            ritardo_partenza.setText("+ "+f.ritardo_partenza);
            ritardo_partenza.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if(f.ritardo_partenza < 0) {
            ritardo_partenza.setText("- "+Math.abs(f.ritardo_partenza));
            ritardo_partenza.setTextColor(getResources().getColor(R.color.colorAccent));
        } else if (f.treno_partito) {
            ritardo_partenza.setTextColor(getResources().getColor(R.color.colorAccent));
            ritardo_partenza.setText("0");
        } else {
            ritardo_partenza.setText("");
        }

        if (f.treno_arrivato) {
            nome_stazione.setTextColor(getResources().getColor(R.color.colorAccent));
            simbolo_stazione.setColorFilter(getResources().getColor(R.color.colorAccent));
        } else {
            nome_stazione.setTextColor(Color.rgb(120, 120, 120));
            simbolo_stazione.setColorFilter(Color.rgb(150, 150, 150));
        }

        if (f.is_origine) {
            segmento_precedente.setVisibility(INVISIBLE);
        } else {
            segmento_precedente.setVisibility(VISIBLE);
            if (f.treno_arrivato) {
                segmento_precedente.setBackgroundColor(Color.rgb(140, 181, 97));
            } else {
                segmento_precedente.setBackgroundColor(Color.rgb(220, 220, 220));
            }
        }

        if (f.is_termine) {
            segmento_successivo.setVisibility(INVISIBLE);
        } else {
            segmento_successivo.setVisibility(VISIBLE);
            if (f.treno_partito) {
                segmento_successivo.setBackgroundColor(Color.rgb(140, 181, 97));
            } else {
                segmento_successivo.setBackgroundColor(Color.rgb(220, 220, 220));
            }
        }

        binario.setText(f.binario);
        if (f.binario_confermato || (f.treno_partito && !f.binario.equals("No binario"))) {
            binario.setTextColor(Color.rgb(3, 101, 192));
            simbolo_binario.setColorFilter(Color.rgb(3, 101, 192));
        } else {
            binario.setTextColor(Color.rgb(150, 150, 150));
            simbolo_binario.setColorFilter(Color.rgb(150, 150, 150));
        }

        if (f.is_fermata_soppressa) {
            nome_stazione.setTextColor(getResources().getColor(R.color.colorPrimary));
            simbolo_stazione.setColorFilter(getResources().getColor(R.color.colorPrimary));
            segmento_successivo.setBackgroundColor(Color.rgb(220, 220, 220));
            segmento_precedente.setBackgroundColor(Color.rgb(220, 220, 220));
            ritardo_arrivo.setText("");
            ritardo_partenza.setText("");
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemAction(1);
            }
        });
    }

}