package it.federicomagnani.stazioniitaliane.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static it.federicomagnani.stazioniitaliane.Utility.ucWords;

public class TrenoInStazione {

    public String destinazione, origine, orario_partenza, orario_arrivo, binario_arrivo, binario_partenza, identificativo, tipo_treno, cod_stazione_origine;
    public int ritardo, numero_treno;
    public boolean binario_confermato;
    public boolean riprogrammazione = false;
    public boolean soppresso = false;

    public TrenoInStazione(JSONObject o) {
        try {

            destinazione = o.getString("destinazione");
            origine = o.getString("origine");
            ritardo = o.getInt("ritardo");
            cod_stazione_origine = o.getString("codOrigine");
            numero_treno = o.getInt("numeroTreno");
            orario_partenza = o.getString("compOrarioPartenza");
            orario_arrivo = o.getString("compOrarioArrivo");
            identificativo = o.getString("compNumeroTreno");
            binario_partenza = o.getString("binarioEffettivoPartenzaDescrizione").equals("null") ? o.getString("binarioProgrammatoPartenzaDescrizione") : o.getString("binarioEffettivoPartenzaDescrizione");
            binario_arrivo = o.getString("binarioEffettivoArrivoDescrizione").equals("null") ? o.getString("binarioProgrammatoArrivoDescrizione") : o.getString("binarioEffettivoArrivoDescrizione");
            tipo_treno = o.getString("categoria");

            //DA AGGIUSTARE
            riprogrammazione = o.getString("riprogrammazione").equals("Y");
            //soppresso = o.getBoolean("nonPartito");

            binario_confermato = !o.getString("binarioEffettivoArrivoDescrizione").equals("null") || !o.getString("binarioEffettivoPartenzaDescrizione").equals("null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPosto() {
        return ucWords(destinazione.equals("null") ? origine : destinazione);
    }

    public String getOrario() {
        return orario_arrivo.equals("null") ? orario_partenza : orario_arrivo;
    }

    public String getBinario() {
        return ucWords(binario_partenza.equals("null") ? binario_arrivo : binario_partenza);
    }

}