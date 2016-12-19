package mau.restaurantapp.utils;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONPathBuilder {
    /**
     * Reads a JSONObject and converts it into routes usable for GoogleMap
     *
     * @param jObject JSON received from Google's directional service
     * @return List of routes
     */
    public static List<List> parse(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        List<String> directions = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jObject.getJSONArray("routes");

            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<>();

                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    String o = ((JSONObject) ((JSONObject) jLegs.get(j)).get("distance")).get("text") + "|-|";
                    o += ((JSONObject) ((JSONObject) jLegs.get(j)).get("duration")).get("text") + "|-|";
                    o += ((JSONObject) jLegs.get(j)).get("end_address") + "|-|";
                    o += ((JSONObject) jLegs.get(j)).get("start_address") + "|-|";

                    for (int k = 0; k < jSteps.length(); k++) {
                        String d = o + ((JSONObject) jSteps.get(k)).get("html_instructions") + "|-|";
                        d += ((JSONObject) ((JSONObject) jSteps.get(k)).get("distance")).get("text") + "|-|";
                        d += (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("duration")).get("text");

                        directions.add(d);
                        String polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude));
                            hm.put("lng", Double.toString((list.get(l)).longitude));
                            path.add(hm);
                        }
                    }

                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            // Handle JSONException
        } catch (Exception e) {
            // Handle unknown/unexpected exception
        }

        List<List> r = new ArrayList<>();
        r.add(routes);
        r.add(directions);

        return r;
    }

    /**
     * Decodes each line for the route-builder into a list of LatLng objects.
     *
     * @param encoded string containing raw output
     * @return List of LatLng objects
     */
    private static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
