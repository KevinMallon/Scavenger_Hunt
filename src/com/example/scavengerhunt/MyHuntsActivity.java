package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MyHuntsActivity extends Activity {

    ListView listView;
    final String currentUsername = (String) ParseUser.getCurrentUser().get(
	    "username");
    final ParseUser currentUser = ParseUser.getCurrentUser();
    MyHuntsListViewAdapter adapter;
    Format formatter = new SimpleDateFormat("MM-dd-yy, h:mm a");
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> IDs = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.all_hunts);
	ParseQuery<ParseObject> huntsquery = ParseQuery.getQuery("Hunt");
	huntsquery.whereEqualTo("owner", currentUsername);
	try {
	    List<ParseObject> huntsListObject = huntsquery.find();

	    for (ParseObject obj : huntsListObject) {
		titles.add((String) obj.get("title"));
		IDs.add(obj.getObjectId());
	    }

	    listView = (ListView) findViewById(R.id.list);

	    adapter = new MyHuntsListViewAdapter(MyHuntsActivity.this, titles,
		    IDs);

	    listView.setAdapter(adapter);
	    if (adapter.getCount() == 0) {
		Toast.makeText(getApplicationContext(),
			" You haven't created any scavenger hunts yet.",
			Toast.LENGTH_LONG).show();
		finish();
	    }
	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		    final String huntId = IDs.get(position);
		    final Intent intent = new Intent(MyHuntsActivity.this,
			    MyHunt.class);
		    intent.putExtra("HuntID", huntId);
		    Log.d("huntId", "hunt id is " + huntId);
		    startActivity(intent);
		}
	    });

	} catch (ParseException e1) {
	    e1.printStackTrace();
	}
    }

}
