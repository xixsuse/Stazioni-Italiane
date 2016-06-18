package it.federicomagnani.stazioniitaliane.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

            JSONArray json_fermate = o.getJSONArray("fermate");
            for (int i=0; i<json_fermate.length(); i++) {
                fermate.add(new FermataTreno(json_fermate.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
