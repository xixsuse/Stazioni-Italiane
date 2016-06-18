package it.federicomagnani.stazioniitaliane.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.federicomagnani.stazioniitaliane.Utility;

import static it.federicomagnani.stazioniitaliane.Utility.ucWords;

public class Tratta {

    public String ultimo_rilevamento_stazione;
    public String ultimo_rilevamento_orario;
    public Stazione origine;
    public Stazione destinazione;
    public String origine_orario;
    public String destinazione_orario;
    public int ritardo;
    public String durata;
    public ArrayList<FermataTreno> fermate;
    public boolean is_soppresso = false;
    public String messaggio;

    public Tratta() {
        fermate = new ArrayList<>();
    }

    public Tratta(JSONObject o) {
        this();
        try {
            ritardo = o.getInt("ritardo");
            origine = new Stazione(o.getString("idOrigine"), Utility.ucWords(o.getString("origineZero")));
            destinazione = new Stazione(o.getString("idDestinazione"), Utility.ucWords(o.getString("destinazioneZero")));
            origine_orario = o.getString("compOrarioPartenza");
            destinazione_orario = o.getString("compOrarioArrivo");
            ultimo_rilevamento_stazione = ucWords(o.getString("stazioneUltimoRilevamento"));
            ultimo_rilevamento_orario = o.getString("compOraUltimoRilevamento");
            messaggio = o.getString("subTitle");

            if (!o.getJSONArray("descOrientamento").getString(0).equals("--")) {
                messaggio += o.getJSONArray("descOrientamento").getString(0);
            }

            is_soppresso = o.getString("tipoTreno").equals("ST") && o.getInt("provvedimento") == 1;

            //Memorizzo le varie fermate
            JSONArray json_fermate = o.getJSONArray("fermate");
            int indice_arrivo = -1;
            for (int i=0; i<json_fermate.length(); i++) {
                FermataTreno fermata_temp = new FermataTreno(json_fermate.getJSONObject(i));
                if (fermata_temp.treno_arrivato) {
                    indice_arrivo = i;
                }
                fermate.add(fermata_temp);
            }

            //Correggo i difetti di queste API di M***A
            if (fermate.size() > 0) {
                fermate.get(0).is_origine = true;
                fermate.get(fermate.size()-1).is_termine = true;
                for (int i = 0; i < fermate.size(); i++) {
                    if (i <= indice_arrivo) {
                        if (i<indice_arrivo) {
                            fermate.get(i).treno_partito = true;
                        }
                        fermate.get(i).treno_arrivato = true;
                        fermate.get(i).data_stimata = "";
                    } else {
                        if (ritardo < 0) {
                            ritardo = 0;
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(fermate.get(i).milliseconds_data_arrivo+(60000*ritardo));

                        fermate.get(i).data_stimata = Utility.formaData(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
