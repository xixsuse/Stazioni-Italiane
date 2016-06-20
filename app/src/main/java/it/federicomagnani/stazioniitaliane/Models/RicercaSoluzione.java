package it.federicomagnani.stazioniitaliane.Models;

public class RicercaSoluzione {

    public Stazione partenza;
    public Stazione arrivo;

    public RicercaSoluzione(Stazione partenza, Stazione arrivo) {
        this.partenza = partenza;
        this.arrivo = arrivo;
    }

    public RicercaSoluzione() {

    }

}
