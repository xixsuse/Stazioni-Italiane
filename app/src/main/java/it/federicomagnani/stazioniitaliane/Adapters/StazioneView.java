package it.federicomagnani.stazioniitaliane.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.nlopez.smartadapters.views.BindableFrameLayout;
import it.federicomagnani.stazioniitaliane.Models.Stazione;
import it.federicomagnani.stazioniitaliane.R;


public class StazioneView extends BindableFrameLayout<Stazione> {

    TextView nome;
    ImageView fav;

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
        fav = (ImageView) getRootView().findViewById(R.id.img_item_stazione_fav);
    }

    @Override
    public void bind(Stazione s) {
        nome.setText(s.nome);

        if (s.preferita) {
            fav.setColorFilter(Color.rgb(196, 38, 38));
            fav.setImageResource(R.mipmap.star);
        } else {
            fav.setColorFilter(Color.rgb(150, 150, 150));
            fav.setImageResource(R.mipmap.staroutline);
        }

        fav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(2);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(1);
            }
        });

    }

}