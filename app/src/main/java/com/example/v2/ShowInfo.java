package com.example.v2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowInfo extends AppCompatActivity {

    RequestQueue requestQueue;
    TextView textView;
    TextView reccomandation,todayscal, averagecal;
    int res;
    private Toolbar showToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_info);


        showToolbar = findViewById(R.id.show_page_toolbar);
        setSupportActionBar(showToolbar);
        getSupportActionBar().setTitle("Know your Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        textView = findViewById(R.id.resInfo);
        todayscal = findViewById(R.id.todaysintake);
        averagecal = findViewById(R.id.averageIntake);
        reccomandation = findViewById(R.id.reccomandation);

        String myValue = getIntent().getStringExtra("result");

        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        String url = "https://world.openfoodfacts.org/api/v0/product/"+myValue+".json";

        sendAPIRequest(url);

    }

    private void sendAPIRequest(String url){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject product = new JSONObject(response.toString()).getJSONObject("product");
                    JSONObject nutriments = new JSONObject(product.toString()).getJSONObject("nutriments");

                    res = nutriments.getInt("energy_value");
                    giveSuggestion();
                  //  textView.setText(String.valueOf(res));
                    textView.setText("Calories in this item : " + res);

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

    private void giveSuggestion(){
        String url = "https://fitbitapi.herokuapp.com/api";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject goals = new JSONObject(response.toString()).getJSONObject("goals");
                    int calories = goals.getInt("calories");
                    int todaysIntake = response.getInt("todaysIntake");

                    todayscal.setText("Today's calorie intake : " + todaysIntake);
                    averagecal.setText("Average calorie intake : " + calories);

                    if (calories>(todaysIntake+res)){
                        reccomandation.setText("You can eat it, DON'T WORRY");
                    }else if(calories<=(todaysIntake+res)){
                        reccomandation.setText("Don't take it, your daily calorie limit will exceed");
                    }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.aboutus:
                Log.i("Item selected", "Settings");
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ShowInfo.this, LoginIntent.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }


}
