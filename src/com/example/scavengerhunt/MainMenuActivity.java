package com.example.scavengerhunt;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainMenuActivity extends Activity {
    private Button createHuntButton;
    private Button myHuntsButton;
    private Button playingHuntsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mainmenu);
	ParseInstallation installation = ParseInstallation
		.getCurrentInstallation();
	installation.put("owner", ParseUser.getCurrentUser());
	installation.saveInBackground();
	ParseAnalytics.trackAppOpened(getIntent());
	setupButtonCallbacks();
    }

    private void doCreateHunt(final String title) {
	ParseUser currentUser = ParseUser.getCurrentUser();

	if (currentUser != null && currentUser.getObjectId() != null) {
	    final String username = currentUser.getUsername();
	    final ParseObject hunt = new ParseObject("Hunt");
	    hunt.put("title", title);
	    hunt.put("owner", username);
	    hunt.saveInBackground(new SaveCallback() {
		@Override
		public void done(ParseException e) {
		    if (e == null) {
			String huntID = hunt.getObjectId();
			Intent i = new Intent(MainMenuActivity.this,
				CreateHuntActivity.class);
			Log.i("ScavengerHuntActivity", "intent Title " + title);
			i.putExtra("CreateHuntActivity", huntID);
			i.putExtra("Title", title);
			startActivity(i);
		    } else {
			Log.d("CreateHuntActivity",
				"ParseObject retrieval error: "
					+ Log.getStackTraceString(e));
		    }
		}

	    });
	} else {
	    CharSequence text = "Sorry, the hunt was not updated.";
	    finish();
	}
    }

    public void showStartTitleDialog(View v) {
	;
	DialogFragment newFragment = new TitleFragment();
	newFragment.show(getFragmentManager(), "startTitle");
    }

    public void onFinishTitleFragment(String title) {
	doCreateHunt(title);
    }

    private void setupButtonCallbacks() {
	createHuntButton = (Button) findViewById(R.id.mainMenuButton_createHunt);
	createHuntButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		showStartTitleDialog(v);
	    }
	});

	myHuntsButton = (Button) findViewById(R.id.mainMenuButton_myHunts);
	myHuntsButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MainMenuActivity.this,
			MyHuntsActivity.class);
		startActivity(i);
	    }
	});

	playingHuntsButton = (Button) findViewById(R.id.mainMenuButton_huntsPlaying);
	playingHuntsButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MainMenuActivity.this,
			PlayingHuntsActivity.class);
		startActivity(i);

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
