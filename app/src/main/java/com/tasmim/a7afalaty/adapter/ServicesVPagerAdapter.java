package com.tasmim.a7afalaty.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tasmim.a7afalaty.fragment.ServicesFrag;
import com.tasmim.a7afalaty.fragment.ServicesMap;
import com.tasmim.a7afalaty.model.ShowItemsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma7MouD on 11/12/2018.
 */

public class ServicesVPagerAdapter extends FragmentPagerAdapter {

    int Numoftabs;
    List<ShowItemsModel.Datum> list;

    public ServicesVPagerAdapter(FragmentManager fm, int numoftabs_list, List<ShowItemsModel.Datum> list) {
        super(fm);
        this.Numoftabs = numoftabs_list;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ServicesFrag tab1 = new ServicesFrag();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("data_list", (ArrayList<? extends Parcelable>) list);
                tab1.setArguments(bundle1);
                return tab1;

            case 1:
                ServicesMap tab2 = new ServicesMap();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("data_list", (ArrayList<? extends Parcelable>) list);
                tab2.setArguments(bundle2);
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Numoftabs;
    }
}
