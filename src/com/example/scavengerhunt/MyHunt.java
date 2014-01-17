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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MyHunt extends Activity {
    ListView listView;
    ArrayAdapter<String> playersadapter;
    ArrayAdapter<String> itemsadapter;
    ParseUser currentUser = ParseUser.getCurrentUser();
    final String currentUsername = (String) ParseUser.getCurrentUser().get(
	    "username");
    Button editHuntButton;

    private String getHuntID() {
	Intent showhunt = getIntent();
	String huntID = showhunt.getStringExtra("HuntID");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.my_hunt);
	setupEditButtonCallbacks();
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

		    System.out.println("current user " + currentUsername);
		    System.out.println("owner " + owner);

		    tvHuntTitle.setText(title);
		    tvStartTime.setText(startTime);
		    tvEndTime.setText(endTime);

		    List<String> players = object.getList("huntPlayers");

		    List<String> items = object.getList("huntItems");

		    getDecliningPlayers(players);
		    setItemList(items);

		} else {
		    Log.d("ShowHunt",
			    "ParseObject retrieval error: "
				    + Log.getStackTraceString(e));
		}

	    }

	});
    }

    private void getDecliningPlayers(final List<String> players) {
	ParseQuery<ParseObject> declinedQuery = ParseQuery.getQuery("Declined");
	declinedQuery.whereEqualTo("hunt", getHuntID());
	declinedQuery.findInBackground(new FindCallback<ParseObject>() {
	    @Override
	    public void done(List<ParseObject> decliningPlayerListObject,
		    ParseException e) {
		if (e == null) {
		    final String[] decliningPlayers = new String[decliningPlayerListObject
			    .size()];
		    int i = 0;
		    String decliner = new String();

		    for (ParseObject decline : decliningPlayerListObject) {
			decliner = (String) decline.get("userName");
			decliningPlayers[i] = decliner;
			System.out.println("decliner hunt" + decliner);
			i++;
		    }
		    List<String> remainingPlayers = new ArrayList<String>(
			    players);
		    remainingPlayers.removeAll(Arrays.asList(decliningPlayers));
		    setPlayerList(remainingPlayers);
		    System.out.println("remainingPlayers "
			    + Arrays.asList(remainingPlayers));
		}
	    }
	});
    }

    private void setItemList(List<String> items) {
	listView = (ListView) findViewById(R.id.listView1);
	itemsadapter = new ArrayAdapter<String>(MyHunt.this,
		R.layout.small_list, items);
	listView.setAdapter(itemsadapter);
    }

    private void setPlayerList(final List<String> playerList) {
	listView = (ListView) findViewById(R.id.listView2);
	playersadapter = new ArrayAdapter<String>(MyHunt.this,
		R.layout.small_list, playerList);
	listView.setAdapter(playersadapter);
    }

    private String[] getPlayersArray() {
	listView = (ListView) findViewById(R.id.listView2);
	ListAdapter playersAdapter = listView.getAdapter();
	String[] playersArray = new String[playersAdapter.getCount()];
	for (int i = 0; i < playersAdapter.getCount(); i++) {
	    playersArray[i] = (playersAdapter.getItem(i)).toString();
	}
	return playersArray;
    }

    private void setupEditButtonCallbacks() {
	editHuntButton = (Button) findViewById(R.id.edit_hunt);
	editHuntButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MyHunt.this, UpdateHuntActivity.class);
		i.putExtra("UpdateHuntActivity", getHuntID());
		Bundle bundle = new Bundle();
		bundle.putSerializable("players", getPlayersArray());
		i.putExtras(bundle);
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
