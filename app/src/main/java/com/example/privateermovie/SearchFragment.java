package com.example.privateermovie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.privateermovie.Models.MovieSearch;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.btn_startsearch).setOnClickListener(view -> getMovieBySearch());

    }

    void getMovieBySearch() {
        String query = ((EditText)getView().findViewById(R.id.inp_search)).getText().toString();

        String url = "https://api.themoviedb.org/3/search/movie?query=" + query;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            MovieSearch movieSearch = new Gson().fromJson(response, MovieSearch.class);
            Toast.makeText(getContext(), "# results: " + movieSearch.total_results, Toast.LENGTH_LONG).show();
            fillAdapter(movieSearch.results);

        }, error -> Log.e("Volley", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", Secrets.Token);
                return params;
            }
        };
        MainActivity.rq.add(request);
    }

    private void fillAdapter(ArrayList<MovieSearch.Result> results) {
        MovieAdapter adaptor = new MovieAdapter(getContext(), results);
        GridView gridView =  getView().findViewById(R.id.view_results);
        gridView.setAdapter(adaptor);
    }
}