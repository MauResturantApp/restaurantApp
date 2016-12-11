package mau.resturantapp.aktivitys.mainFragments;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mau.resturantapp.R;
import mau.resturantapp.utils.HtmlUtils;
import mau.resturantapp.utils.JSONPathBuilder;
import mau.resturantapp.utils.ListUtil;

import static android.R.attr.direction;
import static android.R.attr.path;
import static mau.resturantapp.R.id.map;
import static mau.resturantapp.R.id.start;

public class FindWay_frag extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    // Google Services
    GoogleApiClient googleApiClient;
    GoogleMap gMap;
    SupportMapFragment mFrag;
    Location lastLocation;
    Marker restLocation;
    double lastLatitude;
    double lastLongitude;
    boolean pathDrawn = false;

    // View
    private View rod;
    private ImageButton btnGetDirectionsDriving;
    private ImageButton btnGetDirectionsTransit;
    private ImageButton btnGetDirectionsWalking;
    private ImageButton btnGetDirectionsBicycling;
    private LinearLayout directionRoutes;
    private TextView directionRoute;

    // Default values for Google Maps
    private static final String MAPS_UNITS = "&units=metric";
    private static final String MAPS_REGION = "&region=da";
    private static final String MAPS_ALTERNATIVES = "&alternatives=false";
    private static final String MAPS_AVOID = "&avoid=indoor";
    private static long REQUEST_INTERVAL = 30000l;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    // Restaurant info (move to resources?)
    private static final double RESTAURANT_LONGITUDE = 12.396593;
    private static final double RESTAURANT_LATITUDE = 55.731068;
    private static final LatLng RESTAURANT_COORDINATE = new LatLng(RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);
    private static final String RESTAURANT_NAME = "Restaurant DTU";
    private static final String RESTAURANT_ADDRESS = "Lautrupvang 15, 2750 Ballerup";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.findos_frag, container, false);

        // Build GoogleApiClient
        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Check current state of the location setting (and prompt user to enable if disabled)
        checkLocationSetting();

        // View setup
        directionRoutes = (LinearLayout) rod.findViewById(R.id.directionRoutes);
        directionRoute = (TextView) rod.findViewById(R.id.directionRoute);

        // Google map
        mFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mFrag.getMapAsync(this);

        return rod;
    }

    @Override
    public void onStart() {
        if(googleApiClient != null)
            if(!googleApiClient.isConnected())
                googleApiClient.connect();

        super.onStart();
    }

    @Override
    public void onResume() {
        if(googleApiClient != null)
            if(!googleApiClient.isConnected())
                googleApiClient.connect();

        super.onResume();
    }

    @Override
    public void onStop() {
        if(googleApiClient != null)
            if(googleApiClient.isConnected())
                googleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onPause() {
        if(googleApiClient != null)
            if(googleApiClient.isConnected())
                googleApiClient.disconnect();

        super.onPause();
    }

    /**
     * Used to calculate the current direction to the restaurant from current location (via GPS).<br>
     * Available modes:<br>
     *     <ul>
     *         <li>driving - Instructions for cars</li>
     *         <li>transit - Instructions for public transportation</li>
     *         <li>walking - Instructions for walking</li>
     *         <li>bicycling - Instructions for bicycles</li>
     *     </ul>
     *
     *     @param mode Desired transportation mode
     */
    private void getDirections(String mode) {
        if(pathDrawn) {
            gMap.clear();

            // As we've cleared the map, we need to add restaurant's location again
            gMap.addMarker(new MarkerOptions().position(RESTAURANT_COORDINATE).title(RESTAURANT_NAME).snippet(RESTAURANT_ADDRESS));
        }

        String directions = "https://maps.googleapis.com/maps/api/directions/";
        String apiKey = "AIzaSyAJSLBYERt3rGk01l3-2QNvwpUnDwsV-XM";

        // Setting up parameters (input/output format)
        String origin = this.lastLatitude + "," + this.lastLongitude;
        String destination = RESTAURANT_LATITUDE + "," + RESTAURANT_LONGITUDE;

        directions += "json?origin=" + origin + "&destination=" + destination + "&key=" + apiKey + "&mode=" + mode;
        directions += MAPS_ALTERNATIVES + MAPS_AVOID + MAPS_REGION + MAPS_UNITS;

        // DEBUG
        System.out.println(":::::::DIRECTIONS::::::: " + directions);

        // Build a marker for current location
        MarkerOptions options = new MarkerOptions();
        LatLng currentPos = new LatLng(this.lastLatitude, this.lastLongitude);

        options.position(currentPos);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        Marker currentMarker = gMap.addMarker(options);

        // Move camera
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(restLocation.getPosition());
        builder.include(currentMarker.getPosition());

        LatLngBounds bounds = builder.build();

        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        gMap.animateCamera(cu);

        // Build route
        new JSONPathDownloader().execute(directions);

        // Build route description
        directionRoutes.setVisibility(View.VISIBLE);

        // Now that path has been drawn, next time make sure to delete previous
        pathDrawn = true;
    }

    /**
     * Will build the readable directions with instructions.
     * @param directions String-list containing all instructions
     */
    private void buildDescription(List<String> directions) {
        // Build instruction list proper
        List<String> instructions = new ArrayList<>();

        String[] totals = directions.get(0).split("\\|-\\|");
        String totalDistance = totals[0];
        String totalDuration = totals[1];
        String endLocation = totals[2];
        String startLocation = totals[3];

        directionRoute.setText("The trip from " + startLocation.split(",")[0] + " to " + endLocation.split(",")[0] + " will take " + totalDuration + ". The trip is " + totalDistance + ".\n\n");

        // Note: the fromHtml-deprecation can be avoided with adding a 2nd parameter
        // The 2nd parameter is an int describing the HTML-version.
        // As we use "minSdk 16", we can't do this, as it requires APF level of 24. For now, we just
        // use the deprecated version. We don't really need the fromHtml-method, it's just to strip
        // all entities and HTML-tags that comes with JSON from Google
        for(int i = 0; i < directions.size(); i++) {
            String[] in = directions.get(i).split("\\|-\\|");
            if(i == (directions.size()-1))
                instructions.add(Html.fromHtml(in[4]).toString());
            else
                instructions.add(Html.fromHtml(in[4]).toString() + "\n" + in[5] + " - " + in[6]);
        }

        // Add proper instruction list to list adapter
        ArrayAdapter<String> ap = new ArrayAdapter<>(getActivity(), R.layout.findos_instruction_list, instructions);

        // Create the list with adapter
        ListView lw = (ListView) rod.findViewById(R.id.directionsInstructionsList);
        lw.setAdapter(ap);

        // "ListView inside ScrollView"-fix
        ListUtil.setListViewHeightBasedOnChildren(lw);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.gMap = map;

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setAllGesturesEnabled(true);

        map.setOnMarkerClickListener(this);

        restLocation = map.addMarker(new MarkerOptions().position(RESTAURANT_COORDINATE).title(RESTAURANT_NAME).snippet(RESTAURANT_ADDRESS));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(RESTAURANT_COORDINATE, 15));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                .target(restLocation.getPosition())
                .zoom(17)
                .build()
        ));

        restLocation.showInfoWindow();

        // Button for driving directions
        btnGetDirectionsDriving = (ImageButton) rod.findViewById(R.id.btnGetDirectionsCar);
        btnGetDirectionsDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections("driving");
            }
        });

        // Button for transit directions
        btnGetDirectionsTransit = (ImageButton) rod.findViewById(R.id.btnGetDirectionsTransit);
        btnGetDirectionsTransit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections("transit");
            }
        });

        // Button for walking directions
        btnGetDirectionsWalking = (ImageButton) rod.findViewById(R.id.btnGetDirectionsWalk);
        btnGetDirectionsWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections("walking");
            }
        });

        // Button for bicycling directions
        btnGetDirectionsBicycling = (ImageButton) rod.findViewById(R.id.btnGetDirectionsBicycle);
        btnGetDirectionsBicycling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections("bicycling");
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLocation != null) {
                lastLatitude = lastLocation.getLatitude();
                lastLongitude = lastLocation.getLongitude();
            }
        } catch(SecurityException e) {
            // If a SecurityException is thrown, it is due to the fact, that the user is not
            // allowing the app to use current location. This should've been resolved already,
            // but might've been overlooked upon an app-resume or whatever. Anyway, redo the
            // entire please-change-your-settings-dialog check
            checkLocationSetting();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Not needed atm, but has be be implemented for the sake of the interface
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // For now we set the latitude and longitude to 0 - if the map shows a location in the
        // ocean west of Africa, connection basically failed

        lastLatitude = 0;
        lastLongitude = 0;
    }

    /**
     * This will allow us to control how to react to the user's choice in regard of either accepting
     * or declining to turn on location service for the app.
     */
    public void checkLocationSetting() {
        LocationRequest request = LocationRequest.create();

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(REQUEST_INTERVAL);
        request.setFastestInterval(REQUEST_INTERVAL/6);

        LocationSettingsRequest.Builder b = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);

        // This disables the "Never"-option in the dialog)
        b.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> r = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, b.build());

        r.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult lResult) {
                final Status status = lResult.getStatus();
                //final LocationSettingsStates state = lResult.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All good
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Could build an "unknown error"-dialog here, and return to previous
                            // fragment to get away from the now non-functioning map-feature
                            // as we know by this error, that something is not working - and we
                            // can't really know what went wrong at this point)
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // This time we know what's wrong - we're not able to change the settings,
                        // so we could create a "You need to manually enable location settings"-error
                        // and then return to previous fragment, away from the maps-feature.
                        break;

                    default:
                        // We should never reach this part of the code, but IF something strange
                        // should happen, it could be handled with a "Something went terribly wrong
                        // so we'll send you back"-error dialog, and then return to previous
                        // fragment, as we can't be sure that the maps-feature will work properly
                }
            }
        });
    }

    /**
     * Will trigger IF a required action from user has been taken, after being prompted for
     * changing settings/enabling location.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // If this part of the code is reached, we know the settings have been
                        // enabled, and we can continue with no further implications (as if...)
                        break;

                    case Activity.RESULT_CANCELED:
                        // This part of the code is reached if the user says "no" to enabling the
                        // location-settings. Should just return to previous fragment, away from
                        // the maps-feature, which won't work without locations enabled.
                        break;
                }
                break;
        }
    }

    /**
     * Inner Class for Path Downloader
     */
    private class JSONPathDownloader extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONPathTask pathTask = new JSONPathTask();

            // Invokes the thread for parsing the JSON data
            pathTask.execute(result);
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(strUrl);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuilder sb  = new StringBuilder();

                String line;

                while((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }

            return data;
        }
    }

    /**
     * Inner Class for Path Task.
     * This will add lines on GoogleMap in a non-UI thread.
     */
    private class JSONPathTask extends AsyncTask<String, Integer, List<List>> {
        @Override
        protected List<List> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List> pathTask = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                pathTask = JSONPathBuilder.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return pathTask;
        }

        @Override
        protected void onPostExecute(List<List> result) {
            List<List<HashMap<String,String>>> paths = result.get(0);
            List<String> directions = result.get(1);

            // Build path on map
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < paths.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = paths.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            try {
                gMap.addPolyline(lineOptions);
            } catch(NullPointerException e) {
                // If this exception is thrown, something's wrong with the JSON builder
                e.printStackTrace();
            }

            // Add instructions to list
            buildDescription(directions);
        }
    }
}