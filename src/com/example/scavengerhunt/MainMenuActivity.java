package com.example.scavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseUser;

public class MainMenuActivity extends Activity {
    private static final int MY_DATE_DIALOG_ID = 1;
    private Button createHuntButton;
    private Button viewGamesButton;
    @SuppressWarnings("unused")
    // This member will be used for actual game play, which is why it's
    // but since no game play code exists yet, it's unused in this activity
    private ParseUser currentUser;

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

    /**
     * Setup the Screen callbacks
     */
    private void setupButtonCallbacks() {
	createHuntButton = (Button) findViewById(R.id.mainMenuButton_createHunt);
	createHuntButton.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View view) {
		/*
		 * MainMenuActivity Intent i = new Intent(MainMenuActivity.this,
		 * CreateHuntActivity.class); startActivity(i); } });
		 */
		startActivity(new Intent(MainMenuActivity.this,
			CreateHuntActivity.class));
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

    // /////////////////////////////////////////////////////
    // Menu Handler
    // /////////////////////////////////////////////////////

    /**
     * The create options menu event listener. Invoked at the time to create the
     * menu.
     * 
     * @param the
     *            menu to create
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.mainmenu, menu);
	return true;
    }

    /**
     * The options item selected event listener. Invoked when a menu item has
     * been selected.
     */
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
