package com.example.scavengerhunt;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_items_activity);
	itemNameEditText = (EditText) findViewById(R.id.item_input);
	// if (hasItems() == "yes") {
	ParseQuery<ParseObject> query = ParseQuery.getQuery("HuntItem");
	query.whereEqualTo("huntId", getHuntID());
	query.findInBackground(new FindCallback<ParseObject>() {
	    @Override
	    public void done(List<ParseObject> objects, ParseException e) {
		if (e == null) {
		    ArrayList<String> itemNames = new ArrayList<String>();
		    int i = 0;
		    int a = 0;
		    // System.out.println("currentItems in onCreate "
		    // + currentItems);
		    String itemName = new String();
		    for (ParseObject obj : objects) {
			itemName = (java.lang.String) obj.get("itemName");
			itemNames.add(itemName);
			i++;
			a++;
		    }
		    System.out.println("itemNames" + itemNames);
		    m_listItems = itemNames;
		    System.out.println("m_listItems set" + m_listItems);
		    setAdapter(m_listItems);
		}

	    }

	});
	// }

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
		    m_adapter.notifyDataSetChanged();
		    itemDisplay();

		    ParseObject huntItem = new ParseObject("HuntItem");
		    huntItem.put("itemName", itemInput);
		    huntItem.put("huntId", getHuntID());
		    huntItem.saveInBackground();
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

    public void itemDisplay() {
	if (getItemNameInput().length() > 0) {
	    itemsArray.add(getItemNameInput());
	    itemNameEditText.setText("");
	}
    }
}
