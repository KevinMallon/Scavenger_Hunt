package com.example.scavengerhunt;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class DeleteItemsActivity extends CreateHuntActivity {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.delete_items_activity);
	ParseQuery<ParseObject> huntquery = ParseQuery.getQuery("Hunt");
	huntquery.whereEqualTo("objectId", getHuntID());
	huntquery.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    @Override
	    public void done(ParseObject object, com.parse.ParseException e) {
		if (e == null) {
		    List<String> items = new ArrayList<String>();
		    items = object.getList("huntItems");
		    // ArrayList<String> items = new ArrayList<String>();
		    // int selectedItemPositions[] = new int[object.size()];
		    // String itemName = new String();
		    // for (ParseObject obj : object) {
		    // itemName = (java.lang.String) obj.get("itemName");
		    // itemNames.add(itemName);
		    // }
		    m_listItems = (ArrayList<String>) items;
		    setAdapter(m_listItems);
		    // System.out.println("selectedItemPositions "
		    // + selectedItemPositions);
		}
	    }
	});

	setupButtonCallbacks();
    }

    private void setAdapter(final ArrayList<String> m_listItems) {
	lv = (ListView) findViewById(R.id.listView1);
	m_adapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_multiple_choice, m_listItems);
	lv.setAdapter(m_adapter);
	lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    }

    private void setupButtonCallbacks() {
	findViewById(R.id.deleteButton).setOnClickListener(
		new OnClickListener() {
		    public final void onClick(View v) {
			SparseBooleanArray checkedPositions = lv
				.getCheckedItemPositions();
			ArrayList<String> selectedItems = new ArrayList<String>();
			ArrayList<String> remainingItems = new ArrayList<String>();
			for (int i = 0; i < m_listItems.size(); i++) {
			    if (checkedPositions.get(i)) {
				selectedItems.add(m_adapter.getItem(i));
			    } else {
				remainingItems.add(m_adapter.getItem(i));
			    }
			}

			// SparseBooleanArray checked = lv
			// .getCheckedItemPositions();
			// System.out.println(" # checked " + checked.size());

			// for (int i = 0; i < checked.size(); i++) {
			// int position = checked.keyAt(i);
			// if (checked.valueAt(i))
			// selectedItems.add(m_adapter.getItem(position));
			// }

			System.out.println(" selectedItems " + selectedItems);
			System.out.println(" remainingItems " + remainingItems);
			removeItems(selectedItems);
			returnItems(remainingItems);

			Intent intent = new Intent(DeleteItemsActivity.this,
				CreateHuntActivity.class);
			intent.putExtra("data", selectedItems);
			setResult(1, intent);
			finish();
		    }
		});
    }

    private void removeItems(final ArrayList<String> selectedItems) {
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Hunt");
	query.getInBackground(getHuntID(), new GetCallback<ParseObject>() {
	    public void done(ParseObject hunt, com.parse.ParseException e) {
		if (e == null) {
		    hunt.removeAll("huntItems", selectedItems);
		    hunt.saveInBackground(new SaveCallback() {
			public void done(com.parse.ParseException arg0) {
			    if (arg0 == null) {
				Log.d("Item Save", "Items Removed!");
			    } else {
				Log.i("ItemsActivity", "Error removing items "
					+ arg0);
			    }
			}
		    });
		}
	    }
	});
    }

    private void returnItems(ArrayList<String> remainingItems) {
	String[] items = new String[remainingItems.size()];
	for (int i = 0; i < remainingItems.size(); i++) {
	    items[i] = remainingItems.get(i);
	}
	System.out.println(" remainingItems for return " + items);
	Intent intent = new Intent(DeleteItemsActivity.this,
		UpdateHuntActivity.class);
	Bundle bundle = new Bundle();
	bundle.putSerializable("items", items);
	intent.putExtras(bundle);
	setResult(1, intent);
	finish();
    }

}
