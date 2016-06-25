package it.federicomagnani.stazioniitaliane.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Soluzione {

    public String durata;
    public ArrayList<Veicolo> veicoli = new ArrayList<>();

    public Soluzione() {
        durata = "00:00";
    }

    public Soluzione(JSONObject o) {
        this();
        try {
            this.durata = o.getString("durata");

            JSONArray arr_veicoli = o.getJSONArray("vehicles");
            for (int i=0; i<arr_veicoli.length(); i++) {
                veicoli.add(new Veicolo(arr_veicoli.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
