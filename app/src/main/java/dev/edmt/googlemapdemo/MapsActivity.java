package dev.edmt.googlemapdemo;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Spinner spinner;
    private LocationManager locationManager;
    private String provider;
    private final int FINE_LOCATION_PERMISSION = 9999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Request Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_PERMISSION);
        }

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0: // Hybrid
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 1: // None
                        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                        break;
                    case 2: // Normal
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 3: // Statellite
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 4: // Terrain
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    default:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });


        //Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);

        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null)
            Log.i("Log info", "Location Archived");
        else
            Log.i("Log info", "No location");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


        //Request Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1,  this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

    }


    @Override
    public void onLocationChanged(Location location) {
        Double lat,lng;
        lat = location.getLatitude();
        lng = location.getLongitude();
        // Add a marker in Sydney and move the camera
        LatLng myPosition = new LatLng(lat, lng);

        //Simple Marker
        mMap.addMarker(new MarkerOptions().position(myPosition).title("My Position"));

        //Circle
        //mMap.addCircle(new CircleOptions().center(myPosition).radius(1000).strokeColor(Color.GREEN).fillColor(Color.argb(80,0,0,255)));

        //Polygon
//        LatLng myPosition1 = new LatLng(lat, lng);
//        LatLng myPosition2 = new LatLng(lat+0.1, lng);
//        LatLng myPosition3 = new LatLng(lat, lng+0.2);
//        LatLng myPosition4 = new LatLng(lat+0.1, lng+0.2);
//        mMap.addPolygon(new PolygonOptions().add(myPosition1).add(myPosition2).add(myPosition4).add(myPosition3)
//                .fillColor(Color.argb(80,0,0,255)).strokeColor(Color.BLUE).strokeWidth(10));

        //Polyline
//        LatLng myPosition4 = new LatLng(lat+0.1, lng+0.2);
//        mMap.addPolyline(new PolylineOptions().add(myPosition).add(myPosition4).color(Color.BLUE).geodesic(true));


        //Info windows
        Marker infoWindows = mMap.addMarker(new MarkerOptions().position(myPosition).title("My Position").snippet("This is my postion"));
        infoWindows.showInfoWindow();
        mMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
        Log.i("Log info", " Lat : "+lat+" Long : "+lng);
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this,"Marker "+marker.getTitle()+" is clicked",Toast.LENGTH_SHORT).show();
    }
}
