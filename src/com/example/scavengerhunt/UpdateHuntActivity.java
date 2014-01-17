package com.example.scavengerhunt;

import java.text.Format;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UpdateHuntActivity extends Activity {
    static final int REQUEST_CODE = 100;
    static final int ITEMS_RESULT_CODE = 1;
    static final int PLAYERS_RESULT_CODE = 2;
    static final int END_TIME_DIALOG_ID = 4;
    public Button selectPlayersButton;
    public Button addItemsButton;
    public Button EditHuntButton_EditHunt;
    public String startDate;
    public String startTime;
    ListView listView;
    ArrayAdapter<String> itemsAdapter;
    ArrayAdapter<String> playersAdapter;
    public ArrayList<String> itemStrArr;
    final Format dateformatter = new SimpleDateFormat("MM-dd-yyyy");
    final SimpleDateFormat timeformatter = new SimpleDateFormat("h:mm a",
	    Locale.US);

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.update_hunt);
	doPopulateHunt();
	setupButtonCallbacks();
    }

    private void doPopulateHunt() {
	final EditText textbox_Title = (EditText) findViewById(R.id.textbox_Title);
	final EditText editStartDate = (EditText) findViewById(R.id.editStartDate);
	final EditText editStartTime = (EditText) findViewById(R.id.editStartTime);
	final EditText editEndDate = (EditText) findViewById(R.id.editEndDate);
	final EditText editEndTime = (EditText) findViewById(R.id.editEndTime);
	ParseQuery<ParseObject> huntquery = ParseQuery.getQuery("Hunt");
	huntquery.whereEqualTo("objectId", getHuntID());
	huntquery.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    @Override
	    public void done(ParseObject object, com.parse.ParseException e) {
		if (e == null) {

		    final String title = (String) object.get("title");
		    textbox_Title.setText(title);

		    editStartDate.setText(formatDate(object
			    .get("start_datetime")));
		    editStartTime.setText(formatTime(object
			    .get("start_datetime")));
		    editEndDate.setText(formatDate(object.get("end_datetime")));
		    editEndTime.setText(formatTime(object.get("end_datetime")));

		    final List<String> items = object.getList("huntItems");
		    getRemainingPlayers();
		    setItemsView(items);

		} else {
		    Log.d("ShowHunt",
			    "ParseObject retrieval error: "
				    + Log.getStackTraceString(e));
		}
	    }
	});
    }

    private String formatDate(Object date) {
	String formattedDate = dateformatter.format(date);
	return formattedDate;
    }

    private String formatTime(Object time) {
	String formattedTime = timeformatter.format(time);
	return formattedTime;
    }

    private final String getHuntID() {
	String huntID = getIntent().getStringExtra("UpdateHuntActivity");
	return huntID;
    }

    private final List<String> getRemainingPlayers() {
	Bundle bundle = getIntent().getExtras();
	String[] players = (String[]) bundle.getSerializable("players");
	List<String> playersList = Arrays.asList(players);
	setPlayersView(playersList);
	return playersList;
    }

    private void setItemsView(List<String> items) {
	listView = (ListView) findViewById(R.id.listViewItems);
	itemsAdapter = new ArrayAdapter<String>(UpdateHuntActivity.this,
		R.layout.small_list, items);
	listView.setAdapter(itemsAdapter);
    }

    private void setPlayersView(List<String> players) {
	listView = (ListView) findViewById(R.id.listViewPlayers);
	playersAdapter = new ArrayAdapter<String>(UpdateHuntActivity.this,
		R.layout.small_list, players);
	listView.setAdapter(playersAdapter);
    }

    private void doUpdateHunt() {
	final ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	final Context context = this;
	query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    @Override
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		if (e == null) {
		    hunt.put("title", getHuntTitleInput());
		    hunt.put("start_datetime", getStartDateTime());
		    hunt.put("end_datetime", getEndDateTime());
		    hunt.saveInBackground(new SaveCallback() {
			@Override
			public void done(com.parse.ParseException arg0) {
			    if (arg0 == null) {

				Intent showhunt = new Intent(
					UpdateHuntActivity.this, MyHunt.class);
				showhunt.putExtra("HuntID", getHuntID());
				startActivity(showhunt);
			    } else {
				Log.i("ScavengerHuntActivity",
					"game title that is inputted " + arg0);
			    }
			}
		    });
		} else {
		    String text = "Sorry, the hunt was not updated.";
		    int duration = Toast.LENGTH_SHORT;
		    Toast.makeText(context, text, duration).show();
		    finish();
		}

	    }

	});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == REQUEST_CODE) {
	    if (resultCode == ITEMS_RESULT_CODE) {
		ParseQuery<ParseObject> huntquery = ParseQuery.getQuery("Hunt");
		huntquery.whereEqualTo("objectId", getHuntID());
		huntquery.getInBackground(getHuntID(),
			new GetCallback<ParseObject>() {
			    @Override
			    public void done(ParseObject object,
				    com.parse.ParseException e) {
				if (e == null) {
				    List<String> items = object
					    .getList("huntItems");
				    setItemsView(items);
				}
			    }
			});
	    }
	    if (resultCode == PLAYERS_RESULT_CODE) {
		Bundle bundle = data.getExtras();
		String[] players = (String[]) bundle.getSerializable("players");
		List<String> playersList = Arrays.asList(players);
		setPlayersView(playersList);
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

    private String getItemsCount() {
	listView = (ListView) findViewById(R.id.listViewItems);
	ListAdapter itemsAdapter = listView.getAdapter();
	int itemsCount = itemsAdapter.getCount();
	String hasItems = "no";
	if (itemsCount > 0) {
	    hasItems = "yes";
	}
	return hasItems;
    }

    private String[] getPlayersArray() {
	listView = (ListView) findViewById(R.id.listViewPlayers);
	ListAdapter playersAdapter = listView.getAdapter();
	String[] playersArray = new String[playersAdapter.getCount()];
	for (int i = 0; i < playersAdapter.getCount(); i++) {
	    playersArray[i] = (playersAdapter.getItem(i)).toString();
	}
	return playersArray;
    }

    private String getUserInput(int id) {
	EditText input = (EditText) findViewById(id);
	return input.getText().toString();
    }

    private void setupButtonCallbacks() {
	findViewById(R.id.add_players).setOnClickListener(
		new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			Intent i = new Intent(UpdateHuntActivity.this,
				PlayersActivity.class);
			i.putExtra("huntID", getHuntID());
			i.putExtra("currentPlayers",
				Arrays.toString(getPlayersArray()));
			startActivityForResult(i, REQUEST_CODE);
		    }
		});
	findViewById(R.id.add_items).setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(UpdateHuntActivity.this,
			AddItemsActivity.class);
		i.putExtra("huntID", getHuntID());
		i.putExtra("hasItems", getItemsCount());
		System.out.println("hasItems intent " + getItemsCount());
		startActivityForResult(i, REQUEST_CODE);
	    }
	});
	findViewById(R.id.delete_items).setOnClickListener(
		new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			Intent i = new Intent(UpdateHuntActivity.this,
				DeleteItemsActivity.class);
			i.putExtra("huntID", getHuntID());
			startActivityForResult(i, REQUEST_CODE);
		    }
		});
	findViewById(R.id.UpdateHuntButton).setOnClickListener(
		new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			doUpdateHunt();
		    }
		});

	findViewById(R.id.UpdateHuntButton_cancel).setOnClickListener(
		new OnClickListener() {
		    @Override
		    public final void onClick(View v) {
			ParseQuery<ParseObject> huntquery = ParseQuery
				.getQuery("Hunt");
			huntquery.whereEqualTo("huntID", getHuntID());

			huntquery.getInBackground(getHuntID(),
				new GetCallback<ParseObject>() {
				    @Override
				    public void done(ParseObject hunt,
					    com.parse.ParseException e) {
					if (e == null) {
					    hunt.deleteInBackground(new DeleteCallback() {
						@Override
						public void done(
							com.parse.ParseException e) {
						    if (e == null) {

						    }
						}
					    });
					    finish();
					}
				    }
				});
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
