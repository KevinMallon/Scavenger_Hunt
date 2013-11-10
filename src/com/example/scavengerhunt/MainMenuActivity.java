package com.example.scavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainMenuActivity extends Activity {
    private Button createHuntButton;
    private Button viewGamesButton;
    protected ParseObject hunt;
    ParseUser currentUser = ParseUser.getCurrentUser();
    String username = currentUser.getUsername();

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mainmenu);
	setupButtonCallbacks();
    }

    public void onResume() {
	super.onResume();
	currentUser = ParseUser.getCurrentUser();
    }

    private void doCreateHunt() {
	ParseUser currentUser = ParseUser.getCurrentUser();

	if (currentUser != null && currentUser.getObjectId() != null) {
	    final String username = currentUser.getUsername();
	    final ParseObject hunt = new ParseObject("Hunt");
	    hunt.put("owner", username);
	    hunt.saveInBackground(new SaveCallback() {
		public void done(ParseException e) {
		    if (e == null) {
			String huntID = hunt.getObjectId();
			Intent i = new Intent(MainMenuActivity.this,
				CreateHuntActivity.class);
			i.putExtra("CreateHuntActivity", huntID);
			startActivity(i);
		    } else {
			Log.d("CreateHuntActivity",
				"ParseObject retrieval error: "
					+ Log.getStackTraceString(e));
		    }
		}

	    });

	}
    }

    private void setupButtonCallbacks() {
	createHuntButton = (Button) findViewById(R.id.mainMenuButton_createHunt);
	createHuntButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		doCreateHunt();
	    }
	});
	/*
	 * joinGameButton = (Button) findViewById(R.id.mainMenuButton_joinGame);
	 * joinGameButton.setOnClickListener(new OnClickListener() { public void
	 * onClick(View v) { // XXX open JoinGameActivity // Intent i = new
	 * Intent(mThisActivity, JoinGameActivity.class); //
	 * mThisActivity.startActivity(i); } });
	 */

	viewGamesButton = (Button) findViewById(R.id.mainMenuButton_myGames);
	viewGamesButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		// XXX open MyGamesActivity
		// Intent i = new Intent(mThisActivity, MyGamesActivity.class);
		// mThisActivity.startActivity(i);
	    }
	});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.mainmenu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.menuitem_prefs:
	    // Intent i = new Intent(mThisActivity, PrefsActivity.class);
	    // mThisActivity.startActivity(i);
	    return true;
	case R.id.menuitem_logout:
	    ParseUser.logOut();
	    finish();
	    return true;
	default:
	    break;
	}
	return false;
    }

}
