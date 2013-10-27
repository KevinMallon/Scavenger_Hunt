package com.example.scavengerhunt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CreateHuntActivity extends Activity {

    static final int START_DATE_DIALOG_ID = 1;
    static final int START_TIME_DIALOG_ID = 2;
    static final int END_DATE_DIALOG_ID = 3;
    static final int END_TIME_DIALOG_ID = 4;
    public Button selectPlayersButton;
    public Button addItemsButton;
    private TextView startDateDisplay;
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

    ListView listview;
    List<ParseObject> ob;
    private String[] myPlayerStringArray;

    // ProgressDialog mProgressDialog;
    // ArrayAdapter<String> adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_hunt);
	setupButtonCallbacks();

	ParseACL defaultACL = new ParseACL();
	defaultACL.setPublicReadAccess(true);
	ParseACL.setDefaultACL(defaultACL, true);

	selectPlayersButton = (Button) findViewById(R.id.select_players);
	addItemsButton = (Button) findViewById(R.id.add_items);
	startDateDisplay = (TextView) findViewById(R.id.tvDate01);
	pickStartDate = (Button) findViewById(R.id.pickDateButton01);

	pickStartDate.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showDialog(START_DATE_DIALOG_ID);
	    }

	});

	final Calendar cal1 = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat(
		"EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
	System.out.println(dateFormat.format(cal1.getTime()));
	year = cal1.get(Calendar.YEAR);
	month = cal1.get(Calendar.MONTH);
	day = cal1.get(Calendar.DAY_OF_MONTH);

	updateStartDate();

	startTimeDisplay = (TextView) findViewById(R.id.tvTime02);
	pickStartTime = (Button) findViewById(R.id.pickTimeButton02);

	pickStartTime.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showDialog(START_TIME_DIALOG_ID);

	    }

	});

	hours = cal1.get(Calendar.HOUR);
	min = cal1.get(Calendar.MINUTE);
	ampm = cal1.get(Calendar.AM_PM);
	updateStartTime();

	endDateDisplay = (TextView) findViewById(R.id.tvEndDate04);
	pickEndDate = (Button) findViewById(R.id.pickEndDateButton04);

	pickEndDate.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showDialog(END_DATE_DIALOG_ID);
	    }

	});

	final Calendar cal2 = Calendar.getInstance();
	year = cal2.get(Calendar.YEAR);
	month = cal2.get(Calendar.MONTH);
	day = cal2.get(Calendar.DAY_OF_MONTH);

	updateEndDate();

	endTimeDisplay = (TextView) findViewById(R.id.tvEndTime03);
	pickEndTime = (Button) findViewById(R.id.pickEndTimeButton03);

	pickEndTime.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showDialog(END_TIME_DIALOG_ID);

	    }

	});

	hours = cal2.get(Calendar.HOUR);
	min = cal2.get(Calendar.MINUTE);
	ampm = cal2.get(Calendar.AM_PM);

	updateEndTime();

    }

    private void doCreateHunt() {
	ParseUser currentUser = ParseUser.getCurrentUser();
	if (currentUser != null && currentUser.getObjectId() != null
		&& getHuntTitleInput() != "") {
	    final String username = currentUser.getUsername();
	    /*
	     * EditText titleEditText = (EditText)
	     * findViewById(R.id.textbox_Title); titleEditText.setText(title);
	     */
	    ParseObject hunt = new ParseObject("Hunt");
	    hunt.put("title", getHuntTitleInput());
	    hunt.put("startDate", startDate);
	    hunt.put("endDate", endDate);
	    hunt.put("startTime", startTime);
	    hunt.put("endTime", endTime);
	    hunt.put("owner", username);
	    hunt.saveInBackground();
	}
    }

    // private void queryForUser() {
    // List<ParseQuery<ParseUser>> parseUserQueryList = new
    // ArrayList<ParseQuery<ParseUser>>();
    // ParseQuery<ParseUser> parseUsernameQuery = ParseUser.getQuery();
    // List<ParseUser> usernames = parseUsernameQuery.find();
    // }
    // parseUsernameQuery.findInBackground();

    // ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
    // query.findInBackground(new FindCallback<>()
    //
    //
    // ParseQuery<User> query = ParseQuery.getQuery(User.class);
    // query.findInBackground(new FindCallback<>();
    //
    // query.selectKeys(Arrays.asList("userName"));
    // List<ParseUser> usernames = query.find();

    // ArrayAdapter adapter = new ArrayAdapter<String>(this,
    // android.R.layout.simple_list_item_multiple_choice, myPlayerStringArray);
    //
    // ListView listView = (ListView) findViewById(R.id.textView1);
    // listView.setAdapter(adapter);
    //
    // ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
    // query.selectKeys(userName);
    // List<ParseObject> results = query.find();
    //
    // List<ParseQuery> parseUserQueryList = new ArrayList<ParseQuery>();
    // final ParseQuery parseUsernameQuery = ParseUser.getQuery();
    //
    // protected void showSelectPlayersDialog() {
    // // List<ParseQuery<ParseUser>> parseUserQueryList = new
    // ArrayList<ParseQuery<ParseUser>>();
    // // ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
    // List<ParseQuery> parseUserQueryList = new ArrayList<ParseQuery>();
    // final ParseQuery parseUsernameQuery = ParseUser.getQuery("username");
    // String playerName = ParseUser.getString("playerName");
    // ParseQuery<ParseUser> parseUsernameQuery = ParseUser.getQuery();
    // Array usernames = parseUsernameQuery.find();
    // boolean[] checkedPlayers = new boolean[usernames.length];
    // int count = usernames.length;
    //
    // String selectedPlayers;
    // for(int i = 0; i < count; i++)
    // checkedPlayers[i] = selectedPlayers.contains(usernames[i]);

    // return builder.create();
    // }

    private String getHuntTitleInput() {
	return getUserInput(R.id.textbox_Title);
    }

    private String getStartDateInput() {
	return getUserInput(R.id.tvDate01);
    }

    private String getStartTimeInput() {
	return getUserInput(R.id.tvTime02);
    }

    private String getEndDateInput() {
	return getUserInput(R.id.tvEndDate04);
    }

    private String getEndTimeInput() {
	return getUserInput(R.id.tvEndTime03);
    }

    private String getUserInput(int id) {
	EditText input = (EditText) findViewById(id);
	return input.getText().toString();
    }

    public void updateStartTime() {
	startTimeDisplay.setText(new StringBuilder().append(hours).append(':')
		.append(min));
	startTime = (new StringBuilder().append(hours).append(':').append(min))
		.toString();

    }

    public void updateStartDate() {
	startDateDisplay.setText(new StringBuilder().append(month + 1)
		.append('-').append(day).append('-').append(year));
	startDate = (new StringBuilder().append(month + 1).append('-')
		.append(day).append('-').append(year)).toString();

    }

    private DatePickerDialog.OnDateSetListener StartDateListener = new DatePickerDialog.OnDateSetListener() {

	@Override
	public void onDateSet(DatePicker view, int yr, int monthOfYear,
		int dayOfMonth) {
	    year = yr;
	    month = monthOfYear;
	    day = dayOfMonth;
	    updateStartDate();
	}
    };

    private TimePickerDialog.OnTimeSetListener StartTimeListener = new TimePickerDialog.OnTimeSetListener() {

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	    hours = hourOfDay;
	    min = minute;
	    updateStartTime();
	}

    };

    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case START_DATE_DIALOG_ID:
	    return new DatePickerDialog(this, StartDateListener, year, month,
		    day);
	case START_TIME_DIALOG_ID:
	    return new TimePickerDialog(this, StartTimeListener, hours, min,
		    false);
	case END_DATE_DIALOG_ID:
	    return new DatePickerDialog(this, endDateListener, year, month, day);
	case END_TIME_DIALOG_ID:
	    return new TimePickerDialog(this, endTimeListener, hours, min,
		    false);
	}
	return null;
    }

    public void updateEndTime() {
	endTimeDisplay.setText(new StringBuilder().append(hours).append(':')
		.append(min));
	endTime = (new StringBuilder().append(hours).append(':').append(min))
		.toString();
    }

    public void updateEndDate() {
	endDateDisplay.setText(new StringBuilder().append(month + 1)
		.append('-').append(day).append('-').append(year));
	endDate = (new StringBuilder().append(month + 1).append('-')
		.append(day).append('-').append(year)).toString();
    }

    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

	@Override
	public void onDateSet(DatePicker view, int yr, int monthOfYear,
		int dayOfMonth) {
	    year = yr;
	    month = monthOfYear;
	    day = dayOfMonth;
	    updateEndDate();
	}
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	    hours = hourOfDay;
	    min = minute;
	    updateEndTime();
	}

    };

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
		startActivity(new Intent(CreateHuntActivity.this,
			ItemsActivity.class));
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
