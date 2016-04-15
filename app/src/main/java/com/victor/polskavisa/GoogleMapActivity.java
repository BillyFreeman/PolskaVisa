package com.victor.polskavisa;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener {

    private SupportMapFragment mapFragment;
    private List<LocationEntity> entityList;

    private LocationManager locationManager;
    private float mapZoom;
    private boolean foundLocation;

    private LatLng currentLocation;
    private String nearest;

    private GoogleMap myMap;
    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        container = (ViewGroup) findViewById(R.id.main_container);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.g_map_layout);
        mapFragment.getMapAsync(this);

        String[] locArray = getResources().getStringArray(R.array.ppva_cordinates);
        String[] temp;
        entityList = new ArrayList<>();
        for (String s : locArray) {
            temp = s.split(";");
            entityList.add(new LocationEntity(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), temp[2], temp[3]));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        for (LocationEntity le : entityList) {
            myMap.addMarker(new MarkerOptions().position(new LatLng(le.getLat(), le.getLng())).title(le.getTitle()));
        }
        currentLocation = new LatLng(entityList.get(0).getLat(), entityList.get(0).getLng());
        mapZoom = 5.0f;
        myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(mapZoom));

        foundLocation = false;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!foundLocation) {
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mapZoom = 17.0f;
            myMap.moveCamera(CameraUpdateFactory.newLatLng(getNearestLocation(entityList, location)));
            myMap.animateCamera(CameraUpdateFactory.zoomTo(mapZoom));
            Toast.makeText(this, "Найден ближайший к Вам ППВА (" + nearest + ")", Toast.LENGTH_LONG).show();
            foundLocation = true;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private LatLng getNearestLocation(List<LocationEntity> list, Location location) {
        Location iter = new Location(LocationManager.GPS_PROVIDER);
        float distance = -1f;
        int locationIndex = -1;
        for (LocationEntity le : list) {
            iter.setLatitude(le.getLat());
            iter.setLongitude(le.getLng());
            if (distance < 0) {
                distance = location.distanceTo(iter);
                locationIndex = list.indexOf(le);
            } else if (distance > location.distanceTo(iter)) {
                distance = location.distanceTo(iter);
                locationIndex = list.indexOf(le);
            }
        }
        nearest = list.get(locationIndex).getTitle();
        return new LatLng(list.get(locationIndex).getLat(), list.get(locationIndex).getLng());
    }
}
