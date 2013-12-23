package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ShowHunt extends Activity {
    protected static final String ShowHunt = null;
    protected static final int VISIBLE = 0;
    protected static final int GONE = 0;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ParseUser currentUser = ParseUser.getCurrentUser();
    Button editHuntButton;

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

		    if (owner == username) {
			setButtonVisibility();
		    }

		    tvHuntTitle.setText(title);
		    tvStartTime.setText(startTime);
		    tvEndTime.setText(endTime);

		    List<String> players = new ArrayList<String>();
		    players = object.getList("huntPlayers");

		    List<String> items = new ArrayList<String>();
		    items = object.getList("huntItems");

		    setPlayerList(players);
		    setItemList(items);

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
	adapter = new ArrayAdapter<String>(ShowHunt.this, R.layout.small_list,
		items);
	listView.setAdapter(adapter);
    }

    private void setPlayerList(List<String> playerList) {
	listView = (ListView) findViewById(R.id.listView2);
	adapter2 = new ArrayAdapter<String>(ShowHunt.this, R.layout.small_list,
		playerList);
	listView.setAdapter(adapter2);
    }

    @Override
    public void onResume() {
	super.onResume();
    }

    private void setButtonVisibility() {
	editHuntButton.setVisibility(View.GONE);
    }

    private void setupButtonCallbacks() {
	editHuntButton = (Button) findViewById(R.id.edit_hunt);
	editHuntButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(ShowHunt.this, UpdateHuntActivity.class);
		i.putExtra("UpdateHuntActivity", getHuntID());
		startActivity(i);
	    }
	});

    }

}
