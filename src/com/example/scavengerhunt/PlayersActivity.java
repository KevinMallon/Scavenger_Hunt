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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PlayersActivity extends Activity implements OnClickListener {
    ParseUser currentUser = ParseUser.getCurrentUser();
    protected static final String String = null;
    ListView listView;
    Button button;
    ArrayAdapter<String> adapter;
    private List<ParseUser> userList = new ArrayList<ParseUser>();

    private String getCurrentPlayers() {
	Intent intent = getIntent();
	String currentPlayers = intent.getStringExtra("currentPlayers");
	return currentPlayers;
    }

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("huntID");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.players_activity);
	ParseQuery<ParseUser> query = ParseUser.getQuery();
	query.findInBackground(new FindCallback<ParseUser>() {
	    @Override
	    public void done(List<ParseUser> users, ParseException e) {
		if (e == null) {
		    String[] usernames = new String[users.size()];
		    boolean[] playerPositions = new boolean[users.size()];
		    int i = 0;
		    String currentPlayers = getCurrentPlayers();
		    System.out.println("currentPlayers in onCreate "
			    + currentPlayers);
		    String username = new String();
		    System.out.println("all users " + users);
		    for (ParseUser user : users) {
			username = user.getString("username");
			usernames[i] = username;
			if (currentPlayers != null) {
			    boolean contains = currentPlayers
				    .contains(username);
			    System.out.println("username " + username);
			    System.out.println("contains? " + contains);
			    playerPositions[i] = contains;
			}
			i++;
		    }
		    userList = users;
		    listView = (ListView) findViewById(R.id.list);
		    button = (Button) findViewById(R.id.testbutton);
		    adapter = new ArrayAdapter<String>(PlayersActivity.this,
			    android.R.layout.simple_list_item_multiple_choice,
			    usernames);
		    listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		    listView.setAdapter(adapter);
		    int a = 0;
		    if (currentPlayers != null) {
			for (boolean position : playerPositions) {
			    listView.setItemChecked(a, position);
			    a++;
			}
		    }
		    button.setOnClickListener(PlayersActivity.this);
		}
	    }
	});
    }

    @Override
    public void onClick(View v) {
	SparseBooleanArray checkedPositions = listView
		.getCheckedItemPositions();
	final ArrayList<String> selectedPlayers = new ArrayList<String>();
	final ArrayList<String> unselectedPlayers = new ArrayList<String>();
	for (int i = 0; i < adapter.getCount(); i++) {
	    if (checkedPositions.get(i)) {
		selectedPlayers.add(adapter.getItem(i));
	    } else {
		unselectedPlayers.add(adapter.getItem(i));
	    }
	}
	removePlayers(selectedPlayers, unselectedPlayers);
	returnPlayers(selectedPlayers);
    };

    private void removePlayers(final ArrayList<String> selectedPlayers,
	    final ArrayList<String> unselectedPlayers) {
	final List<String> removeList = unselectedPlayers;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		if (e == null) {
		    System.out.println("remove " + removeList);
		    System.out.println("save " + selectedPlayers);
		    hunt.removeAll("huntPlayers", removeList);
		    hunt.saveInBackground(new SaveCallback() {
			public void done(com.parse.ParseException arg0) {
			    if (arg0 == null) {
				Log.d("Player Save", "Players Removed!");
			    } else {
				Log.i("PlayersActivity",
					"Error removing players " + arg0);
			    }
			}
		    });
		    savePlayers(selectedPlayers);
		    invitePlayers(hunt, selectedPlayers);
		}
	    }
	});
    }

    private void savePlayers(final ArrayList<String> selectedPlayers) {
	final List<String> addList = selectedPlayers;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		if (e == null) {
		    hunt.addAllUnique("huntPlayers", addList);
		    hunt.saveInBackground(new SaveCallback() {
			public void done(com.parse.ParseException arg0) {
			    if (arg0 == null) {
				Log.d("Player Save", "Players Saved!");
			    } else {
				Log.i("PlayersActivity",
					"Error saving players " + arg0);
			    }
			}
		    });
		}
	    }
	});
    }

    private void invitePlayers(final ParseObject hunt,
	    final ArrayList<String> selectedPlayers) {

	for (String huntPlayer : selectedPlayers) {
	    ParseQuery<ParseInstallation> pushQuery = ParseInstallation
		    .getQuery();
	    pushQuery.whereEqualTo("owner", huntPlayer);

	    Log.d("push player", huntPlayer);
	    ParsePush push = new ParsePush();
	    push.setQuery(pushQuery);
	    push.setMessage(currentUser.getString("username")
		    + " has invited you to join scavenger hunt, "
		    + hunt.getString("title") + "!");
	    push.sendInBackground();

	    final ParseObject notification = new ParseObject("notification");
	    notification.put("huntId", getHuntID());
	    notification.put("huntTitle", hunt.get("title"));
	    notification.put("owner", currentUser.getObjectId());
	    notification.put("invitedUser", huntPlayer);
	    notification.put("sentNotification", true);
	    notification.saveInBackground();
	}
    }

    private void returnPlayers(ArrayList<String> selectedPlayers) {
	String[] players = new String[selectedPlayers.size()];
	for (int i = 0; i < selectedPlayers.size(); i++) {
	    players[i] = selectedPlayers.get(i).toString();
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
