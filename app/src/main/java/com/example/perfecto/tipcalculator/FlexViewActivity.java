package com.example.perfecto.tipcalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.perfecto.tipcalculator.api.Tip;
import com.example.perfecto.tipcalculator.api.TipsClient;
import com.example.perfecto.tipcalculator.api.TipsClientBuilder;
import com.example.perfecto.tipcalculator.api.TipsClientBuilder.TipsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlexViewActivity extends AppCompatActivity
    implements TipCalcFragment.OnListFragmentInteractionListener {

    private static final String TAG = FlexViewActivity.class.getSimpleName();

    private TipsClient client;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.content_flex_view) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            TipCalcFragment firstFragment = new TipCalcFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_flex_view, firstFragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Tip item) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        recycler = (RecyclerView) findViewById(R.id.tipcalc_list);
        connectAndGetApiData();

    }

    private void connectAndGetApiData() {
        client = TipsClientBuilder.build();
        Call<TipsResponse> call = client.getTips();
        call.enqueue(new Callback<TipsResponse>() {
            @Override
            public void onResponse(Call<TipsResponse> call, Response<TipsResponse> response) {
                List<Tip> tips = response.body().getResults();
                if(recycler != null)
                    recycler.setAdapter(new TipAdapter(tips, R.layout.fragment_tipcalc, getApplicationContext()));
                Log.d(TAG, "Number of movies received: " + tips.size());
            }

            @Override
            public void onFailure(Call<TipsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

}
