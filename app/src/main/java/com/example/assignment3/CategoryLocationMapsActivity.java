package com.example.assignment3;

import androidx.fragment.app.FragmentActivity;

import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.assignment3.databinding.ActivityCategoryLocationMapsBinding;

import java.util.Locale;

public class CategoryLocationMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityCategoryLocationMapsBinding binding;
    private String locationToFocus;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryLocationMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationToFocus = getIntent().getExtras().getString("location", "");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void findLocationMoveCamera() {
        // initialise Geocode to search location using String
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // getFromLocationName method works for API 33 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            /**
             * countryToFocus: String value, any string we want to search
             * maxResults: how many results to return if search was successful
             * successCallback method: if results are found, this method will be executed
             *                          runs in a background thread
             */
            if (locationToFocus.equals("")){
                Toast.makeText(getBaseContext(), "Category address not found", Toast.LENGTH_SHORT).show();
            }
            geocoder.getFromLocationName(locationToFocus, 1, addresses -> {
                if (!addresses.isEmpty()) {
                    runOnUiThread(() -> {
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));

                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(newAddressLocation)
                                        .title(locationToFocus)
                        );
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10f));
                    });
                } else {
                    uiHandler.post(() -> {
                    Toast.makeText(getBaseContext(), "Category address not found", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * Change map display type, feel free to explore other available map type:
         * MAP_TYPE_NORMAL: Basic map.
         * MAP_TYPE_SATELLITE: Satellite imagery.
         * MAP_TYPE_HYBRID: Satellite imagery with roads and labels.
         * MAP_TYPE_TERRAIN: Topographic data.
         * MAP_TYPE_NONE: No base map tiles
         */
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        findLocationMoveCamera();
    }
}