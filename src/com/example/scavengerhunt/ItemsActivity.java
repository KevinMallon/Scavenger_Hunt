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
	String huntID = i.getStringExtra("huntID");
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
	findViewById(R.id.addButton).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		String itemInput = itemNameEditText.getText().toString();
		String itemDescriptionInput = descriptionEditText.getText()
			.toString();

		if (null != itemInput && itemInput.length() > 0) {

		    m_listItems.add(itemInput);
		    m_adapter.notifyDataSetChanged();
		    itemDisplay();

		    // for (int i = 0; i < itemsArray.size(); i++) {
		    ParseObject item = new ParseObject("Item");
		    item.put("itemName", itemInput);
		    item.put("itemDescription", itemDescriptionInput);
		    item.put("huntID", getHuntID());
		    item.saveInBackground();
		    // }
		}
		itemNameEditText.requestFocus();
	    }
	});
	findViewById(R.id.finishedButton).setOnClickListener(
		new OnClickListener() {
		    public final void onClick(View v) {
			Intent intent = new Intent(ItemsActivity.this,
				CreateHuntActivity.class);
			intent.putExtra("huntID", getHuntID());
			System.out.println("sending from Item list "
				+ getHuntID());
			setResult(RESULT_OK, intent);
			finish();
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

    public void itemDisplay() {
	if (getItemNameInput().length() > 0) {
	    itemsArray.add(getItemNameInput());
	    itemNameEditText.setText("");
	    descriptionEditText.setText("");
	}
    }
}
