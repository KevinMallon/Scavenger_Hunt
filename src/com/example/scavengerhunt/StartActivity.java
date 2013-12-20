package com.example.scavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class StartActivity extends Activity {

    @SuppressWarnings("rawtypes")
    class InitializeParseTask extends AsyncTask {
	@Override
	protected Object doInBackground(Object... params) {
	    ScavengerHuntApplication.getInstance().initializeParse();
	    return null;
	}

	@Override
	protected void onPostExecute(Object result) {
	    startActivity(new Intent(StartActivity.this, LoginActivity.class));
	    finish();
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.splash);
	new InitializeParseTask().execute();
    }

}
