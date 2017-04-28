package com.example.perfecto.tipcalculator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.perfecto.tipcalculator.api.model.Tip;
import com.example.perfecto.tipcalculator.api.service.TipsServiceInterface;
import com.example.perfecto.tipcalculator.api.TipsClientBuilder;
import com.example.perfecto.tipcalculator.api.service.TipsServiceManager;

import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlexViewActivity extends AppCompatActivity
    implements TipCalcFragment.OnListFragmentInteractionListener {

    private static final String TAG = FlexViewActivity.class.getSimpleName();

    private Logger logger = LoggerFactory.getLogger(FlexViewActivity.class);

    private TipsServiceManager tipsServiceManager = new TipsServiceManager("http://paulsbruce-AndroidDemoAppSvc.ngrok.io");

    private RecyclerView recycler;

    public void setTipsServiceManager(TipsServiceManager mServiceManager) {
        this.tipsServiceManager = mServiceManager;
        loadTipsData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flex_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.content_flex_view) != null) {

            if (savedInstanceState != null) {
                return;
            }

            TipCalcFragment firstFragment = new TipCalcFragment();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_flex_view, firstFragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Tip item) {
        logger.debug("Interact: {}", item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        loadTipsData();
    }

    private void loadTipsData() {
        //load data in a background thread
        new GetTipsTask((RecyclerView)findViewById(R.id.tipcalc_list)).execute(tipsServiceManager);
    }

    private class GetTipsTask extends AsyncTask<TipsServiceManager, Object, Boolean> {

        private List<Tip> tips = new ArrayList<Tip>();
        private RecyclerView recycler = null;

        public GetTipsTask(RecyclerView recycler) {
            this.recycler = recycler;
        }

        protected Boolean doInBackground(TipsServiceManager... tipsServiceManager) {

            try {
                tips = tipsServiceManager[0].getTips();
                return true;
            } catch(IOException e) {
                Log.e(TAG, e.toString());
            }
            return false;
        }

        protected void onProgressUpdate(Object... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Boolean success) {
            if(success) {
                recycler.setAdapter(new TipAdapter(tips, R.layout.fragment_tipcalc, getApplicationContext()));
                Log.d(TAG, "Number of tips received: " + tips.size());
            }
        }
    }

}
