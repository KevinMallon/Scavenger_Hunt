package com.example.scavengerhunt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CreateHuntActivity extends Activity {

    static final int START_DATE_DIALOG_ID = 1;
    static final int START_TIME_DIALOG_ID = 2;
    static final int END_DATE_DIALOG_ID = 3;
    static final int END_TIME_DIALOG_ID = 4;
    public Button selectPlayersButton;
    public Button addItemsButton;
    public Button createhuntButton_CreateHunt;
    public String startDate;
    public String startTime;

    public ArrayList<String> itemStrArr;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    final Intent i = getIntent();

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("CreateHuntActivity");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_hunt);
	// selectPlayersButton = (Button) findViewById(R.id.select_players);
	addItemsButton = (Button) findViewById(R.id.add_items);
	createhuntButton_CreateHunt = (Button) findViewById(R.id.createhuntButton_CreateHunt);
	setupButtonCallbacks();
    }

    private void doUpdateHunt() {
	System.out.println("starting update");
	final ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	final Intent i = getIntent();
	String huntID = i.getStringExtra("CreateHuntActivity");
	System.out.println(huntID);
	final Context context = this;
	query.getInBackground(huntID, new GetCallback<ParseObject>() {
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		System.out.println(hunt);
		if (e == null) {
		    hunt.put("title", getHuntTitleInput());
		    hunt.put("start_datetime", getStartDateTime());
		    hunt.put("end_datetime", getEndDateTime());
		    hunt.saveInBackground();
		    Intent showhunt = new Intent(CreateHuntActivity.this,
			    ShowHunt.class);
		    showhunt.putExtra("HuntID", getHuntID());
		    System.out.println("from call on update hunt "
			    + getHuntID());
		    startActivity(showhunt);
		} else {
		    CharSequence text = "Sorry, the hunt was not updated.";
		    int duration = Toast.LENGTH_SHORT;
		    Toast.makeText(context, text, duration).show();
		    finish();
		}
	    }
	});
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == 100) {
	    if (resultCode == RESULT_OK) {
		String huntID = data.getStringExtra("huntID");
		showItems(huntID);
		showPlayers(huntID);
	    } else {
		// do something else
	    }

	}
    }

    private void showPlayers(String huntID) {
	ParseQuery<ParseObject> playerquery = ParseQuery.getQuery("Player");
	playerquery.whereEqualTo("huntID", huntID);
	System.out.println("first in showP " + huntID);
	playerquery.selectKeys(Arrays.asList("playerName"));
	List<ParseObject> playerNameListObject;

	try {
	    playerNameListObject = playerquery.find();
	    if (playerNameListObject.size() > 0) {
		System.out.println("# of players "
			+ playerNameListObject.size());
		int playerCt = playerNameListObject.size();
		TextView playerCount = (TextView) findViewById(R.id.tv2);
		playerCount.setText(playerCt + " players selected.");
		// for (ParseObject obj : playerNameListObject) {
		// players.add((String) obj.get("playerName"));
		// }
		//
		// ListView listView2 = (ListView) findViewById(R.id.listView2);
		//
		// // Bind array strings into an adapter
		// adapter2 = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, players);
		//
		// listView2.setAdapter(adapter2);
	    }

	} catch (com.parse.ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void showItems(String huntID) {
	System.out.println("showing item list");
	ParseQuery<ParseObject> itemquery = ParseQuery.getQuery("Item");
	itemquery.whereEqualTo("huntID", huntID);
	itemquery.selectKeys(Arrays.asList("itemName"));
	List<ParseObject> itemNameListObject;
	try {
	    itemNameListObject = itemquery.find();
	    System.out.println("first in showI " + huntID);
	    if (itemNameListObject.size() > 0) {
		int itemCt = itemNameListObject.size();
		TextView itemCount = (TextView) findViewById(R.id.tv1);
		itemCount.setText(itemCt + " items added.");

		// for (ParseObject obj : itemNameListObject) {
		// values.add((String) obj.get("itemName"));
		// }
		//
		// ListView listView1 = (ListView) findViewById(R.id.listView1);
		//
		// // Bind array strings into an adapter
		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, values);
		//
		// listView1.setAdapter(adapter);
	    }
	} catch (com.parse.ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private String getHuntTitleInput() {
	return getUserInput(R.id.textbox_Title);
    }

    private Date getStartDateTime() {
	return convertToDateTime(getUserInput(R.id.editStartDate) + " "
		+ getUserInput(R.id.editStartTime));
    }

    private Date getEndDateTime() {
	return convertToDateTime(getUserInput(R.id.editEndDate) + " "
		+ getUserInput(R.id.editEndTime));
    }

    private String getUserInput(int id) {
	EditText input = (EditText) findViewById(id);
	return input.getText().toString();
    }

    private void setupButtonCallbacks() {

	findViewById(R.id.select_players).setOnClickListener(
		new OnClickListener() {
		    public void onClick(View v) {
			Intent i = new Intent(CreateHuntActivity.this,
				PlayersActivity.class);
			i.putExtra("huntID", getHuntID());
			startActivityForResult(i, 100);
		    }
		});

	findViewById(R.id.add_items).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		Intent i = new Intent(CreateHuntActivity.this,
			ItemsActivity.class);
		i.putExtra("huntID", getHuntID());
		System.out.println("switching to pick Items " + getHuntID());
		startActivityForResult(i, 100);
	    }
	});

	findViewById(R.id.createhuntButton_CreateHunt).setOnClickListener(
		new OnClickListener() {
		    public void onClick(View v) {
			doUpdateHunt();
		    }
		});

	findViewById(R.id.createhuntButton_cancel).setOnClickListener(
		new OnClickListener() {
		    public final void onClick(View v) {
			ParseUser.logOut();
			finish();
		    }
		});
    }

    private Date convertToDateTime(String dateString) {
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy h:mm a",
		Locale.US);
	Date convertedDate = new Date();
	try {
	    convertedDate = dateFormat.parse(dateString);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return convertedDate;
    }

    public void showStartDatePickerDialog(View v) {
	DialogFragment newFragment = new DatePickerFragment();
	newFragment.show(getFragmentManager(), "startDatePicker");
    }

    public void showStartTimePickerDialog(View v) {
	DialogFragment newFragment = new TimePickerFragment();
	newFragment.show(getFragmentManager(), "startTimePicker");
    }

    public void showEndDatePickerDialog(View v) {
	DialogFragment newFragment = new DatePickerFragment();
	newFragment.show(getFragmentManager(), "endDatePicker");
    }

    public void showEndTimePickerDialog(View v) {
	DialogFragment newFragment = new TimePickerFragment();
	newFragment.show(getFragmentManager(), "endTimePicker");
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
