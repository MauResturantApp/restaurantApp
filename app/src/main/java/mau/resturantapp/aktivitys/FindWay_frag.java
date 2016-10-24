package mau.resturantapp.aktivitys;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import mau.resturantapp.R;

public class FindWay_frag extends Fragment implements LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Button btnGetDirections;
    private View rod;

    // Information variables
    private Location location;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private boolean canGetLocation = false;

    // Final variables
    private static final long UPDATE_DISTANCE = 10;         // In meters
    private static final long UPDATE_TIME_INTERVAL = 5000;  // In milliseconds

    private static final double RESTAURANT_LATITUDE = 12.396593;
    private static final double RESTAURANT_LONGITUDE = 55.731068;
    private static final LatLng RESTAURANT_COORDINATE = new LatLng(RESTAURANT_LONGITUDE, RESTAURANT_LATITUDE);
    private static final String RESTAURANT_NAME = "Restaurant DTU";
    private static final String RESTAURANT_ADDRESS = "Lautrupvang 15, 2750 Ballerup";

    // Manager
    private LocationManager lm;

    // Google Map
    MapFragment mFrag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.findos_frag, container, false);

        // Location control started
        geoLocation();

        // Button for directions
        // Button for directions
        btnGetDirections = (Button) rod.findViewById(R.id.btnGetDirections);
        btnGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections();
            }
        });

        // Google map
        // Google map
        mFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mFrag.getMapAsync(this);

        return rod;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGeoLocation();
    }

    @Override
    public void onResume() {
        super.onResume();

        geoLocation();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopGeoLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        System.out.println("Lat: " + latitude + " || Long: " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO
    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("Provider has been disabled");
        geoLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("Provider has been disabled");
        stopGeoLocation();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setAllGesturesEnabled(false);

        map.addMarker(new MarkerOptions().position(new LatLng(RESTAURANT_LONGITUDE, RESTAURANT_LATITUDE)).title(RESTAURANT_NAME).snippet(RESTAURANT_ADDRESS)).showInfoWindow();

        map.setOnMarkerClickListener(this);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(RESTAURANT_COORDINATE, 15));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     *
     */
    private void getDirections() {
        // TODO Either make a route on the map with the help of geoLocation or
        // just make new intent with Google Maps and set location

        System.out.println("Longi: " + longitude);
        System.out.println("Lati: " + latitude);
    }

    /**
     * Gets location from GPS. If GPS isn't available network is used.<br>
     * To avoid draining batteries, use {@link #stopGeoLocation()} to stop the tracker when the
     * app's lifecycle is "onPause".
     *
     * @return Returns the current location (as {@link Location})
     */
    private Location geoLocation() {
        // TODO Implement hasPermissions!
        try {
            // Set LocationManager from given Context
            if(lm == null)
                lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // Get location from network
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Update location
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME_INTERVAL, UPDATE_DISTANCE, this);

                // Apply new location
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                // Apply latitude and longitude from update
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                this.canGetLocation = true;
            }
            // If this part of the code is reached throw Exception - we assume GPS is unavailable.
            // Run method to enable location in settings here instead of exception
            else
                promptUserNoGPS();
            // throw new Exception("GPS/Location not available."); // Old code

        } catch(SecurityException e) {
            // TODO If insufficient permissions are granted, this code should be executed. Stacktrace included for now.
            //System.out.println(e.getMessage());
            e.getStackTrace();
        } catch(Exception e) {
            // TODO "Unknown" error handling code goes here. Stacktrace included for now.
            //System.out.println(e.getMessage());
            e.getStackTrace();
        }

        return location;
    }

    /**
     * Stop updating location.
     */
    private void stopGeoLocation() {
        try {
            if (lm != null)
                lm.removeUpdates(this);
        } catch (SecurityException e) {
            // TODO Should probably handle this as well.
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
    }

    /**
     * Prompts the user to turn on GPS.
     */
    private void promptUserNoGPS() {
        // Build alert dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        // Set title and message for user
        alert.setTitle("GPS turned off");
        alert.setMessage("GPS is currently turned off. Turn it on in settings.");

        // Go to settings...
        alert.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        // Or fuck off...
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    /**
     * Will return the current latitude. Will return 0.0 if no location is set. This will only
     * be a "problem" if the person is in the Gulf of Guinea, which is the actual
     * intersection of the Equator and Prime Meridian [0.0;0.0].
     *
     * @return Current latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Will return the current longitude. Will return 0.0 if no location is set. This will only
     * be a "problem" if the person is in the Gulf of Guinea, which is the actual
     * intersection of the Equator and Prime Meridian [0.0;0.0].
     *
     * @return Current longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return True if either network or GPS is available.
     */
    public boolean canGetLocation() {
        return canGetLocation;
    }
}
