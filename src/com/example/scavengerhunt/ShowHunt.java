package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ShowHunt extends Activity {
    ListView listView;
    Button button;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    private String getHuntID() {
	Intent showhunt = getIntent();
	String huntID = showhunt.getStringExtra("HuntID");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.show_hunt);
	final TextView tvHuntTitle = (TextView) findViewById(R.id.tvHuntTitle);
	final TextView tvStartTime = (TextView) findViewById(R.id.tvStartTime);
	final TextView tvEndTime = (TextView) findViewById(R.id.tvEndTime);

	final ParseQuery<ParseObject> huntquery = ParseQuery.getQuery("Hunt");
	huntquery.whereEqualTo("objectId", getHuntID());
	huntquery.findInBackground(new FindCallback<ParseObject>() {
	    public void done(List<ParseObject> object, ParseException e) {
		if (e == null) {
		    Log.d("ShowHunt", "got it");
		    List<String> title = new ArrayList<String>();
		    List<String> startTime = new ArrayList<String>();
		    List<String> endTime = new ArrayList<String>();
		    Format formatter = new SimpleDateFormat("MM-dd-yy HH:mm");
		    for (ParseObject huntObject : object) {
			huntObject.get("title");
			title.add((String) huntObject.get("title"));
			startTime.add((String) formatter.format(huntObject
				.get("start_datetime")));
			endTime.add((String) formatter.format(huntObject
				.get("end_datetime")));
		    }

		    String endTimeString = endTime.get(0);
		    String startTimeString = startTime.get(0);
		    tvHuntTitle.setText(title.get(0));
		    tvStartTime.setText(startTimeString);
		    tvEndTime.setText(endTimeString);
		} else {
		    Log.d("ShowHunt",
			    "ParseObject retrieval error: "
				    + Log.getStackTraceString(e));
		}
	    }
	});

	ParseQuery<ParseObject> query = ParseQuery.getQuery("Item");
	query.whereEqualTo("huntID", getHuntID());
	query.selectKeys(Arrays.asList("itemName"));
	try {
	    List<ParseObject> itemNameListObject = query.find();

	    List<String> values = new ArrayList<String>();

	    for (ParseObject obj : itemNameListObject) {
		values.add((String) obj.get("itemName"));
	    }

	    listView = (ListView) findViewById(R.id.listView1);

	    // Bind array strings into an adapter
	    adapter = new ArrayAdapter<String>(this, R.layout.small_list,
		    values);

	    listView.setAdapter(adapter);

	} catch (ParseException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	setupButtonCallbacks();
    }

    @Override
    public void onResume() {
	super.onResume();
	this.showPlayers();
    }

    private void showPlayers() {

	ParseQuery<ParseObject> playerquery = ParseQuery.getQuery("Player");
	playerquery.whereEqualTo("huntID", getHuntID());
	playerquery.selectKeys(Arrays.asList("playerName"));
	try {
	    List<ParseObject> playerNameListObject = playerquery.find();
	    List<String> players = new ArrayList<String>();
	    if (playerNameListObject.size() > 0) {
		for (ParseObject obj : playerNameListObject) {
		    players.add((String) obj.get("playerName"));
		}

		listView = (ListView) findViewById(R.id.listView2);

		// Bind array strings into an adapter
		adapter2 = new ArrayAdapter<String>(this, R.layout.small_list,
			players);

		listView.setAdapter(adapter2);
	    }

	} catch (ParseException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	setupButtonCallbacks();
    }

    private void setupButtonCallbacks() {
	// findViewById(R.id.select_players).setOnClickListener(
	// new OnClickListener() {
	// public void onClick(View v) {
	// Intent i = new Intent(ShowHunt.this,
	// PlayersActivity.class);
	// i.putExtra("huntID", getHuntID());
	// startActivity(i);
	// }
	// });
    }

}
