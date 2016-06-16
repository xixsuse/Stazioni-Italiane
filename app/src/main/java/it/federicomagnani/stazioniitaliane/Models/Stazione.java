package it.federicomagnani.stazioniitaliane.Models;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

public class Stazione extends SugarRecord {

    public String id_stazione;
    public String nome;

    public Stazione() {
        id_stazione = "test";
        nome = "test";
    }

    public Stazione(JSONObject o) {
        try {
            id_stazione = o.getString("id");
            nome = o.getString("nome");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Stazione && ((Stazione) o).id_stazione.equals(this.id_stazione) && ((Stazione) o).nome.equals(this.nome);
    }

}
