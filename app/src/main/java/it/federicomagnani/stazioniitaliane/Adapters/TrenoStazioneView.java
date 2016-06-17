package it.federicomagnani.stazioniitaliane.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import io.nlopez.smartadapters.views.BindableFrameLayout;
import it.federicomagnani.stazioniitaliane.Models.TrenoStazione;
import it.federicomagnani.stazioniitaliane.R;


public class TrenoStazioneView  extends BindableFrameLayout<TrenoStazione> {

    TextView destinazione, orario, binario, ritardo, identificativo;
    ImageView img_tipo;

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

        img_tipo = (ImageView) getRootView().findViewById(R.id.img_item_treno_stazione);
    }

    @Override
    public void bind(TrenoStazione trenoStazione) {
        destinazione.setText(trenoStazione.getPosto());
        orario.setText(trenoStazione.getOrario());

        if (trenoStazione.getBinario().equals("null")) {
            binario.setText("No binario");
        } else {
            binario.setText("Binario " + trenoStazione.getBinario());
        }

        if (trenoStazione.ritardo > 0) {
            ritardo.setVisibility(VISIBLE);
            ritardo.setText("+"+trenoStazione.ritardo+" minuti");
        } else {
            ritardo.setVisibility(GONE);
        }

        identificativo.setText(trenoStazione.identificativo);

        if (trenoStazione.binario_confermato) {
            binario.setTextColor(Color.rgb(3, 101, 192));
        } else {
            binario.setTextColor(Color.rgb(130, 130, 130));
        }

        if (trenoStazione.tipo_treno.equals("IC")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_intercity);
        } else if (trenoStazione.tipo_treno.equals("REG")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_regionale);
        } else { //Fallback su default
            img_tipo.setImageResource(R.mipmap.logo_treno_standard);
        }

    }

}