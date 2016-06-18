package it.federicomagnani.stazioniitaliane.Models;

public class Treno {

    //Questi due parametri lo identificano univocamente
    public String codice_origine;
    public String codice_identificativo;
    public int numero_treno;

    public Treno(String codice_origine, String codice_identificativo, int numero_treno) {
        this.codice_identificativo = codice_identificativo;
        this.codice_origine = codice_origine;
        this.numero_treno = numero_treno;
    }

}
