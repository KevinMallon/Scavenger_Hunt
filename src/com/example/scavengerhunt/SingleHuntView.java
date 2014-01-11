package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SingleHuntView extends Activity {

    ListView listView;
    ArrayAdapter<String> playersadapter;
    ArrayAdapter<String> itemsadapter;
    final ParseUser currentUser = ParseUser.getCurrentUser();
    final String currentUsername = (String) currentUser.get("username");
    Button acceptButton;
    Button declineButton;

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("HuntID");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.single_hunt_view);

	final TextView tvHuntTitle = (TextView) findViewById(R.id.tvHuntTitle);
	final TextView tvStartTime = (TextView) findViewById(R.id.tvStartTime);
	final TextView tvEndTime = (TextView) findViewById(R.id.tvEndTime);
	final TextView tvComingSoon = (TextView) findViewById(R.id.tvComingSoon);
	final String huntID = getHuntID();
	System.out.println("Show huntID " + huntID);
	ParseQuery<ParseObject> huntquery = ParseQuery.getQuery("Hunt");
	huntquery.whereEqualTo("objectId", huntID);

	huntquery.getInBackground(huntID, new GetCallback<ParseObject>() {
	    @Override
	    public void done(ParseObject object, ParseException e) {
		if (e == null) {
		    String title = new String();
		    String owner = new String();
		    String startTime = new String();
		    String endTime = new String();
		    Format formatter = new SimpleDateFormat("MM-dd-yy, h:mm a");

		    title = (String) object.get("title");
		    startTime = formatter.format(object.get("start_datetime"));
		    endTime = formatter.format(object.get("end_datetime"));

		    owner = (String) object.get("owner");
		    final String username = currentUser.getUsername();
		    System.out.println("current user " + username);
		    System.out.println("owner " + owner);

		    tvHuntTitle.setText(title);
		    tvStartTime.setText(startTime);
		    tvEndTime.setText(endTime);

		    List<String> players = new ArrayList<String>();
		    players = object.getList("huntPlayers");

		    List<String> items = new ArrayList<String>();
		    items = object.getList("huntItems");

		    final Date startDatetime = object.getDate("start_datetime");
		    final Date endDatetime = object.getDate("end_datetime");
		    System.out.println("Date() "
			    + new Date().before(endDatetime)
			    + new Date().after(startDatetime));
		    if (new Date().before(endDatetime)
			    && new Date().after(startDatetime)
			    && object.getParseUser("winner") == null) {
			setPlayerList(players);
			setItemList(items);
		    } else {
			tvComingSoon
				.setText("Players and Items will be shown when the hunt begins.");
		    }

		} else {
		    Log.d("ShowHunt",
			    "ParseObject retrieval error: "
				    + Log.getStackTraceString(e));
		}

	    }

	});
	setupButtonCallbacks();
    }

    private void setItemList(List<String> items) {
	listView = (ListView) findViewById(R.id.listView1);
	itemsadapter = new ArrayAdapter<String>(SingleHuntView.this,
		R.layout.small_list, items);
	listView.setAdapter(itemsadapter);
    }

    private void setPlayerList(List<String> playerList) {
	listView = (ListView) findViewById(R.id.listView2);
	playersadapter = new ArrayAdapter<String>(SingleHuntView.this,
		R.layout.small_list, playerList);
	listView.setAdapter(playersadapter);
    }

    private void setupButtonCallbacks() {
	declineButton = (Button) findViewById(R.id.declineButton);
	declineButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		removeDeclined();

	    }
	});
    }

    private void removeDeclined() {
	final List<String> playerToRemove = new ArrayList<String>();
	playerToRemove.add(currentUsername);
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		Log.i("scavenger Hunt", "in parse query" + hunt.getObjectId());
		if (e == null) {
		    hunt.removeAll("huntPlayers", playerToRemove);
		    hunt.saveInBackground(new SaveCallback() {
			public void done(com.parse.ParseException arg0) {
			    if (arg0 == null) {
				Toast.makeText(
					getApplicationContext(),
					"You've been withdrawn from this hunt.",
					Toast.LENGTH_LONG).show();
				Intent i = new Intent(SingleHuntView.this,
					PlayingHuntsActivity.class);
				i.putExtra("UpdateHuntActivity", getHuntID());
				startActivity(i);
				Log.i("Decline", "Players Removed!");
			    } else {
				Log.i("Decline", "Error removing players "
					+ arg0);
			    }
			}
		    });
		}
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
