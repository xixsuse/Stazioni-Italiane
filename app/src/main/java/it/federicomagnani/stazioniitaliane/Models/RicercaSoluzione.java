package it.federicomagnani.stazioniitaliane.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RicercaSoluzione {

    public Stazione partenza;
    public Stazione arrivo;
    public Date date;

    public RicercaSoluzione(Stazione partenza, Stazione arrivo) {
        this();

        this.partenza = partenza;
        this.arrivo = arrivo;
    }

    public RicercaSoluzione() {
        this.date = new Date();
    }

    public String getData() {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
