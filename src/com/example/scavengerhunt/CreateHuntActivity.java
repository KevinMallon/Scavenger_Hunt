package com.example.scavengerhunt;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    private int hours, min;
    private EditText titleEditText;
    private String title;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_hunt);
	setupButtonCallbacks();

	ParseACL defaultACL = new ParseACL();
	defaultACL.setPublicReadAccess(true);
	ParseACL.setDefaultACL(defaultACL, true);

	startDateDisplay = (TextView) findViewById(R.id.tvDate01);
	pickStartDate = (Button) findViewById(R.id.pickDateButton01);

	pickStartDate.setOnClickListener(new OnClickListener() {

	    /*
	     * private String getTitleInput() { return
	     * getTitleInput(R.id.textbox_Title); }
	     */

	    @Override
	    public void onClick(View v) {
		showDialog(START_DATE_DIALOG_ID);
	    }

	});

	final Calendar cal1 = Calendar.getInstance();
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

	updateStartTime();

	endDateDisplay = (TextView) findViewById(R.id.tvEndDate04);
	pickEndDate = (Button) findViewById(R.id.pickEndDateButton04);

	pickEndDate.setOnClickListener(new OnClickListener() {

	    /*
	     * private String getTitleInput() { return
	     * getTitleInput(R.id.textbox_Title); }
	     */

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

	updateEndTime();

    }

    /*
     * private String getTitleInput() { return getUserInput(R.id.textbox_Title);
     * }
     */

    @Override
    public void onResume() {
	super.onResume();
	ParseUser currentUser = ParseUser.getCurrentUser();
	if (currentUser != null && currentUser.getObjectId() != null) {
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

    /*
     * private String getTitle() { EditText editText = (EditText)
     * findViewById(R.id.submit_button); return getInput(R.id.textbox_Title); }
     */

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
	endDateDisplay.setText(new StringBuilder().append(day).append('-')
		.append(month + 1).append('-').append(year));
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

    // protected Dialog onCreateEndDialog(int id) { switch (id) { case
    // START_DATE_DIALOG_ID: return new DatePickerDialog(this, endDateListener,
    // year, month, day); case START_TIME_DIALOG_ID: return new
    // TimePickerDialog(this, endTimeListener, hours, min, false); } return
    // null; }

    private void setupButtonCallbacks() {
	findViewById(R.id.createhuntButton_CreateHunt).setOnClickListener(
		new OnClickListener() {
		    public void onClick(View v) {
			onResume();
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

    /*
     * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
     * (item.getItemId()) { case R.id.menuitem_prefs: // Intent i = new
     * Intent(mThisActivity, PrefsActivity.class); //
     * mThisActivity.startActivity(i); return true; case R.id.menuitem_logout:
     * ParseUser.logOut(); finish(); return true; default: break; } return
     * false; }
     */

}
