package com.example.mymap;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location currentLocation;
    private LatLng toLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button favourite = findViewById(R.id.button1);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFavouriteTop3Restaurants();
            }
        });

        Button contactList = findViewById(R.id.button2);
        contactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRecentContact();
            }
        });

        Button navigation = findViewById(R.id.button4);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format("http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude(),
                        toLocation.latitude,
                        toLocation.longitude);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        final ImageView current = findViewById(R.id.button3);
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                MapsActivity.this.getCurrentLocation();
                LatLng here = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(here).title("You are here"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(here, 12));
            }
        });


    }

    private void getCurrentLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            assert locationManager != null;
            boolean checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!checkGPS) {
                Toast.makeText(this, "No GPS Available", Toast.LENGTH_SHORT).show();
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(10.0f);
        //mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Button nav = findViewById(R.id.button4);
                nav.setVisibility(View.VISIBLE);
                MapsActivity.this.toLocation = marker.getPosition();
                marker.showInfoWindow();
                return false;
            }
        });

        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                Button nav = findViewById(R.id.button4);
                nav.setVisibility(View.INVISIBLE);
                MapsActivity.this.toLocation = null;
            }
        });
    }

    public void myFavouriteTop3Restaurants(){
        mMap.clear();

        LatLng restaurant1 = new LatLng(37.426290, -122.144490);
        mMap.addMarker(new MarkerOptions().position(restaurant1).title("Joanie's Cafe").snippet("American restaurant").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(restaurant1));

        LatLng restaurant2 = new LatLng(37.413681, -122.125351);
        mMap.addMarker(new MarkerOptions().position(restaurant2).title("Tufo House").snippet("Korean barbecue restaurant").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(restaurant2));

        LatLng restaurant3 = new LatLng(37.445068, -122.163773);
        mMap.addMarker(new MarkerOptions().position(restaurant3).title("Evvia Estiatorio").snippet("Greek restaurant").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(restaurant3));
    }

    public void myRecentContact(){
        mMap.clear();

        LatLng list1 = new LatLng(37.43, -122.14);
        mMap.addMarker(new MarkerOptions().position(list1).title("Jessie").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(list1));


        LatLng list2 = new LatLng(37.42, -122.15);
        mMap.addMarker(new MarkerOptions().position(list2).title("Ray").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(list2));

        LatLng list3 = new LatLng(37.44, -122.12);
        mMap.addMarker(new MarkerOptions().position(list3).title("Mother").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(list3));


    }




}
