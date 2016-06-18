package it.federicomagnani.stazioniitaliane.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import it.federicomagnani.stazioniitaliane.Utility;

public class FermataTreno {

    public Stazione stazione;
    public String data_arrivo_programmata;
    public long milliseconds_data_arrivo;
    public int ritardo_arrivo;
    public String data_partenza_programmata;
    public String data_stimata;
    public int ritardo_partenza;
    public boolean treno_arrivato;
    public boolean treno_partito;
    public String binario;
    public boolean binario_confermato = false;
    public boolean is_origine = false;
    public boolean is_termine = false;
    public boolean is_fermata_soppressa = false;

    public FermataTreno(JSONObject o) {
        try {
            stazione = new Stazione(o.getString("id"), o.getString("stazione"));
            ritardo_arrivo = o.getInt("ritardoArrivo");
            ritardo_partenza = o.getInt("ritardoPartenza");

            //Stato del treno
            treno_partito = !o.getString("partenzaReale").equals("null");
            treno_arrivato = !o.getString("arrivoReale").equals("null") || treno_partito;

            //Gestione binario
            if (!o.getString("binarioEffettivoArrivoDescrizione").equals("null")) {
                binario = "Binario "+o.getString("binarioEffettivoArrivoDescrizione");
                binario_confermato = true;
            } else if (!o.getString("binarioProgrammatoArrivoDescrizione").equals("null")) {
                binario = "Binario "+o.getString("binarioProgrammatoArrivoDescrizione");
            } else if  (!o.getString("binarioEffettivoPartenzaDescrizione").equals("null")) {
                binario = "Binario "+o.getString("binarioEffettivoPartenzaDescrizione");
                binario_confermato = true;
            } else if (!o.getString("binarioProgrammatoPartenzaDescrizione").equals("null")) {
                binario = "Binario "+o.getString("binarioProgrammatoPartenzaDescrizione");
            } else {
                binario = "No binario";
            }
            binario = binario.trim();

            is_fermata_soppressa = o.has("actualFermataType") && o.getInt("actualFermataType") == 3;

            //Magheggi con le date
            Calendar calendar = Calendar.getInstance();
            if (!o.getString("partenza_teorica").equals("null")) {
                calendar.setTimeInMillis(Long.parseLong(o.getString("partenza_teorica")));
                data_partenza_programmata = Utility.formaData(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            } else {
                data_partenza_programmata = "";
            }
            if (!o.getString("arrivo_teorico").equals("null")) {
                calendar.setTimeInMillis(Long.parseLong(o.getString("arrivo_teorico")));
                data_arrivo_programmata = Utility.formaData(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                milliseconds_data_arrivo = Long.parseLong(o.getString("arrivo_teorico"));
            } else {
                data_arrivo_programmata = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
