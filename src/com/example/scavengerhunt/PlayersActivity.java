package com.example.scavengerhunt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PlayersActivity extends Activity implements OnClickListener {
    ListView listView;
    Button button;

    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.players_activity);

	// ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
	// query.selectKeys(Arrays.asList("playerName", "score"));;
	// List<ParseObject> results = query.find();

	// List<ParseQuery<ParseUser>> parseUserQueryList = new
	// ArrayList<ParseQuery<ParseUser>>();

	ParseQuery<ParseUser> parseUsernameQuery = ParseUser.getQuery();
	parseUsernameQuery.selectKeys(Arrays.asList("username"));
	try {
	    List<ParseUser> usernameListObject = parseUsernameQuery.find();
	    System.out.println(usernameListObject.size());
	    List<String> values = new ArrayList<String>();

	    for (ParseObject obj : usernameListObject) {
		values.add((String) obj.get("username"));
	    }
	    listView = (ListView) findViewById(R.id.list);
	    button = (Button) findViewById(R.id.testbutton);

	    // Bind array strings into an adapter
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		    android.R.layout.simple_list_item_multiple_choice, values);
	    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    listView.setAdapter(adapter);

	    button.setOnClickListener(this);
	} catch (ParseException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

    }

    @Override
    public void onClick(View arg0) {
	// TODO Auto-generated method stub

    }
}
// ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(
// this, "User");
// adapter.setTextKey("username");
//
// ListView listView = (ListView) findViewById(R.id.listview);
// listView.setAdapter(adapter);
// }
// }
// private static EditText _playerText = null;
// private static Button _playerAdd = null;
// private static final ArrayList<String> _playerListContents = new
// ArrayList<String>();
//
// _playerListContents.add("Salt");
// _playerListContents.add("Pepper");
//
// _playerText = (EditText) findViewById(R.id.player_text);
// _playerAdd = (Button) findViewById(R.id.player_button);
// _playerAdd.setOnClickListener(this);
//
// ArrayAdapter adapter = new ArrayAdapter<String>(this,
// android.R.layout.activity_list_item, _playerListContents);
// ListView listView = (ListView) findViewById(R.id.listview);
// listView.setAdapter(adapter);

// class ListView extends BaseAdapter {
// private LayoutInflater playerInflater;
//
// public ListView(Context context) {
// playerInflater = LayoutInflater.from(context);
// }
//
// public int getCount() {
// return _playerListContents.size();
// }
//
// public Object getItem(int position) {
// return position;
// }
//
// public long getItemId(int position) {
// return position;
// }
//
// public View getView(int position, View view, ViewGroup group) {
//
// ListContent contents;
//
// if (view == null) {
// view = playerInflater.inflate(
// R.layout.players_activity_inflate, null);
// contents = new ListContent();
// contents.text = (EditText) view
// .findViewById(R.id.player_first);
// // contents.text.setCompoundDrawables(
// // view.getResources().getDrawable(R.drawable.arrow_black),
// // null, null, null );
// view.setTag(contents);
// } else {
// contents = (ListContent) view.getTag();
// }
//
// contents.text.setText(_playerListContents.get(position));
// return view;
// }
//
// class ListContent {
// TextView text;
// }
// }

// public onClick(View v) {
//
// String PlayerText;
// if( v == _playerAdd )
// {
// PlayerText = _playerText.getText().toString();
// if(PlayerText.contains( "/" ))
// invalidCharacterToast("/");
// else
// _playerListContents.add( _playerText.getText().toString() );
// setListAdapter( new ListViewAdapter(this) );
// }
// }
// public void invalidCharacterToast(String character)
// {
// TextView displayView = new TextView(this);
// displayView.setBackgroundColor(Color.DKGRAY);
// displayView.setTextColor(Color.RED);
// displayView.setPadding(10,10,10,10);
// displayView.setText(character + " is an invalid character");
//
// Toast theToast = new Toast(this);
// theToast.setView(displayView);
// theToast.setDuration(Toast.LENGTH_LONG);
// theToast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
// theToast.show();
// }

// // Locate the ListView in listview_main.xml
// list = (ListView) findViewById(R.id.listview);
//
// // Pass results to ListViewAdapter Class
// list = new ListView(this);
// // Binds the Adapter to the ListView
// list.setAdapter(adapter);
// // Capture ListView item click
// list.setOnItemClickListener(new OnItemClickListener() {
//
// @Override
// public void onItemClick(AdapterView<?> parent, View view,
// int position, long id) {
// Intent i = new Intent(PlayersActivity.this,
// SinglePlayerView.class);
// // Pass all data flag
// i.putExtra("colour", colours);
// // Pass a single position
// i.putExtra("position", position);
// // Open SingleItemView.java Activity
// startActivity(i);
// }
//
// });

//
// }

// @Override
// public void onClick(View view) {
// switch(view.getId()) {
// case R.id.select_colours:
// showSelectColoursDialog();
// break;
//
// default:
// break;
// }
// }
//
// protected void onChangeSelectedColours() {
// StringBuilder stringBuilder = new StringBuilder();
//
// for(CharSequence colour : selectedColours)
// stringBuilder.append(colour + ",");
//
// selectColoursButton.setText(stringBuilder.toString());
// }
//
// protected void showSelectColoursDialog() {
// boolean[] checkedColours = new boolean[colours.length];
// int count = colours.length;
//
// for(int i = 0; i < count; i++)
// checkedColours[i] = selectedColours.contains(colours[i]);
//
// DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new
// DialogInterface.OnMultiChoiceClickListener() {
// @Override
// public void onClick(DialogInterface dialog, int which, boolean isChecked) {
// if(isChecked)
// selectedColours.add(colours[which]);
// else
// selectedColours.remove(colours[which]);
//
// onChangeSelectedColours();
// }
// };
//
// AlertDialog.Builder builder = new AlertDialog.Builder(this);
// builder.setTitle("Select Colours");
// builder.setMultiChoiceItems(colours, checkedColours, coloursDialogListener);
//
// AlertDialog dialog = builder.create();
// dialog.show();
// }
