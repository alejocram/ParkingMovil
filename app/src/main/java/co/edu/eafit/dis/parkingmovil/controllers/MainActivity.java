package co.edu.eafit.dis.parkingmovil.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import co.edu.eafit.dis.parkingmovil.R;
import co.edu.eafit.dis.parkingmovil.app.AppController;
import co.edu.eafit.dis.parkingmovil.models.ModelManager;
import co.edu.eafit.dis.parkingmovil.models.Place;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageTextView = (TextView) findViewById(R.id.message);

        getPlacesFromServer();
    }

    public void updateView(){
        String titles = "";
        for (Place place : ModelManager.getInstance().places) {
            titles += place.getName();
        }
        messageTextView.setText(titles);
    }

    //Imolmentación de Volley
    private void getPlacesFromServer() {
        JsonArrayRequest req = new JsonArrayRequest("https://baas.kinvey.com/appdata/kid_SkWHVhlL/place",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        ModelManager.getInstance().places.clear();

                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Place placeTmp = new Place();
                                placeTmp.setId(obj.getString("_id"));
                                placeTmp.setName(obj.getString("name"));
                                placeTmp.setDetails(obj.getString("details"));
                                placeTmp.setLatitude(obj.getDouble("latitude"));
                                placeTmp.setLongitute(obj.getDouble("longitude"));
                                placeTmp.setPicture(obj.getString("picture"));
                                ModelManager.getInstance().places.add(placeTmp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        updateView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic a2lkX1NrV0hWaGxMOjU2NjNkMzdjNGI1ZDQ5MjU4NGI3MDg3MjdkOWExOWMx");
                headers.put("Content-Type", "multipart/form-data");
                headers.put("X-Kinvey-API-Version","3");
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, "json_array");
    }
}
