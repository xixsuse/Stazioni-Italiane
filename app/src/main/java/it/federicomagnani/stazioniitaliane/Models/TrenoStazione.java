package it.federicomagnani.stazioniitaliane.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class TrenoStazione {

    public String destinazione, origine, orario_partenza, orario_arrivo, binario_arrivo, binario_partenza, identificativo, tipo_treno;
    public int ritardo;
    public boolean binario_confermato;

    public TrenoStazione(JSONObject o) {
        try {
            destinazione = o.getString("destinazione");
            origine = o.getString("origine");
            ritardo = o.getInt("ritardo");
            orario_partenza = o.getString("compOrarioPartenza");
            orario_arrivo = o.getString("compOrarioArrivo");
            identificativo = o.getString("compNumeroTreno");
            binario_partenza = o.getString("binarioEffettivoPartenzaDescrizione").equals("null") ? o.getString("binarioProgrammatoPartenzaDescrizione") : o.getString("binarioEffettivoPartenzaDescrizione");
            binario_arrivo = o.getString("binarioEffettivoArrivoDescrizione").equals("null") ? o.getString("binarioProgrammatoArrivoDescrizione") : o.getString("binarioEffettivoArrivoDescrizione");
            tipo_treno = o.getString("categoria");

            binario_confermato = !o.getString("binarioEffettivoArrivoDescrizione").equals("null") || !o.getString("binarioEffettivoPartenzaDescrizione").equals("null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPosto() {
        return destinazione.equals("null") ? origine : destinazione;
    }

    public String getOrario() {
        return orario_arrivo.equals("null") ? orario_partenza : orario_arrivo;
    }

    public String getBinario() {
        return binario_partenza.equals("null") ? binario_arrivo : binario_partenza;
    }

}