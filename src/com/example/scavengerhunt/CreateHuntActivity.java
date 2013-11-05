package com.example.scavengerhunt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//import android.widget.DatePicker;
//import android.widget.TimePicker;

public class CreateHuntActivity extends MainMenuActivity {

    static final int START_DATE_DIALOG_ID = 1;
    static final int START_TIME_DIALOG_ID = 2;
    static final int END_DATE_DIALOG_ID = 3;
    static final int END_TIME_DIALOG_ID = 4;
    public Button selectPlayersButton;
    public Button addItemsButton;
    private TextView startDateDisplay;
    private TextView playerNameDisplay;
    public String startDate;
    private Button pickStartDate;
    private TextView endDateDisplay;
    public String endDate;
    private Button pickEndDate;
    private int year, month, day;
    private TextView startTimeDisplay;
    public String startTime;
    private Button pickStartTime;
    private TextView endTimeDisplay;
    public String endTime;
    private Button pickEndTime;
    private int hours, min, ampm;
    private EditText titleEditText;
    private String players;

    private static final String TITLE = "Title";
    private static final int ITEMS_CODE = 1;
    final int PLAYER_REQUEST_CODE = 1;
    final int ITEM_REQUEST_CODE = 2;

    ListView listview;
    List<ParseObject> ob;
    private String[] myPlayerStringArray;
    Bundle b;
    ArrayList<String> itemStrArr;

    // ProgressDialog mProgressDialog;
    // ArrayAdapter<String> adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_hunt);
	// EditText titleEditText = (EditText) findViewById(R.id.textbox_Title);
	selectPlayersButton = (Button) findViewById(R.id.select_players);
	addItemsButton = (Button) findViewById(R.id.add_items);

	setupButtonCallbacks();

	ParseACL defaultACL = new ParseACL();
	defaultACL.setPublicReadAccess(true);
	ParseACL.setDefaultACL(defaultACL, true);

    }

    // public void findHuntID() {
    // final ParseQuery<ParseObject> huntquery = ParseQuery.getQuery("Hunt");
    // huntquery.getFirstInBackground(new GetCallback<ParseObject>() {
    // public void done(final ParseObject hunt, final ParseException e) {
    // if (e == null) {
    // String huntID = hunt.getObjectId();
    // Log.d("CreateHuntActivity", "ParseObject retrieved: "
    // + huntID);
    // return;
    // } else {
    // Log.d("CreateHuntActivity", "ParseObject retrieval error: "
    // + Log.getStackTraceString(e));
    // }
    // }
    // });
    // }

    private String getHuntId() {
	return hunt.getObjectId();
    }

    @Override
    public void onResume() {
	super.onResume();
	// Bundle b = getIntent().getExtras();
	// if (b != null) {
	// String[] playerResultArr = b.getStringArray("selectedPlayers");
	// ListView lvPlayers = (ListView) findViewById(R.id.playerList);
	//
	// ArrayAdapter<String> adapterP = new ArrayAdapter<String>(this,
	// android.R.layout.simple_list_item_1, playerResultArr);
	// lvPlayers.setAdapter(adapterP);
	// }
	// String[] playerListDisplay = b.getStringArray("selectedPlayers");
	// players = playerListDisplay.toString();
	// playerNameDisplay.setText(players);
	// for (String players : playerListDisplay) {
	// playername.add((String) players.get("selectedPlayers" + ","));
	// playerNameDisplay.setText(new
	// StringBuilder().append(playerListDisplay(i));
	// for (ParseObject obj : usernameListObject) {
	// values.add((String) obj.get("username" + ","));
	// }
	// }
	// startTimeDisplay.setText(new
	// StringBuilder().append(hours).append(':')
	// .append(min));

    }

    // @Override
    // protected void setTitle() {
    // titleEditText;
    // }

    // @Override
    // public void onPause() {
    // super.onPause();
    //
    // }

    // @Override
    // protected void onSaveInstanceState(Bundle savedInstanceState) {
    //
    // super.onSaveInstanceState(savedInstanceState);
    // // Store UI state to the savedInstanceState.
    // // This bundle will be passed to onCreate on next call.
    // EditText titleEditText = (EditText) findViewById(R.id.textbox_Title);
    // String strTitle = titleEditText.getText().toString();
    // savedInstanceState.putString(TITLE, strTitle);
    //
    // }

    // @Override
    // public void onRestoreInstanceState(Bundle savedInstanceState) {
    // super.onRestoreInstanceState(savedInstanceState);
    // Restore UI state from the savedInstanceState.
    // String strTitle = savedInstanceState.getString(TITLE);
    // setTitle();
    // }

    private void doCreateHunt() {
	ParseUser currentUser = ParseUser.getCurrentUser();

	if (currentUser != null && currentUser.getObjectId() != null) {
	    final String username = currentUser.getUsername();
	    /*
	     * EditText titleEditText = (EditText)
	     * findViewById(R.id.textbox_Title); titleEditText.setText(title);
	     */
	    ParseObject hunt = new ParseObject("Hunt");
	    hunt.put("owner", username);
	    hunt.put("title", getHuntTitleInput());
	    hunt.put("start_datetime", getStartDateTime());
	    hunt.put("end_datetime", getEndDateTime());
	    hunt.put("owner", username);
	    hunt.saveInBackground(new SaveCallback() {
		public void done(com.parse.ParseException e) {
		    if (e == null) {
			final ParseQuery<ParseObject> huntquery = ParseQuery
				.getQuery("Hunt");
			huntquery
				.getFirstInBackground(new GetCallback<ParseObject>() {
				    public void done(ParseObject hunt,
					    com.parse.ParseException e) {
					if (e == null) {
					    String huntID = hunt.getObjectId();
					    Log.d("CreateHuntActivity", huntID);
					    // savePlayers(huntID);
					    saveItems(huntID);
					} else {
					    Log.d("CreateHuntActivity",
						    "ParseObject retrieval error: "
							    + Log.getStackTraceString(e));
					}
				    }
				});

		    } else {
			Log.d("CreateHuntActivity", "ParseObject save error: "
				+ Log.getStackTraceString(e));
		    }
		}
	    });

	}
    }

    // @Override
    // public void onActivityResult(int requestCode, int resultCode, Intent
    // intent) {
    // super.onActivityResult(requestCode, resultCode, intent);
    // if (requestCode == ITEMS_CODE && resultCode == RESULT_OK) {
    // ArrayList<String> itemStrArr = intent
    // .getStringArrayListExtra("items");
    // System.out.println("looksgood" + itemStrArr.size());
    // if (itemStrArr != null) {
    // // List<String> itemsResults = new ArrayList<String>();
    // // .getStringArrayList(itemAndDescriptionArray);
    // System.out.println(itemStrArr.size());
    // for (int i = 0; i < itemStrArr.size(); i++) {
    // ParseObject item = new ParseObject("Item");
    // item.put("itemName", itemStrArr.get(i));
    // // item.put("huntID", huntID);
    // item.saveInBackground();
    // }
    // }
    //
    // }
    // }

    private void savePlayers(String huntID) {
	Bundle b = getIntent().getExtras();
	System.out.println(b.size());
	if (b != null) {
	    ArrayList<String> playerResults = b
		    .getStringArrayList("selectedPlayers");
	    System.out.println(playerResults.size());
	    for (int i = 0; i < playerResults.size(); i++) {
		ParseObject player = new ParseObject("Player");
		player.put("playerName", playerResults.get(i));
		player.put("huntID", huntID);
		player.saveInBackground();
	    }
	}
    }

    private void saveItems(String huntID) {
	Bundle b = getIntent().getExtras();
	System.out.println(b.size());
	if (b != null) {
	    ArrayList<String> itemStrArr = b.getStringArrayList("items");
	    // if (itemStrArr != null) {
	    // List<String> itemsResults = new ArrayList<String>();
	    // .getStringArrayList(itemAndDescriptionArray);
	    System.out.println(itemStrArr.size());
	    for (int i = 0; i < itemStrArr.size(); i++) {
		ParseObject item = new ParseObject("Item");
		item.put("itemName", itemStrArr.get(i));
		item.put("huntID", huntID);
		item.saveInBackground();
	    }
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
			startActivity(new Intent(CreateHuntActivity.this,
				PlayersActivity.class));
		    }
		});

	findViewById(R.id.add_items).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		Intent i = new Intent(CreateHuntActivity.this,
			ItemsActivity.class);
		startActivity(i);
		// startActivityForResult(i, ITEMS_CODE);
	    }
	});

	findViewById(R.id.createhuntButton_CreateHunt).setOnClickListener(
		new OnClickListener() {
		    public void onClick(View v) {
			doCreateHunt();
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
