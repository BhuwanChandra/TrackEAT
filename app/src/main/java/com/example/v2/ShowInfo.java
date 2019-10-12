package com.example.v2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowInfo extends AppCompatActivity {

    RequestQueue requestQueue;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_info);

        textView = findViewById(R.id.resInfo);

        String myValue = getIntent().getStringExtra("result");

        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        sendAPIRequest(myValue);

    }

    private void sendAPIRequest(String value){
        String url = "https://world.openfoodfacts.org/api/v0/product/"+value+".json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject product = new JSONObject(response.toString()).getJSONObject("product");
                    JSONObject nutriments = new JSONObject(product.toString()).getJSONObject("nutriments");

                    int res = nutriments.getInt("energy_value");

                  //  textView.setText(String.valueOf(res));
                    textView.setText("Calories : " + res);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error",error.toString());
            }
        });

        requestQueue.add(request);
    }

}
