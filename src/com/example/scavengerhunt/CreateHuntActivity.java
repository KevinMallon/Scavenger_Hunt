package com.example.scavengerhunt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("CreateHuntActivity");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_hunt);
	setupButtonCallbacks();
    }

    private void doCompleteHunt() {
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
					CreateHuntActivity.this, ShowHunt.class);
				showhunt.putExtra("HuntID", getHuntID());
				startActivity(showhunt);
			    } else {
				Log.i("ScavengerHuntActivity",
					"game title that is inputted " + arg0);
			    }
			}
		    });
		} else {
		    CharSequence text = "Sorry, the hunt was not updated.";
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
	if (requestCode == 100) {
	    if (resultCode == 1) {
		Bundle bundle = data.getExtras();
		String[] items = (String[]) bundle.getSerializable("items");
		showItems(items);
	    }
	    if (resultCode == 2) {
		Bundle bundle = data.getExtras();
		String[] selectedPlayers = (String[]) bundle
			.getSerializable("players");
		showPlayers(selectedPlayers);
	    }
	}
    }

    private void showPlayers(String[] selectedPlayers) {
	TextView players = (TextView) findViewById(R.id.tv2);
	players.setText(Arrays.toString(selectedPlayers).replaceAll("\\[|\\]",
		""));
    }

    private void showItems(String[] items) {
	TextView players = (TextView) findViewById(R.id.tv1);
	players.setText(Arrays.toString(items).replaceAll("\\[|\\]", ""));
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

    private String getPlayersInput() {
	return getContents(R.id.tv2);
    }

    private String getUserInput(int id) {
	EditText input = (EditText) findViewById(id);
	return input.getText().toString();
    }

    private String getContents(int id) {
	TextView input = (TextView) findViewById(id);
	return input.getText().toString();
    }

    private void setupButtonCallbacks() {

	findViewById(R.id.select_players).setOnClickListener(
		new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			Intent i = new Intent(CreateHuntActivity.this,
				PlayersActivity.class);
			i.putExtra("huntID", getHuntID());
			startActivityForResult(i, 100);
		    }
		});

	findViewById(R.id.add_items).setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(CreateHuntActivity.this,
			AddItemsActivity.class);
		i.putExtra("huntID", getHuntID());
		i.putExtra("from", "create");
		startActivityForResult(i, 100);
	    }
	});

	findViewById(R.id.createHuntButton).setOnClickListener(
		new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			doCompleteHunt();
		    }
		});

	findViewById(R.id.cancelHuntButton).setOnClickListener(
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
