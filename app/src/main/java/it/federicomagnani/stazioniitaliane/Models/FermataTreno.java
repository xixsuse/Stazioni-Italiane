package it.federicomagnani.stazioniitaliane.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class FermataTreno {

    public Stazione stazione;
    public String data_arrivo_programmata;
    public int ritardo_arrivo;
    public String data_partenza_programmata;
    public int ritardo_partenza;

    public FermataTreno(JSONObject o) {
        try {
            stazione = new Stazione(o.getString("id"), o.getString("stazione"));
            ritardo_arrivo = o.getInt("ritardoArrivo");
            ritardo_partenza = o.getInt("ritardoArrivo");

            //Magheggi con le date
            Calendar calendar = Calendar.getInstance();
            if (!o.getString("partenza_teorica").equals("null")) {
                calendar.setTimeInMillis(o.getInt("partenza_teorica"));
                data_partenza_programmata = calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE);
            } else {
                data_partenza_programmata = "--:--";
            }
            if (!o.getString("arrivo_teorico").equals("null")) {
                calendar.setTimeInMillis(o.getInt("arrivo_teorico"));
                data_arrivo_programmata = calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE);
            } else {
                data_arrivo_programmata = "--:--";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
