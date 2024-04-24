package com.example.privateermovie;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initGui();
        rq = Volley.newRequestQueue(getApplicationContext());
        fragmentChanger(StartFragment.class);
        //getMovieBySearch("Star Wars");
        //getMovieByIdAsync(17);
    }

    void  initGui(){
        findViewById(R.id.nav_movie).setOnClickListener(v -> {fragmentChanger(MovieFragment.class);});
        findViewById(R.id.nav_series).setOnClickListener(v -> {fragmentChanger(SeriesFragment.class);});
        findViewById(R.id.nav_watchlist).setOnClickListener(v -> {fragmentChanger(WatchlistFragment.class);});
        findViewById(R.id.btn_search).setOnClickListener(v -> {fragmentChanger(SearchFragment.class);});
    }


    private void fragmentChanger(Class c) {
        // if (getSupportFragmentManager().getBackStackEntryCount() > 0)

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // Name can be null
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            showCloseAppDialog();
        }
    }

    private void showCloseAppDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Going back further will close the app. Are you sure you want to continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Close the app
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dismiss the dialog
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public void getMovieByIdAsync(int id) {
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        {
            String url = "https://api.themoviedb.org/3/movie/" + id;
            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                Movie movie = new Gson().fromJson(response, Movie.class);
                Toast.makeText(getApplicationContext(), movie.title, Toast.LENGTH_LONG).show();
            }, error -> Log.e("Volley", error.toString()))
            {
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", Secrets.Token);
                    return params;
                }
            };
            rq.add(request);
        }
    }







}