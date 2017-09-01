package com.iitp.mayank.celesta2k17.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.iitp.mayank.celesta2k17.R;
import com.iitp.mayank.celesta2k17.adapters.HighlightsRecylerViewAdapter;
import com.iitp.mayank.celesta2k17.data.HighlightsData;
import com.iitp.mayank.celesta2k17.utils.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by manish on 26/8/17.
 */

public class SplashActivity extends Activity {
    Handler handler;
    Runnable action;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        handler = new Handler();
        action = new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        DownloadImagesAysncTask downloadImage = new DownloadImagesAysncTask();
        downloadImage.execute(new ContextWrapper(getApplicationContext()), this);
    }

    // to trigger download task in background thread
    private class DownloadImagesAysncTask extends AsyncTask<Object , Void , Boolean>
    {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean)
            {
                Toast.makeText(getBaseContext(), "Download failed. Please try again later", Toast.LENGTH_LONG).show();
            }
            handler.postDelayed(action, 1000);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            return new NetworkUtils().downloadImages((ContextWrapper) params[0] , (Context)params[1]);
        }
    }

    //to trigger download task for extracting highlights
    private  class fetchHighlihtsAsynctask extends  AsyncTask< Object , Void , ArrayList<HighlightsData> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<HighlightsData> doInBackground(Object... params) {
            return  new NetworkUtils().extractHighlights((ContextWrapper) params[0] , (Context)params[1] ) ;
        }

        @Override
        protected void onPostExecute(ArrayList<HighlightsData> highlightsDatas) {
            //if the data is not uploaded
            if( highlightsDatas != null )
            {
                //populate the recycler view adapter with this data
                HighlightsRecylerViewAdapter highlightsRecylerViewAdapter = new HighlightsRecylerViewAdapter(highlightsDatas);
            }
            else
            {
                Log.e(SplashActivity.class.getName()," Error while fetching highlights ") ;
            }

        }
    }


}

