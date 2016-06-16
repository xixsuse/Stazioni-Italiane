package it.federicomagnani.stazioniitaliane;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class FragmentStazioni extends Fragment {

    public static String id_stazione = "";
    public static String nome_stazione = "";
    FragmentPagerItemAdapter adapter;

    public FragmentStazioni() {
        // Required empty public constructor
    }

    public static FragmentStazioni newInstance() {
        return new FragmentStazioni();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stazioni, container, false);

        if (id_stazione.length()>0) {
            adapter = new FragmentPagerItemAdapter(
                    getChildFragmentManager(), FragmentPagerItems.with(getContext())
                    .add("Partenze", FragmentList.class, new Bundler().putInt("section", 1).get())
                    .add("Arrivi", FragmentList.class, new Bundler().putInt("section", 2).get())
                    .create());

            ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);

            SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpagertab);
            viewPagerTab.setViewPager(viewPager);
        }

        return v;
    }

}
