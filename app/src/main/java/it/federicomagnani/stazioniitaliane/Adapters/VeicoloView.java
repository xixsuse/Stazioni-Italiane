package it.federicomagnani.stazioniitaliane.Adapters;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import io.nlopez.smartadapters.views.BindableFrameLayout;
import it.federicomagnani.stazioniitaliane.ActivityMain;
import it.federicomagnani.stazioniitaliane.FragmentTratta;
import it.federicomagnani.stazioniitaliane.Models.Tratta;
import it.federicomagnani.stazioniitaliane.Models.Treno;
import it.federicomagnani.stazioniitaliane.Models.Veicolo;
import it.federicomagnani.stazioniitaliane.R;


public class VeicoloView extends BindableFrameLayout<Veicolo> {

    TextView destinazione_finale, ora_partenza, ora_arrivo, stazione_partenza, stazione_arrivo, ritardo;
    ImageView img_tipo;
    CardView card_view;
    Context c;

    public VeicoloView(Context context) {
        super(context);
        c = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_veicolo;
    }

    @Override
    public void onViewInflated() {
        //Assegno le views
        destinazione_finale = (TextView) getRootView().findViewById(R.id.txt_item_veicolo_destinazione_finale);
        ora_partenza = (TextView) getRootView().findViewById(R.id.txt_item_veicolo_ora_partenza);
        ora_arrivo = (TextView) getRootView().findViewById(R.id.txt_item_veicolo_ora_arrivo);
        stazione_partenza = (TextView) getRootView().findViewById(R.id.txt_item_veicolo_stazione_partenza);
        stazione_arrivo = (TextView) getRootView().findViewById(R.id.txt_item_veicolo_stazione_arrivo);
        ritardo = (TextView) getRootView().findViewById(R.id.txt_item_veicolo_ritardo);

        card_view = (CardView) getRootView().findViewById(R.id.card_item_veicolo_ritardo);

        img_tipo = (ImageView) getRootView().findViewById(R.id.img_item_veicolo_tipo);
    }

    @Override
    public void bind(final Veicolo v) {
        destinazione_finale.setText(v.categoria_treno+" per "+v.stazione_arrivo);
        ora_partenza.setText(v.ora_partenza);
        ora_arrivo.setText(v.ora_arrivo);
        stazione_partenza.setText(v.stazione_partenza);
        stazione_arrivo.setText(v.stazione_arrivo);

        if (v.prefisso_treno.equals("REG")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_regionale);
        } else if (v.prefisso_treno.equals("IC")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_intercity);
        } else if  (v.prefisso_treno.equals("EN")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_euronight);
        } else if  (v.prefisso_treno.equals("EC")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_eurocity);
        } else if  (v.prefisso_treno.equals("Frecciarossa")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_frecciarossa);
        } else if  (v.prefisso_treno.equals("Frecciabianca")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_frecciabianca);
        }  else if  (v.prefisso_treno.equals("RV")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_regionale);
        }  else if  (v.num_treno.equals("Urb")) {
            img_tipo.setImageResource(R.mipmap.logo_treno_suburbano);
        } else { //Fallback su default
            img_tipo.setImageResource(R.mipmap.logo_treno_standard);
        }

        setOnClickListener(null);

        if (v.tratta == null) {
            //Trovo il ritardo e altri dettagli
            String url = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/cercaNumeroTreno/" + v.num_treno;
            final AQuery aq = new AQuery(c);
            aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null) {
                        String codice_treno;
                        try {
                            codice_treno = json.getString("numeroTreno");
                            v.id_origine = json.getString("codLocOrig");
                            setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentManager fragmentManager = ((ActivityMain) c).getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack("")
                                            .replace(R.id.container_fragment, FragmentTratta.newInstance(new Treno(v.id_origine, v.categoria_treno, Integer.parseInt(v.num_treno))), "fragment_corrente")
                                            .commit();
                                }
                            });
                            url = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/andamentoTreno/" + v.id_origine + "/" + codice_treno;
                            aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                                @Override
                                public void callback(String url, JSONObject json, AjaxStatus status) {
                                    if (json != null) {
                                        //Nuova tratta
                                        v.tratta = new Tratta(json);
                                        infoTratta(v);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        } else {
            infoTratta(v);
        }

        //Click su treno
        if (v.id_origine != null) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((ActivityMain) c).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack("")
                            .replace(R.id.container_fragment, FragmentTratta.newInstance(new Treno(v.id_origine, v.categoria_treno, Integer.parseInt(v.num_treno))), "fragment_corrente")
                            .commit();
                }
            });
        }

    }

    public void infoTratta(Veicolo v) {
        if (v.tratta.ritardo > 0) {
            ritardo.setText("+ " + v.tratta.ritardo + " minuti");
            card_view.setVisibility(VISIBLE);
        } else {
            card_view.setVisibility(GONE);
        }


    }

}

