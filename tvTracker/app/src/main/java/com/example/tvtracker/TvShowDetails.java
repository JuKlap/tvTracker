package com.example.tvtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvtracker.DB.User_dataQuery;
import com.example.tvtracker.REST.RestRequests;

import java.util.ArrayList;

public class TvShowDetails extends AppCompatActivity {
    private ArrayList<TvShowItem> list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TvShowAdapter mAdapter;// bridge between our data and recycler view (cant load all items at once in recyclerview, adapter puts as many as we need
    private RecyclerView.LayoutManager mLayoutManager; // assigns single items in our list
    private ImageView favStar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);

        mRecyclerView = findViewById(R.id.recycler_tvShows);
        CheckBox checkBox = new CheckBox(this);

        TextView tvShowTitle = (TextView) findViewById(R.id.tvShowTitle);
        TextView tvShowSummary = (TextView) findViewById(R.id.tvShowsDetails_Desc);
        TextView tvShowGenreList = (TextView) findViewById(R.id.tvShowDetails_Genres);
        TextView tvShowEpisode = (TextView) findViewById(R.id.TV_textview);

        ArrayList<TvShowItem> tvEpisodes = new ArrayList<TvShowItem>();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(TvShowDetails.this);
        int tvShowId = sharedPref.getInt("tvShowId", 0);
        int userId = sharedPref.getInt("userId", 0);

        RestRequests restRequests = new RestRequests(TvShowDetails.this);

        restRequests.getShowName(tvShowId, new RestRequests.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(TvShowDetails.this, "Error at TvShowDetails request", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String tvShowName) {
               tvShowTitle.setText(tvShowName);
                /*list.add(new TvShowItem(tvShowName, checkBox));
                mRecyclerView.setHasFixedSize(true); // if we know
                mLayoutManager = new LinearLayoutManager(TvShowDetails.this);
                mAdapter = new TvShowAdapter(list);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);*/
            }
        });

        favStar = findViewById(R.id.TvShowFav);
        if (User_dataQuery.showIsFavorite(userId, tvShowId)) {
            favStar.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            favStar.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }

        //TODO add  Favorite/Unfavorite functionality to the button
     /*   favStar.setOnClickListener(new View.OnClickListener() {
            boolean isFavorite = true;d
            @Override
            public void onClick(View view) {
                                }
            }
        });*/

        restRequests.getShowInfo(tvShowId, new RestRequests.TvShowInfoResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(TvShowDetails.this, "Error at TvShowDetails request", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String ShowName, String ShowDescription, String ShowGenres) {
                tvShowTitle.setText(ShowName);
                tvShowSummary.setText(ShowDescription);
                tvShowGenreList.setText(ShowGenres);
            }
        });


    }
}