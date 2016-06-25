package it.federicomagnani.stazioniitaliane.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import it.federicomagnani.stazioniitaliane.Utility;

public class Veicolo {

    public String stazione_partenza;
    public String stazione_arrivo;
    public String ora_partenza;
    public String ora_arrivo;
    public String prefisso_treno;
    public String categoria_treno;
    public String num_treno;
    public Tratta tratta;
    public String id_origine;

    public Veicolo(JSONObject o) {
        try {
            this.ora_partenza = o.getString("orarioPartenza").substring(11, 16);
            this.ora_arrivo = o.getString("orarioArrivo").substring(11, 16);
            this.stazione_partenza = Utility.ucWords(o.getString("origine"));
            this.stazione_arrivo = Utility.ucWords(o.getString("destinazione"));
            this.num_treno = o.getString("numeroTreno");
            this.prefisso_treno = o.getString("categoriaDescrizione");
            this.categoria_treno = (o.getString("categoriaDescrizione") + " "+ o.getString("numeroTreno")).trim();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
