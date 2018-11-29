package com.tasmim.a7afalaty.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.activities.ShowItemDetails;
import com.tasmim.a7afalaty.model.ShowItemsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ServicesMap extends Fragment implements OnMapReadyCallback {

    List<ShowItemsModel.Datum> services_list;
    MapView mMapView;
    private GoogleMap googleMap;
    private GpsTracker gpsTracker;
    Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            services_list = getArguments().getParcelableArrayList("data_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_services_map, container, false);
        mMapView = rootView.findViewById(R.id.mapView_frag);
        mMapView.onCreate(savedInstanceState);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                getLocation();

                for (int i = 0; i < services_list.size(); i++) {
                    String title = services_list.get(i).getTitle();
                    double lat = Double.parseDouble(services_list.get(i).getLat());
                    double lng = Double.parseDouble(services_list.get(i).getLng());

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(title));
                }

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (marker.getTitle().equals("موقعى")) {

                        } else {
                            int pos = Integer.parseInt((marker.getId()).replaceAll("\\D+", "")) - 1;
                            Intent intent = new Intent(getActivity(), ShowItemDetails.class);
                            intent.putExtra("item_id", services_list.get(pos).getId());
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    private void getLocation() {
        gpsTracker = new GpsTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            if (marker != null) {
                marker.remove();
            }
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            Log.d("lat: ", latitude + "");
            Log.d("lng: ", longitude + "");

            marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("موقعى").draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            googleMap.setMaxZoomPreference(18f);
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 16.6f);
            googleMap.animateCamera(location);

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
