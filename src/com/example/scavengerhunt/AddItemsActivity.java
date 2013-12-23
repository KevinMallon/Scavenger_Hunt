package com.example.scavengerhunt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class AddItemsActivity extends CreateHuntActivity {
    private EditText itemNameEditText;
    String itemList;
    ArrayAdapter<String> m_adapter;
    ArrayList<String> m_listItems = new ArrayList<String>();
    ArrayList<String> itemsArray = new ArrayList<String>();
    ListView lv;
    int count = 0;

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("huntID");
	return huntID;
    }

    private String from() {
	Intent i = getIntent();
	String from = i.getStringExtra("from");
	return from;
    }

    private String hasItems() {
	Intent i = getIntent();
	String hasItems = i.getStringExtra("hasItems");
	System.out.println("hasItems add " + hasItems);
	return hasItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_items_activity);
	itemNameEditText = (EditText) findViewById(R.id.item_input);
	lv = (ListView) findViewById(R.id.listView1);
	m_adapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, m_listItems);
	lv.setAdapter(m_adapter);

	System.out.println("hasItems oncreate " + hasItems());
	if (hasItems() == "yes") {
	}

	setupButtonCallbacks();
    }

    private void setAdapter(final ArrayList<String> m_listItems) {
	lv = (ListView) findViewById(R.id.listView1);
	m_adapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, m_listItems);
	lv.setAdapter(m_adapter);
	System.out.println("m_listItems set" + m_listItems);
    }

    private void setupButtonCallbacks() {
	findViewById(R.id.addButton).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		String itemInput = itemNameEditText.getText().toString();

		if (null != itemInput && itemInput.length() > 0) {

		    m_listItems.add(itemInput);
		    if (count == 0 && from() == "create") {
			setAdapter(m_listItems);
		    }
		    m_adapter.notifyDataSetChanged();
		    itemDisplay();

		    // ParseObject huntItem = new ParseObject("HuntItem");
		    // huntItem.put("itemName", itemInput);
		    // huntItem.put("huntId", getHuntID());
		    // huntItem.saveInBackground();
		}
		itemNameEditText.requestFocus();
		count += 1;

	    }
	});

	// findViewById(R.id.removeButton).setOnClickListener(
	// new OnClickListener() {
	// public void onClick(View v) {
	// SparseBooleanArray checked = lv
	// .getCheckedItemPositions();
	// ArrayList<String> selectedItems = new ArrayList<String>();
	// ArrayList<String> selectedPositions = new ArrayList<String>();
	// for (int i = 0; i < checked.size(); i++) {
	// // Item position in adapter
	// int position = checked.keyAt(i);
	// // Add if it is checked i.e.) == TRUE!
	// if (checked.valueAt(i))
	// selectedItems.add(adapter.getItem(position));
	// selectedPositions.add(Integer.toString(position));
	// }
	// removeItems(selectedItems);
	// // returnPlayers(selectedPlayers);
	// };
	// });

	findViewById(R.id.finishedButton).setOnClickListener(
		new OnClickListener() {
		    @Override
		    public final void onClick(View v) {
			Intent intent = new Intent(AddItemsActivity.this,
				CreateHuntActivity.class);
			saveItems();
			if (from() == "create") {
			    intent.putExtra("data", Integer.toString(count));
			    setResult(1, intent);
			    finish();
			} else {
			    returnItems();
			}
		    }
		});
    }

    private String getItemNameInput() {
	return getUserInput(R.id.item_input);
    }

    private String getUserInput(int id) {
	EditText input = (EditText) findViewById(id);
	return input.getText().toString();
    }

    // private void saveItems(ArrayList<String> selectedItems) {
    // String huntId = getHuntID();
    // for (int i = 0; i < selectedItems.size(); i++) {
    // String item = selectedItems.get(i);
    // Log.d("Player", item.toString());
    // ParseObject huntItem = new ParseObject("HuntItem");
    // huntItem.put("itemName", item);
    // huntItem.put("huntId", huntId);
    // huntItem.saveInBackground(new SaveCallback() {
    // public void done(ParseException e) {
    // if (e == null) {
    // Log.d("Save", "gamePlayer data saved!");
    // } else {
    // Log.d("Save", "Error saving gamePlayer: " + e);
    // }
    // }
    //
    // });
    // }
    // }
    private String[] getItemsArray() {
	ListView listView = (ListView) findViewById(R.id.listView1);
	ListAdapter itemsAdapter = listView.getAdapter();
	String[] itemsArray = new String[itemsAdapter.getCount()];
	for (int i = 0; i < itemsAdapter.getCount(); i++) {
	    itemsArray[i] = (itemsAdapter.getItem(i)).toString();
	    System.out.println("itemsArray[i] " + itemsArray[i]);
	}
	System.out.println("itemsArray " + itemsArray);
	return itemsArray;
    }

    private void returnItems() {
	String[] items = getItemsArray();
	System.out.println("itemsToBundle " + items);
	Intent intent = new Intent(AddItemsActivity.this,
		UpdateHuntActivity.class);
	Bundle bundle = new Bundle();
	bundle.putSerializable("items", items);
	intent.putExtras(bundle);
	setResult(1, intent);
	finish();
    }

    private void saveItems() {
	final List<String> items = Arrays.asList(getItemsArray());

	ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		if (e == null) {
		    // System.out.println("huntItems " + itemsArray);
		    // System.out.println("huntID " + getHuntID());
		    hunt.addAllUnique("huntItems", items);
		    hunt.saveInBackground(new SaveCallback() {
			public void done(com.parse.ParseException arg0) {
			    if (arg0 == null) {
				Log.d("Item Save", "Items Saved!");
			    } else {
				Log.i("ItemsActivity", "Error saving items "
					+ arg0);
			    }
			}
		    });
		}
	    }
	});
    }

    public void itemDisplay() {
	if (getItemNameInput().length() > 0) {
	    itemsArray.add(getItemNameInput());
	    itemNameEditText.setText("");
	}
    }
}
