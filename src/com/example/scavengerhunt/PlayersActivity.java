package com.example.scavengerhunt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PlayersActivity extends CreateHuntActivity implements
	OnClickListener {
    ListView listView;
    Button button;
    ArrayAdapter<String> adapter;

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("huntID");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.players_activity);

	ParseQuery<ParseUser> parseUsernameQuery = ParseUser.getQuery();
	parseUsernameQuery.selectKeys(Arrays.asList("username"));

	try {
	    List<ParseUser> usernameListObject = parseUsernameQuery.find();

	    List<String> values = new ArrayList<String>();

	    for (ParseObject obj : usernameListObject) {
		values.add((String) obj.get("username"));
	    }

	    listView = (ListView) findViewById(R.id.list);
	    button = (Button) findViewById(R.id.testbutton);

	    // Bind array strings into an adapter
	    adapter = new ArrayAdapter<String>(this,
		    android.R.layout.simple_list_item_multiple_choice, values);
	    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    listView.setAdapter(adapter);

	    button.setOnClickListener(this);

	} catch (ParseException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
    }

    public void onClick(View v) {
	SparseBooleanArray checked = listView.getCheckedItemPositions();
	ArrayList<String> selectedPlayers = new ArrayList<String>();
	for (int i = 0; i < checked.size(); i++) {
	    // Item position in adapter
	    int position = checked.keyAt(i);
	    // Add if it is checked i.e.) == TRUE!
	    if (checked.valueAt(i))
		selectedPlayers.add(adapter.getItem(position));
	}

	String[] outputStrArr = new String[selectedPlayers.size()];
	System.out.println(selectedPlayers.size());
	for (int i = 0; i < selectedPlayers.size(); i++) {
	    outputStrArr[i] = selectedPlayers.get(i);
	}

	savePlayers(selectedPlayers, getHuntID());

	Intent intent = new Intent(PlayersActivity.this,
		CreateHuntActivity.class);
	intent.putExtra("huntID", getHuntID());
	System.out.println("sending from Player list " + getHuntID());
	setResult(RESULT_OK, intent);
	finish();
    }

    private void savePlayers(ArrayList<String> selectedPlayers, String huntID) {
	for (int i = 0; i < selectedPlayers.size(); i++) {
	    ParseObject player = new ParseObject("Player");
	    player.put("playerName", selectedPlayers.get(i));
	    player.put("huntID", huntID);
	    player.saveInBackground();
	}

    }
}
