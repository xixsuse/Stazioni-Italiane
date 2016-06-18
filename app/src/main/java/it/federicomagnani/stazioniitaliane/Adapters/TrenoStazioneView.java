package it.federicomagnani.stazioniitaliane.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.nlopez.smartadapters.views.BindableFrameLayout;
import it.federicomagnani.stazioniitaliane.Models.TrenoInStazione;
import it.federicomagnani.stazioniitaliane.R;


public class TrenoStazioneView  extends BindableFrameLayout<TrenoInStazione> {

    TextView destinazione, orario, binario, ritardo, identificativo;
    ImageView img_tipo, img_binario;

    public TrenoStazioneView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_treno_stazione;
    }

    @Override
    public void onViewInflated() {
        //Assegno le views
        destinazione = (TextView) getRootView().findViewById(R.id.txt_item_treno_stazione_destinazione);
        orario = (TextView) getRootView().findViewById(R.id.txt_item_treno_stazione_orario);
        binario = (TextView) getRootView().findViewById(R.id.txt_item_treno_stazione_binario);
        ritardo = (TextView) getRootView().findViewById(R.id.txt_item_treno_stazione_ritardo);
        identificativo = (TextView) getRootView().findViewById(R.id.txt_item_treno_stazione_identificativo);

        img_binario = (ImageView) getRootView().findViewById(R.id.img_treno_stazione_binario);
        img_tipo = (ImageView) getRootView().findViewById(R.id.img_item_treno_stazione);
    }

    @Override
    public void bind(TrenoInStazione trenoInStazione) {
        destinazione.setText(trenoInStazione.getPosto());
        orario.setText(trenoInStazione.getOrario());

        if (trenoInStazione.getBinario().equals("Null")) {
            binario.setText("No binario");
        } else {
            binario.setText("Binario " + trenoInStazione.getBinario());
        }

        if (trenoInStazione.ritardo > 0) {
            ritardo.setVisibility(VISIBLE);
            ritardo.setText("+"+ trenoInStazione.ritardo+" min");
        } else {
            ritardo.setVisibility(GONE);
        }

        identificativo.setText(trenoInStazione.identificativo);

        if (trenoInStazione.binario_confermato) {
            img_binario.setColorFilter(Color.rgb(3, 101, 192));
            binario.setTextColor(Color.rgb(3, 101, 192));
        } else {
            img_binario.setColorFilter(Color.rgb(130, 130, 130));
            binario.setTextColor(Color.rgb(130, 130, 130));
        }

        if (trenoInStazione.tipo_treno.equals("IC")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_intercity);
        } else if (trenoInStazione.tipo_treno.equals("REG")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_regionale);
        } else if  (trenoInStazione.tipo_treno.equals("ES*") && trenoInStazione.identificativo.contains("FR")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_frecciarossa);
        } else if  (trenoInStazione.tipo_treno.equals("ES*") && trenoInStazione.identificativo.contains("FB")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_frecciabianca);
        } else if  (trenoInStazione.tipo_treno.equals("ES*") && trenoInStazione.identificativo.contains("FA")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_frecciaargento);
        } else if  (trenoInStazione.tipo_treno.equals("EC")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_eurocity);
        } else if  (trenoInStazione.tipo_treno.equals("EN")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_euronight);
        } else { //Fallback su default
            img_tipo.setImageResource(R.mipmap.logo_treno_standard);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(1);
            }
        });

    }

}