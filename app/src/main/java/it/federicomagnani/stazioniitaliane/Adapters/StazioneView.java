package it.federicomagnani.stazioniitaliane.Adapters;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.nlopez.smartadapters.views.BindableFrameLayout;
import it.federicomagnani.stazioniitaliane.Models.Stazione;
import it.federicomagnani.stazioniitaliane.Models.TrenoStazione;
import it.federicomagnani.stazioniitaliane.R;


public class StazioneView extends BindableFrameLayout<Stazione> {

    TextView nome;

    public StazioneView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_stazione;
    }

    @Override
    public void onViewInflated() {
        //Assegno le views
        nome = (TextView) getRootView().findViewById(R.id.txt_item_stazione_nome);
    }

    @Override
    public void bind(Stazione s) {
        nome.setText(s.nome);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemAction(1);
            }
        });
    }

}