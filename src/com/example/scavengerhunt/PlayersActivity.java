package com.example.scavengerhunt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PlayersActivity extends Activity implements OnClickListener {

    protected static final String String = null;
    ListView listView;
    Button button;
    ArrayAdapter<String> adapter;
    List<ParseUser> objects;

    private String getCurrentPlayers() {
	Intent intent = getIntent();
	String currentPlayers = intent.getStringExtra("currentPlayers");
	System.out.println("currentPlayers received " + currentPlayers);
	return currentPlayers;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.players_activity);
	ParseQuery<ParseUser> query = ParseUser.getQuery();
	query.findInBackground(new FindCallback<ParseUser>() {
	    @Override
	    public void done(List<ParseUser> objects, ParseException e) {
		if (e == null) {
		    String[] usernames = new String[objects.size()];
		    int selectedPlayerPositions[] = new int[objects.size()];
		    int i = 0;
		    int a = 0;
		    String currentPlayers = getCurrentPlayers();
		    System.out.println("currentPlayers in onCreate "
			    + currentPlayers);
		    String username = new String();
		    for (ParseUser obj : objects) {
			username = (java.lang.String) obj.get("username");
			usernames[i] = username;
			if (currentPlayers != null) {
			    boolean contains = currentPlayers
				    .contains(username);
			    System.out.println("username " + username);
			    System.out.println("contains? " + contains);
			    if (contains == true) {
				selectedPlayerPositions[a] = i;
			    }
			}
			i++;
			a++;
		    }
		    System.out.println("selectedPlayerPositions "
			    + selectedPlayerPositions);
		    listView = (ListView) findViewById(R.id.list);
		    button = (Button) findViewById(R.id.testbutton);
		    adapter = new ArrayAdapter<String>(PlayersActivity.this,
			    android.R.layout.simple_list_item_multiple_choice,
			    usernames);
		    listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		    listView.setAdapter(adapter);
		    if (currentPlayers != null) {
			for (int position : selectedPlayerPositions) {
			    listView.setItemChecked(position, true);
			}
		    }
		    button.setOnClickListener(PlayersActivity.this);
		}
	    }
	});
    }

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("huntID");
	return huntID;
    }

    @Override
    public void onClick(View v) {
	removePlayers();
	SparseBooleanArray checked = listView.getCheckedItemPositions();
	ArrayList<String> selectedPlayers = new ArrayList<String>();
	ArrayList<String> selectedPositions = new ArrayList<String>();
	for (int i = 0; i < checked.size(); i++) {
	    // Item position in adapter
	    int position = checked.keyAt(i);
	    // Add if it is checked i.e.) == TRUE!
	    if (checked.valueAt(i))
		selectedPlayers.add(adapter.getItem(position));
	    selectedPositions.add(Integer.toString(position));
	}
	savePlayers(selectedPlayers);
	returnPlayers(selectedPlayers);
    };

    private void removePlayers() {
	ParseQuery<ParseObject> playerquery = ParseQuery.getQuery("HuntPlayer");
	playerquery.whereEqualTo("huntId", getHuntID());
	playerquery.findInBackground(new FindCallback<ParseObject>() {
	    @Override
	    public void done(List<ParseObject> objects, ParseException e) {
		System.out.println(" # players " + objects.size());
		if (e == null) {
		    for (ParseObject obj : objects) {
			obj.deleteInBackground(new DeleteCallback() {
			    @Override
			    public void done(ParseException e) {
				if (e == null) {
				    Log.d("Delete", "huntPlayer deleted!");
				} else {
				    Log.d("Delete",
					    "Error deleting huntPlayer: " + e);
				}
			    }
			});
		    }
		}
	    }
	});
    }

    private void savePlayers(ArrayList<String> selectedPlayers) {
	String huntId = getHuntID();
	for (int i = 0; i < selectedPlayers.size(); i++) {
	    String user = selectedPlayers.get(i);
	    Log.d("Player", user.toString());
	    ParseObject huntPlayer = new ParseObject("HuntPlayer");
	    huntPlayer.put("userName", user);
	    huntPlayer.put("huntId", huntId);
	    huntPlayer.saveInBackground(new SaveCallback() {
		@Override
		public void done(ParseException e) {
		    if (e == null) {
			Log.d("Save", "gamePlayer data saved!");
		    } else {
			Log.d("Save", "Error saving gamePlayer: " + e);
		    }
		}

	    });
	}
    }

    // private void savePlayers(final ArrayList<String> selectedPlayers) {
    // ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
    // query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
    // public void done(ParseObject hunt, com.parse.ParseException e) {
    // if (e == null) {
    // System.out.println("players " + selectedPlayers);
    // System.out.println("huntID " + getHuntID());
    // hunt.add("players", selectedPlayers);
    // hunt.saveInBackground(new SaveCallback() {
    // public void done(com.parse.ParseException arg0) {
    // if (arg0 == null) {
    // Log.d("Player Save", "Players Saved!");
    // } else {
    // Log.i("PlayersActivity",
    // "Error saving players " + arg0);
    // }
    // }
    // });
    // }
    // }
    // });
    // }

    private void returnPlayers(ArrayList<String> selectedPlayers) {
	String[] players = new String[selectedPlayers.size()];
	for (int i = 0; i < selectedPlayers.size(); i++) {
	    players[i] = selectedPlayers.get(i);
	}
	Intent intent = new Intent(PlayersActivity.this,
		CreateHuntActivity.class);
	Bundle bundle = new Bundle();
	bundle.putSerializable("players", players);
	intent.putExtras(bundle);
	setResult(2, intent);
	finish();
    }

}
