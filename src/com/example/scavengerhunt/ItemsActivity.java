package com.example.scavengerhunt;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseObject;

public class ItemsActivity extends CreateHuntActivity {
    private static final String TAG = "ItemsActivity";
    private EditText itemNameEditText;
    private EditText descriptionEditText;
    String itemList;
    ArrayAdapter<String> m_adapter;
    ArrayList<String> m_listItems = new ArrayList<String>();
    ArrayList<String> itemsArray = new ArrayList<String>();
    ArrayList<String> itemAndDescriptionArray = new ArrayList<String>();
    ListView lv;

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("HuntID");
	System.out.println("got in item list " + huntID);
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.items_activity);
	itemNameEditText = (EditText) findViewById(R.id.item_input);
	descriptionEditText = (EditText) findViewById(R.id.description_input);

	lv = (ListView) findViewById(R.id.listView1);
	m_adapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, m_listItems);
	lv.setAdapter(m_adapter);

	setupButtonCallbacks();
    }

    private void setupButtonCallbacks() {
	findViewById(R.id.finishedButton).setOnClickListener(
		new OnClickListener() {
		    public void onClick(View v) {
			String[] itemStrArr = new String[itemsArray.size()];
			for (int i = 0; i < itemsArray.size(); i++) {
			    itemStrArr[i] = itemsArray.get(i);
			}

			final String huntID = getHuntID();
			for (int i = 0; i < itemsArray.size(); i++) {
			    ParseObject item = new ParseObject("Item");
			    item.put("itemName", itemsArray.get(i));
			    item.put("huntID", huntID);
			    item.saveInBackground();
			}

			finish();

		    }
		});
	findViewById(R.id.addButton).setOnClickListener(new OnClickListener() {
	    public final void onClick(View v) {
		String itemInput = itemNameEditText.getText().toString();

		if (null != itemInput && itemInput.length() > 0) {

		    m_listItems.add(itemInput);

		    m_adapter.notifyDataSetChanged();

		    itemDisplay();
		}
	    }
	});
    }

    private String getItemDescriptionInput() {
	return getUserInput(R.id.description_input);
    }

    private String getItemNameInput() {
	return getUserInput(R.id.item_input);
    }

    private String getUserInput(int id) {
	EditText input = (EditText) findViewById(id);
	return input.getText().toString();
    }

    public void itemDisplay() {
	if (getItemNameInput().length() > 0) {
	    itemsArray.add(getItemNameInput());
	    // itemAndDescriptionArray.add(getItemNameInput() + ", " +
	    // getItemDescriptionInput());
	    itemNameEditText.setText("");
	    descriptionEditText.setText("");

	}
    }
}
