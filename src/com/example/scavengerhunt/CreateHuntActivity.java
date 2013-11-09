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
import android.widget.ListView;
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

    private static final String TITLE = "Title";
    private static final int ITEMS_CODE = 1;
    final int PLAYER_REQUEST_CODE = 1;
    final int ITEM_REQUEST_CODE = 2;

    public ListView listView;
    public List<ParseObject> ob;
    private String[] myPlayerStringArray;
    // private Bundle b;
    public ArrayList<String> itemStrArr;
    ArrayAdapter<String> adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_hunt);
	// selectPlayersButton = (Button) findViewById(R.id.select_players);
	addItemsButton = (Button) findViewById(R.id.add_items);
	createhuntButton_CreateHunt = (Button) findViewById(R.id.createhuntButton_CreateHunt);
	setupButtonCallbacks();
    }

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("CreateHuntActivity");
	return huntID;
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

    private void showItems() throws ParseException, com.parse.ParseException {
	System.out.println("showing item list");
	ParseQuery<ParseObject> itemquery = ParseQuery.getQuery("Item");
	itemquery.whereEqualTo("huntID", getHuntID());
	itemquery.selectKeys(Arrays.asList("itemName"));
	List<ParseObject> itemNameListObject = itemquery.find();
	if (itemNameListObject.size() > 0) {

	    List<String> values = new ArrayList<String>();

	    for (ParseObject obj : itemNameListObject) {
		values.add((String) obj.get("itemName"));
	    }

	    listView = (ListView) findViewById(R.id.itemList);

	    // Bind array strings into an adapter
	    adapter = new ArrayAdapter<String>(this,
		    android.R.layout.simple_list_item_1, values);

	    listView.setAdapter(adapter);
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
	// findViewById(R.id.select_players).setOnClickListener(
	// new OnClickListener() {
	// public void onClick(View v) {
	// startActivity(new Intent(CreateHuntActivity.this,
	// PlayersActivity.class));
	// }
	// });

	findViewById(R.id.add_items).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		Intent items = new Intent(CreateHuntActivity.this,
			ItemsActivity.class);
		items.putExtra("HuntID", getHuntID());
		System.out.println("from call" + getHuntID());
		startActivity(items);
		// startActivityForResult(i, ITEMS_CODE);
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
