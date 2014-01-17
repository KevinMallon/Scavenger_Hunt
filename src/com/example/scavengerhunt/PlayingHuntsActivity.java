package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PlayingHuntsActivity extends Activity {

    ListView listView;
    final String currentUsername = (String) ParseUser.getCurrentUser().get(
	    "username");
    final ParseUser currentUser = ParseUser.getCurrentUser();
    InvitationListViewAdapter adapter;
    final Format formatter = new SimpleDateFormat("MM-dd-yy, h:mm a");
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> IDs = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.all_hunts);
	getDeclinedHunts();
    }

    private void getDeclinedHunts() {
	ParseQuery<ParseObject> declinedQuery = ParseQuery.getQuery("Declined");
	declinedQuery.whereEqualTo("userName", currentUsername);
	declinedQuery.findInBackground(new FindCallback<ParseObject>() {
	    @Override
	    public void done(List<ParseObject> declinedHuntsListObject,
		    ParseException e) {
		if (e == null) {
		    final String[] declinedHunts = new String[declinedHuntsListObject
			    .size()];
		    int i = 0;
		    String decline = new String();

		    for (ParseObject declinedHunt : declinedHuntsListObject) {
			decline = (String) declinedHunt.get("hunt");
			declinedHunts[i] = decline;
			System.out.println("declined hunt" + decline);
			i++;
		    }
		    getRemainingHunts(declinedHunts);
		    System.out.println("declined huntslist"
			    + Arrays.asList(declinedHunts));
		}
	    }
	});
    }

    private void getRemainingHunts(String[] declinedHunts) {
	System.out.println("declined huntslist" + Arrays.asList(declinedHunts));
	ParseQuery<ParseObject> huntsquery = ParseQuery.getQuery("Hunt");
	huntsquery
		.whereNotContainedIn("objectId", Arrays.asList(declinedHunts));
	huntsquery.findInBackground(new FindCallback<ParseObject>() {
	    @Override
	    public void done(List<ParseObject> huntsListObject, ParseException e) {
		if (e == null) {
		    for (ParseObject obj : huntsListObject) {
			if (((ArrayList<String>) obj.get("huntPlayers"))
				.contains(currentUsername)) {
			    final Date startDatetime = obj
				    .getDate("start_datetime");
			    final Date endDatetime = obj
				    .getDate("end_datetime");
			    // if (new Date().before(endDatetime)) {

			    titles.add((String) obj.get("title"));
			    IDs.add(obj.getObjectId());
			    // }
			}
		    }

		    listView = (ListView) findViewById(R.id.list);

		    adapter = new InvitationListViewAdapter(
			    PlayingHuntsActivity.this, titles, IDs);

		    listView.setAdapter(adapter);
		    if (adapter.getCount() == 0) {
			Toast.makeText(
				getApplicationContext(),
				" Sorry, There are no hunts available at this time! Try again later!!",
				Toast.LENGTH_LONG).show();
			finish();
		    }

		    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,
				View view, int position, long id) {
			    final String huntId = IDs.get(position);
			    final Intent intent = new Intent(
				    PlayingHuntsActivity.this,
				    PlayHuntActivity.class);
			    intent.putExtra("HuntID", huntId);
			    Log.d("huntId", "hunt id is " + huntId);
			    startActivity(intent);
			}
		    });
		}
	    }
	});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.menuitem_prefs:
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
