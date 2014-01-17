package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PlayHuntActivity extends Activity {
    final ParseUser currentUser = ParseUser.getCurrentUser();
    final String currentUsername = (String) currentUser.get("username");
    Button declineButton;
    int currentScore;
    List<ParseObject> foundItems;
    ParseObject hunt;
    List<String> players;
    List<String> allItems;
    String[] foundItemsArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.single_hunt_view);
	final TextView tvHuntTitle = (TextView) findViewById(R.id.tvHuntTitle);
	final TextView tvStartTime = (TextView) findViewById(R.id.tvStartTime);
	final TextView tvEndTime = (TextView) findViewById(R.id.tvEndTime);
	final TextView itemsFound = (TextView) findViewById(R.id.tvItemsFound);
	final String huntID = getHuntID();

	ParseQuery<ParseObject> huntquery = ParseQuery.getQuery("Hunt");
	huntquery.whereEqualTo("objectId", huntID);
	huntquery.getInBackground(huntID, new GetCallback<ParseObject>() {
	    @Override
	    public void done(ParseObject object, ParseException e) {
		if (e == null) {
		    hunt = object;
		    final String title = (String) object.get("title");

		    tvHuntTitle.setText(title);
		    final Format formatter = new SimpleDateFormat(
			    "MM-dd-yy, h:mm a");
		    final Date startDatetime = object.getDate("start_datetime");
		    final Date endDatetime = object.getDate("end_datetime");
		    tvStartTime.setText(formatter.format(startDatetime));
		    tvEndTime.setText(formatter.format(endDatetime));
		    players = object.getList("huntPlayers");
		    allItems = object.getList("huntItems");

		    if (new Date().after(endDatetime)) {
			// calculateWinner();
			// gameOverDialog(winner);
			// final String winner = (String) object.get("winner");
		    }
		    if (new Date().after(startDatetime)) {
			setRemainingItemsListView();
		    } else {
			itemsFound
				.setText("Items will be displayed when the hunt begins.");
		    }
		} else {
		    Log.d("PlayHunt",
			    "ParseObject retrieval error: "
				    + Log.getStackTraceString(e));
		}
	    }
	});
	setupButtonCallbacks();
    }

    // private void calculateWinner() {
    // final ParseQuery<ParseObject> query = ParseQuery.getQuery("FoundItem");
    // query.whereEqualTo("hunt", getHuntID());
    // query.whereEqualTo("user", currentUsername);
    // query.findInBackground(new FindCallback<ParseObject>() {
    // public void done(final List<ParseObject> foundItemsList,
    // ParseException e) {
    // if (e == null) {
    // foundItems = foundItemsList;
    // currentScore = foundItems.size();
    // String[] ItemsArray = new String[foundItemsList.size()];
    // int i = 0;
    //
    // for (ParseObject foundItem : foundItemsList) {
    // ItemsArray[i] = (String) foundItem.get("item");
    // i++;
    // }
    // foundItemsArray = ItemsArray;
    // addAllGameItems();
    // showFoundItems();
    // } else {
    // Log.w("Parse Error", "player username retrieval failure");
    // }
    // }
    // });
    // }

    // private void gameOverDialog(final String winner) {
    // Bundle gameover = new Bundle();
    // gameover.putString("winner", winner);
    // final DialogFragment gameOverDialogFragment = new
    // GameOverDialogFragment();
    // gameOverDialogFragment.setArguments(gameover);
    // gameOverDialogFragment.show(getFragmentManager(), "GameOver");
    // }

    private String getHuntID() {
	String huntID = getIntent().getStringExtra("HuntID");
	return huntID;
    }

    private void setRemainingItemsListView() {
	setItemListView();
	getFoundItems();
    }

    private void setItemListView() {
	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		R.layout.small_list);
	final ListView listView = (ListView) findViewById(R.id.listViewItems);
	listView.setAdapter(adapter);
	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, final View view,
		    int position, long id) {
		Log.d("Dialog Values",
			"Position is " + String.valueOf(position)
				+ ". View is " + view.toString() + ". ID is "
				+ String.valueOf(id));
		final String item = (String) parent.getItemAtPosition(position);
		showItemFoundDialog(item);
	    }
	});
    }

    private ArrayList<String> getListViewItems() {
	final ArrayList<String> itemList = new ArrayList<String>();
	final ArrayAdapter<String> adapter = getItemAdapter();
	for (int i = 0; i < (adapter.getCount()); i++) {
	    itemList.add(adapter.getItem(i));
	}
	return itemList;
    }

    private void getFoundItems() {
	final ParseQuery<ParseObject> query = ParseQuery.getQuery("FoundItem");
	query.whereEqualTo("hunt", getHuntID());
	query.whereEqualTo("user", currentUsername);
	query.findInBackground(new FindCallback<ParseObject>() {
	    public void done(final List<ParseObject> foundItemsList,
		    ParseException e) {
		if (e == null) {
		    foundItems = foundItemsList;
		    currentScore = foundItems.size();
		    String[] ItemsArray = new String[foundItemsList.size()];
		    int i = 0;

		    for (ParseObject foundItem : foundItemsList) {
			ItemsArray[i] = (String) foundItem.get("item");
			i++;
		    }
		    foundItemsArray = ItemsArray;
		    addAllGameItems();
		    showFoundItems();
		} else {
		    Log.w("Parse Error", "player username retrieval failure");
		}
	    }
	});
    }

    private void addAllGameItems() {
	for (int i = 0; i < allItems.size(); i++) {
	    addToListView(allItems.get(i), getItemAdapter());
	}
	setScore();
	for (final String listItem : getListViewItems()) {
	    for (final ParseObject foundItem : foundItems) {
		Log.d("Delete found...", "listItem " + listItem + "foundItem "
			+ foundItem.getString("item"));
		if (listItem.equals(foundItem.getString("item"))) {
		    deleteListItem(listItem);
		}
	    }
	}
    }

    private void addToListView(final String item,
	    final ArrayAdapter<String> adapter) {
	adapter.add(item);
	adapter.notifyDataSetChanged();
    }

    private ArrayAdapter<String> getItemAdapter() {
	final ListView listView = (ListView) findViewById(R.id.listViewItems);
	final ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView
		.getAdapter();
	return adapter;
    }

    private void showItemFoundDialog(final String itemName) {
	Bundle item = new Bundle();
	item.putString("item", itemName);
	final DialogFragment itemFoundDialogFragment = new ItemFoundDialogFragment();
	itemFoundDialogFragment.setArguments(item);
	itemFoundDialogFragment.show(getFragmentManager(), "itemFound");
    }

    private void setupButtonCallbacks() {
	declineButton = (Button) findViewById(R.id.declineButton);
	declineButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// withdraw();
		saveDecliner();
	    }
	});
    }

    public void onFoundItemDialog(final String item) {
	// final ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	// query.whereExists("winner");
	// query.getInBackground(hunt.getObjectId(),
	// new GetCallback<ParseObject>() {
	// public void done(ParseObject thisHunt, ParseException e) {
	// if (e == null) {
	// if (thisHunt == null) {
	incrementScore();
	deleteListItem(item);
	appendFoundItem(item);
	saveFoundItem(item);
	checkWin();
	// } else {
	// final String winner = thisHunt
	// .getString("winner");
	// // launchHasWinnerDialog(winner);
	// }
	// } else {
	// Log.w("error", "game retrieval error");
	// }
	// }
	//
	// });
    }

    // private void launchHasWinnerDialog(final String winner) {
    // Bundle won = new Bundle();
    // won.putString("winner", winner);
    // final DialogFragment HasWinnerDialogFragment = new
    // HasWinnerDialogFragment();
    // HasWinnerDialogFragment.setArguments(won);
    // HasWinnerDialogFragment.show(getFragmentManager(), "HasWinner");
    //
    // }

    private void saveFoundItem(final String item) {
	final ParseObject foundItem = new ParseObject("FoundItem");
	foundItem.put("hunt", getHuntID());
	foundItem.put("user", currentUsername);
	foundItem.put("item", item);
	foundItem.put("currentScore", currentScore);
	foundItem.saveInBackground(new SaveCallback() {
	    public void done(ParseException e) {
		if (e == null) {
		    Log.d("PlayHunt", "Item Found!");
		} else {
		    Log.d("PlayHunt", "Error creating found item: " + e);
		}
	    }
	});
    }

    private void deleteListItem(final String item) {
	final ArrayAdapter<String> adapter = getItemAdapter();
	adapter.remove(item);
	adapter.notifyDataSetChanged();
    }

    private void showFoundItems() {
	TextView itemsFound = (TextView) findViewById(R.id.tvItemsFound);
	itemsFound.setText(Arrays.toString(foundItemsArray).replaceAll(
		"\\[|\\]", ""));
    }

    private void appendFoundItem(String item) {
	if (currentScore > 1) {
	    StringBuilder MyStringBuilder = new StringBuilder(item);
	    MyStringBuilder.insert(0, ", ");
	    final TextView itemsFound = (TextView) findViewById(R.id.tvItemsFound);
	    itemsFound.append(MyStringBuilder);
	} else {
	    final TextView itemsFound = (TextView) findViewById(R.id.tvItemsFound);
	    itemsFound.append(item);
	}
    }

    private void setScore() {
	final TextView scoreView = (TextView) findViewById(R.id.tvScore);
	final TextView totalPointsView = (TextView) findViewById(R.id.tvTotalPoints);
	scoreView.setText(String.valueOf(currentScore));
	totalPointsView.setText(" out of " + allItems.size());
    }

    private void incrementScore() {
	currentScore++;
	setScore();
    }

    private void checkWin() {
	if (currentScore == allItems.size()) {
	    setWinner();
	}
    }

    private void setWinner() {
	hunt.put("winner", currentUser);
	hunt.saveInBackground(new SaveCallback() {
	    public void done(ParseException e) {
		if (e == null) {
		    Log.d("PlayHunt",
			    "Winner Saved to hunt named "
				    + hunt.getString("name") + "!");
		} else {
		    Log.d("PlayHunt", "Error in saving winner: " + e);
		}
	    }
	});
	sendWinnerNotification();
    }

    private void sendWinnerNotification() {
	for (final String player : players) {
	    final ParseQuery<ParseInstallation> pushQuery = ParseInstallation
		    .getQuery();
	    pushQuery.whereEqualTo("owner", player);

	    final ParsePush push = new ParsePush();
	    push.setQuery(pushQuery);
	    push.setMessage(currentUsername + " has won the "
		    + hunt.getString("huntTitle") + " scavenger hunt!");
	    push.sendInBackground();
	}
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

    private void saveDecliner() {
	final ParseObject Declined = new ParseObject("Declined");
	Declined.put("hunt", getHuntID());
	Declined.put("userName", currentUsername);
	Declined.saveInBackground(new SaveCallback() {
	    public void done(ParseException e) {
		if (e == null) {
		    Log.d("Declined", currentUsername);
		    Toast.makeText(getApplicationContext(),
			    " You have been withdrawn from this hunt.",
			    Toast.LENGTH_SHORT).show();
		    finish();
		} else {
		    Log.d("Declined", "Error saving decline: " + e);
		}
	    }
	});
    }

    // private void withdraw() {

    // final List<String> playerToRemove = new ArrayList<String>();
    // playerToRemove.add(currentUsername);
    // ParseQuery<ParseObject> query = ParseQuery.getQuery("notification");
    // query.whereEqualTo("invitedUser", currentUsername);
    // // query.whereEqualTo("huntId", getHuntID());
    // Log.i("scavenger Hunt", "notifications for " + currentUsername);
    // query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
    // public void done(final ParseObject notification, ParseException e) {
    // if (e == null) {
    // if (notification == null) {
    // Log.d("Game",
    // "The hunt had an invalid objectid and wasn't found");
    // Toast.makeText(
    // getApplicationContext(),
    // " Sorry, There are no hunts available at this time! Try back later!!",
    // Toast.LENGTH_LONG).show();
    // finish();
    // } else {
    // notification.put("declined", true);
    // notification.saveInBackground(new SaveCallback() {
    // @Override
    // public void done(com.parse.ParseException arg0) {
    // if (arg0 == null) {
    // Intent invtations = new Intent(
    // PlayHuntActivity.this,
    // InvitationsActivity.class);
    // invtations.putExtra("HuntID", getHuntID());
    // startActivity(invtations);
    // } else {
    // Log.i("ScavengerHuntActivity",
    // "problem saving withdrawal " + arg0);
    // Log.i("ScavengerHuntActivity",
    // "not. objectid "
    // + notification
    // .getObjectId());
    // }
    // }
    // });
    // }
    // }
    // }
    // });
    //
    // }
    // }

    private void withdraw() {
	final List<String> playerToRemove = new ArrayList<String>();
	playerToRemove.add(currentUsername);
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	query.selectKeys(Arrays.asList("huntPlayers"));
	query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		Log.i("scavenger Hunt", "in parse query" + hunt.getObjectId());
		if (e == null) {
		    hunt.removeAll("huntPlayers", playerToRemove);
		    hunt.saveInBackground(new SaveCallback() {
			public void done(com.parse.ParseException arg0) {
			    if (arg0 == null) {
				Toast.makeText(
					getApplicationContext(),
					"You've been withdrawn from this hunt.",
					Toast.LENGTH_LONG).show();
				Intent i = new Intent(PlayHuntActivity.this,
					PlayingHuntsActivity.class);
				i.putExtra("UpdateHuntActivity", getHuntID());
				startActivity(i);
				Log.i("Decline", "Players Removed!");
			    } else {
				Log.i("Decline", "Error removing players "
					+ arg0);
			    }
			}
		    });
		}
	    }
	});

    }

}

// private void removeDeclined() {
// final List<String> playerToRemove = new ArrayList<String>();
// playerToRemove.add(currentUsername);
// ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
// query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
// public void done(ParseObject hunt, com.parse.ParseException e) {
// Log.i("scavenger Hunt", "in parse query" + hunt.getObjectId());
// if (e == null) {
// hunt.removeAll("huntPlayers", playerToRemove);
// hunt.saveInBackground(new SaveCallback() {
// public void done(com.parse.ParseException arg0) {
// if (arg0 == null) {
// Toast.makeText(
// getApplicationContext(),
// "You've been withdrawn from this hunt.",
// Toast.LENGTH_LONG).show();
// Intent i = new Intent(PlayHuntActivity.this,
// PlayingHuntsActivity.class);
// i.putExtra("UpdateHuntActivity", getHuntID());
// startActivity(i);
// Log.i("Decline", "Players Removed!");
// } else {
// Log.i("Decline", "Error removing players "
// + arg0);
// }
// }
// });
// }
// }
// });
//
// }