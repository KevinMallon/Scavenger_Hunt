package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InvitationsActivity extends Activity {

    ListView listView;
    final String currentUser = (String) ParseUser.getCurrentUser().get(
	    "username");
    InvitationListViewAdapter adapter;
    Format formatter = new SimpleDateFormat("MM-dd-yy, h:mm a");
    private List<Invitation> invitatationlist = null;
    ArrayList<Invitation> invitationArray = new ArrayList<Invitation>();

    // final ArrayList<Invitation> titles = new ArrayList<Invitation>();
    // final ArrayList<Invitation> IDs = new ArrayList<Invitation>();
    // final ArrayList<Invitation> beginDates = new ArrayList<Invitation>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.invitation_listview);
	// setupButtonCallbacks();
	ParseQuery<ParseObject> huntsquery = ParseQuery.getQuery("Hunt");
	try {
	    List<ParseObject> huntsListObject = huntsquery.find();

	    for (ParseObject obj : huntsListObject) {
		System.out.println("huntPlayers : "
			+ ((ArrayList<Invitation>) obj.get("huntPlayers")));
		if (((ArrayList<Invitation>) obj.get("huntPlayers"))
			.contains(currentUser)) {
		    Invitation invitation = new Invitation();
		    String huntId = obj.getObjectId();
		    System.out.println("huntId : " + huntId);
		    invitation.setHuntId(huntId);
		    invitation.setTitle(obj.getString("title"));
		    // invitation.setBeginDate((Date)
		    // obj.get("start_datetime"));
		    System.out.println("invitation : " + invitation);

		    invitationArray.add(invitation);
		    System.out.println("invitationArray : " + invitationArray
			    + invitationArray.size());
		    listView = (ListView) findViewById(R.id.list);

		    // adapter = new InvitationListViewAdapter(
		    // InvitationsActivity.this,
		    // R.layout.invitation_listview, invitationArray);

		    listView.setAdapter(adapter);
		    // Invitation invitation = new Invitation(title,
		    // setHuntId(obj.getObjectId()), null);
		    // invitationArray.add(new Invitation(obj.get("title")));
		    //
		    //
		    // Invitation.setHuntId(obj.getObjectId());
		    // Invitation.setTitle(obj.getString("title"));
		    //
		    // invitationArray.add(Invitation);

		    // IDs.add(obj.getObjectId());
		    // beginDates.add((String) formatter.format(obj
		    // .get("start_datetime")));
		}

	    }
	    // String[] titlesArray = titles.toArray(new String[titles.size()]);
	    // String[] IDsArray = IDs.toArray(new String[IDs.size()]);
	    // String[] beginDatesArray = beginDates.toArray(new
	    // String[beginDates
	    // .size()]);
	    // System.out.println("AllHunts : " + titles);

	    /**
	     * get on item click listener
	     */
	    listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v,
			final int position, long id) {
		    Log.i("List View Clicked", "**********");
		    Toast.makeText(InvitationsActivity.this,
			    "List View Clicked:" + position, Toast.LENGTH_LONG)
			    .show();
		}
	    });
	} catch (ParseException e1) {
	    e1.printStackTrace();
	}

    }

    // private void setupButtonCallbacks() {
    // final Button buttonRefuse = (Button) findViewById(R.id.buttonRefuse);
    // buttonRefuse.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // remove(v);
    // }
    //
    // });
    //
    // }

}

// package com.example.scavengerhunt;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import android.app.Activity;
// import android.content.Context;
// import android.os.Bundle;
// import android.util.Log;
// import android.widget.ListView;
// import android.widget.Toast;
//
// import com.parse.FindCallback;
// import com.parse.GetCallback;
// import com.parse.ParseException;
// import com.parse.ParseObject;
// import com.parse.ParseQuery;
// import com.parse.ParseUser;
//
// public class InvitationsActivity extends Activity {
// // ListView listview;
// // List<ParseObject> obj;
// // ProgressDialog mProgressDialog;
// InvitationListViewAdapter adapter;
// private List<Invitation> notificationlist = null;
// // Date historyDate;
// // Date futureDate;
// Context context;
// String huntObjectId;
// String huntTitle;
// final String currentUser = (String) ParseUser.getCurrentUser().get(
// "username");
// ListView listview;
// // ArrayAdapter<String> adapter;
// List<String> Ids;
// List<String> huntTitles;
//
// Invitation notification;
//
// @Override
// public void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// // Get the view from listview_main.xml
//
// setContentView(R.layout.notification_listview);
// // setContentView(R.layout.all_hunts);
// getInvitations();
// }
//
// private void getInvitations() {
// // TODO Auto-generated method stub
// notificationlist = new ArrayList<Invitation>();
// ParseQuery<ParseObject> query = ParseQuery.getQuery("notification");
// query.whereEqualTo("invitedUser", currentUser);
// Log.i("scavenger Hunt", "notifications for " + currentUser);
// query.findInBackground(new FindCallback<ParseObject>() {
// public void done(List<ParseObject> objects, ParseException e) {
// if (e == null) {
// // int i = 0;
// Log.d("scavenger", "Retrieved " + objects.size() + " hunts");
// // String[] huntIds = new String[objects.size()];
// for (ParseObject obj : objects) {
// huntObjectId = obj.getString("huntId");
// Log.i("scavenger ", "need to see object id" + "<"
// + huntObjectId + ">");
// ParseQuery<ParseObject> query = ParseQuery
// .getQuery("Hunt");
// query.whereEqualTo("objectId", huntObjectId);
// query.getFirstInBackground(new GetCallback<ParseObject>() {
// public void done(ParseObject object,
// ParseException e) {
// if (object == null) {
// Log.d("Game",
// "The game had an invalid objectid and wasn't found");
// Toast.makeText(
// getApplicationContext(),
// " Sorry, There are no games available at this time! Try back later!!",
// Toast.LENGTH_LONG).show();
// finish();
//
// } else {
// Log.d("Viola", "There is a game");
// // historyDate =
// // object.getDate("beginDate");
// // futureDate = object.getDate("endDate");
// // if (myDate.after(historyDate) &&
// // myDate.before(futureDate)){
// Invitation notification = new Invitation();
// String huntId = object.getObjectId();
// System.out.println("huntId : " + huntId);
// notification.setHuntId(huntId);
// notification.setTitle(object
// .getString("title"));
// System.out.println("notification : "
// + notification);
//
// notificationlist.add(notification);
// System.out.println("notificationlist : "
// + notificationlist);
// listview = (ListView) findViewById(R.id.listview);
// // Pass the results into
// // ListViewAdapter.java
// // adapter = new InvitationListViewAdapter(
// // InvitationsActivity.this,
// // notificationlist);
// // Binds the Adapter to the ListView
// listview.setAdapter(adapter);
// // }
// }
//
// }
//
// });
//
// }
//
// }
//
// }
//
// });
// // return null;
// }

// huntObjectId = obj.getString("huntId");
// Log.i("scavenger ", "need to see object id" + "<"
// + huntObjectId + ">");
// huntIds[i] = huntObjectId;
// i++;
// }
// System.out.println("Show huntIDs " + huntIds);
// getHunts(huntIds);
//
// } else {
// Log.i("InvitationsActivity", "Error finding hunts " + e);
// }
// }
//
// });
//
// }

// public void getHunts(String[] huntIds) {
// for (String huntId : huntIds) {
// ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
// query.whereEqualTo("objectId", huntId);
// System.out.println("Show huntId " + huntId);
// query.getFirstInBackground(new GetCallback<ParseObject>() {
// public void done(ParseObject hunt, ParseException e) {
// if (e == null) {
// Log.d("Viola", "There is a game");
// // historyDate =
// // object.getDate("beginDate");
// // futureDate = object.getDate("endDate");
// // if (myDate.after(historyDate) &&
// // myDate.before(futureDate)){
//
// } else {
// Log.d("Game",
// "The game had an invalid objectid and wasn't found");
// Toast.makeText(
// getApplicationContext(),
// " Sorry, There are no games available at this time! Try back later!!",
// Toast.LENGTH_LONG).show();
// finish();
// }
//
// }
//
// });
// }
//
// System.out.println("notificationList : " + notificationlist);
// setList(notificationlist);
// }

// public void setList(List<Invitation> notificationList) {
// System.out.println("AllHunts : " + notificationList);
// listview = (ListView) findViewById(R.id.list);
// // listView = (ListView) findViewById(R.id.list);
//
// // adapter = new ArrayAdapter<String>(this, R.layout.small_list,
// // huntTitles);
// adapter = new InvitationListViewAdapter(InvitationsActivity.this,
// notificationList);
// listview.setAdapter(adapter);
// }
// }

// Invitation notification = new Invitation();
// notification.setHuntId(huntObjectId);
// Log.d("setHuntId", huntObjectId);
// notification.setTitle(hunts
// .getString("title"));
// Log.d("setTitle", hunts.getString("title"));
// notificationList.add(notification);
//
// listview = (ListView) findViewById(R.id.listview);
// // Pass the results into
// // ListViewAdapter.java
// adapter = new InvitationListViewAdapter(
// InvitationsActivity.this,
// notificationList);
// // Binds the Adapter to the ListView
// listview.setAdapter(adapter);

// package com.example.scavengerhunt;
//
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
//
// import android.app.Activity;
// import android.app.ProgressDialog;
// import android.content.Context;
// import android.os.Bundle;
// import android.util.Log;
// import android.widget.ListView;
// import android.widget.Toast;
//
// import com.parse.FindCallback;
// import com.parse.GetCallback;
// import com.parse.ParseException;
// import com.parse.ParseObject;
// import com.parse.ParseQuery;
// import com.parse.ParseUser;
//
// public class InvitationsActivity extends Activity {
// ListView listview;
// List<ParseObject> obj;
// ProgressDialog mProgressDialog;
// InvitationListViewAdapter adapter;
// private List<Invitation> notificationList = null;
// Date createdDate;
// Date endDate;
// Context context;
// ParseUser currentUser;
// String huntObjectId;
//
// @Override
// public void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// // Get the view from listview_main.xml
// setContentView(R.layout.notification_listview);
// // final Date myDate = new Date();
// currentUser = ParseUser.getCurrentUser();
// notificationList = new ArrayList<Invitation>();
//
// ParseQuery<ParseObject> query = ParseQuery.getQuery("notification");
// query.whereEqualTo("invitedUser", currentUser.get("username"));
// Log.i("scavenger Hunt", "invite activity user " + currentUser);
// query.findInBackground(new FindCallback<ParseObject>() {
// public void done(List<ParseObject> invitedHunts, ParseException e) {
// Log.i("scavenger Hunt", "in parse query2 getting list of hunts");
// if (e == null) {
// Log.d("hunts", "Retrieved " + invitedHunts.size()
// + " hunts");
//
// for (ParseObject hunt : invitedHunts) {
// huntObjectId = hunt.getString("huntId");
// Log.i("scavenger ", "hunt id " + "<" + huntObjectId
// + ">");
// ParseQuery<ParseObject> query = ParseQuery
// .getQuery("Hunt");
// query.getInBackground(huntObjectId,
// new GetCallback<ParseObject>() {
// public void done(ParseObject object,
// ParseException e) {
// if (object == null) {
// Log.d("Hunt",
// "The hunt had an invalid objectid and wasn't found");
// Toast.makeText(
// getApplicationContext(),
// " Sorry, There are no hunts available at this time! Try back later!!",
// Toast.LENGTH_LONG).show();
// finish();
//
// } else {
// Log.d("Viola", "There is a hunt "
// + object.getObjectId());
// // createdDate =
// // object.getDate("beginDate");
// // endDate =
// // object.getDate("endDate");
// // if (myDate.after(createdDate)
// // && myDate.before(endDate)) {
// Invitation notification = new Invitation();
// notification
// .setHuntId(huntObjectId);
// Log.d("setHuntId", huntObjectId);
// notification.setTitle(object
// .getString("title"));
// Log.d("setTitle",
// object.getString("title"));
// notificationList.add(notification);
//
// // }
// }
// listview = (ListView) findViewById(R.id.listview);
// Log.i("scavenger ", "notificationList "
// + "<" + notificationList.size()
// + ">");
// // Pass the results into
// // ListViewAdapter.java
// adapter = new InvitationListViewAdapter(
// InvitationsActivity.this,
// notificationList);
// // Binds the Adapter to the ListView
// listview.setAdapter(adapter);
// }
//
// });
//
// }
//
// }
//
// }
//
// });
// // return null;
// }
// }

// OLD ALL HUNTS
// ListView listView;
// ArrayAdapter<String> adapter;
//
// @Override
// public void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.invitation_listview);
//
// ParseQuery<ParseObject> huntsquery = ParseQuery.getQuery("Hunt");
// try {
// List<ParseObject> huntsListObject = huntsquery.find();
// List<String> titles = new ArrayList<String>();
// final List<String> IDs = new ArrayList<String>();
//
// for (ParseObject obj : huntsListObject) {
// titles.add((String) obj.get("title"));
// IDs.add(obj.getObjectId());
// }
// System.out.println("AllHunts : " + titles);
// listView = (ListView) findViewById(R.id.list);
//
// adapter = new ArrayAdapter<String>(this, R.layout.small_list,
// titles);
//
// listView.setAdapter(adapter);
// listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
// @Override
// public void onItemClick(AdapterView<?> parent, final View view,
// int position, long id) {
// String ID = IDs.get(position);
//
// Intent i = new Intent(PlayingHuntsActivity.this, ShowHunt.class);
// i.putExtra("HuntID", ID);
// startActivity(i);
// }
// });
// } catch (ParseException e1) {
// e1.printStackTrace();
// }
// }

