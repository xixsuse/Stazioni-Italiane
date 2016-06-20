package it.federicomagnani.stazioniitaliane;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

import it.federicomagnani.stazioniitaliane.Models.Stazione;

public class FragmentAggiornamento extends Fragment {

    public FragmentAggiornamento() {
        // Required empty public constructor
    }

    public static FragmentAggiornamento newInstance() {
        return new FragmentAggiornamento();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_aggiornamento, container, false);

        new Stazione().save();
        if (Stazione.listAll(Stazione.class).size()>1000) {
            //Ci sono già i dati, quindi vado alle stazioni
            Stazione.delete(new Stazione());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, FragmentStazioni.newInstance(), "fragment_corrente")
                    .commit();
            return v;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AggiornaStazioniTask().execute(1);
            }
        }, 200);

        return v;
    }

    private class AggiornaStazioniTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {

            //I dati non ci sono, quindi aggiorno
            Stazione.deleteAll(Stazione.class);
            String json_txt;
            final ProgressBar progress = (ProgressBar) getActivity().findViewById(R.id.progress_aggiornamento);
            try {
                InputStream is = getActivity().getAssets().open("stazioni.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json_txt = new String(buffer, "UTF-8");
                try {
                    JSONArray json = new JSONArray(json_txt);
                    for (int i=0; i<json.length(); i++) {
                        new Stazione(json.getJSONObject(i)).save();
                        final int load = i;
                        Thread thread = new Thread()
                        {
                            public void run()
                            {
                                progress.setProgress(load);

                            }
                        };
                        thread.start();
                    }
                    Log.d("stazioni caricate", "Totale di "+json.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, FragmentStazioni.newInstance(), "fragment_corrente")
                    .commit();
        }

    }

}
