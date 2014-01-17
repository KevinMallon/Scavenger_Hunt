package com.example.scavengerhunt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UpdateItems extends CreateHuntActivity {
    private EditText itemNameEditText;
    private EditText descriptionEditText;
    String itemList;
    // ArrayAdapter<String> m_adapter;
    ItemAdapter adapter;
    ArrayList<String> m_listItems = new ArrayList<String>();
    ArrayList<String> itemsArray = new ArrayList<String>();
    ArrayList<String> itemAndDescriptionArray = new ArrayList<String>();
    String[] itemNames;
    String[] itemDescriptions;
    ListView lv;
    int count = 0;
    final Context context = this;

    private String getHuntID() {
	Intent i = getIntent();
	String huntID = i.getStringExtra("huntID");
	return huntID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.listview_item);
	itemNameEditText = (EditText) findViewById(R.id.item_input);
	descriptionEditText = (EditText) findViewById(R.id.description_input);

	ParseQuery<ParseObject> query = ParseQuery.getQuery("HuntItem");
	query.whereEqualTo("objectId", getHuntID());
	query.findInBackground(new FindCallback<ParseObject>() {
	    @Override
	    public void done(List<ParseObject> objects, ParseException e) {
		if (e == null) {
		    String[] itemNames = new String[objects.size()];
		    String[] itemDescriptions = new String[objects.size()];
		    int selectedItemPositions[] = new int[objects.size()];
		    int i = 0;
		    int a = 0;

		    String itemName = new String();
		    String itemDescription = new String();
		    for (ParseObject obj : objects) {
			itemName = (java.lang.String) obj.get("itemName");
			itemDescription = (java.lang.String) obj
				.get("itemDescription");
			itemNames[i] = itemName;
			itemDescriptions[i] = itemDescription;
			// boolean contains = currentItems.contains(itemName);
			// System.out.println("itemName " + itemName);
			// System.out.println("contains? " + contains);
			// if (contains == true) {
			// selectedItemPositions[a] = i;
			// }
			i++;
			a++;
		    }
		    System.out.println("currentItems in onCreate " + itemNames);
		    // m_listItems = itemNames;
		    lv = (ListView) findViewById(R.id.listView1);
		    // m_adapter = new ArrayAdapter<String>(this,
		    // android.R.layout.simple_list_item_1, m_listItems);
		    adapter = new ItemAdapter(context, itemNames,
			    itemDescriptions);
		    lv.setAdapter(adapter);
		    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
				final View view, int position, long id) {
			    String itemName = m_listItems.get(position);
			    setItemName(itemName);
			    int itemPosition = position;
			}
		    });
		    // setAdapter(context, itemNames, itemDescriptions);
		    System.out.println("selectedItemPositions "
			    + selectedItemPositions);
		    // listView = (ListView) findViewById(R.id.list);
		    // button = (Button) findViewById(R.id.testbutton);
		    // adapter = new ArrayAdapter<String>(AddItemsActivity.this,
		    // android.R.layout.simple_list_item_multiple_choice,
		    // usernames);
		    // listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		    // listView.setAdapter(adapter);
		    // for (int position : selectedItemPositions) {
		    // listView.setItemChecked(position, true);
		    // }
		    // button.setOnClickListener(AddItemsActivity.this);
		}

		setupButtonCallbacks();
	    }

	    // private void setAdapter(Context context, final ArrayList<String>
	    // itemNames, final ArrayList<String> itemDescriptions) {
	    //
	    // }

	    private void setupButtonCallbacks() {
		findViewById(R.id.addButton).setOnClickListener(
			new OnClickListener() {

			    @Override
			    public void onClick(View v) {
				String itemInput = itemNameEditText.getText()
					.toString();
				String itemDescriptionInput = descriptionEditText
					.getText().toString();

				if (null != itemInput && itemInput.length() > 0) {

				    m_listItems.add(itemInput);
				    adapter.notifyDataSetChanged();
				    itemDisplay();

				    ParseObject huntItem = new ParseObject(
					    "HuntItem");
				    huntItem.put("itemName", itemInput);
				    huntItem.put("itemDescription",
					    itemDescriptionInput);
				    huntItem.put("huntId", getHuntID());
				    huntItem.saveInBackground();
				}
				itemNameEditText.requestFocus();
				count += 1;
			    }
			});
		findViewById(R.id.finishedButton).setOnClickListener(
			new OnClickListener() {
			    @Override
			    public final void onClick(View v) {
				Intent intent = new Intent(UpdateItems.this,
					CreateHuntActivity.class);
				intent.putExtra("data", Integer.toString(count));
				setResult(1, intent);
				finish();
			    }
			});
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

    private void setItemName(String itemName) {
	itemNameEditText.setText(itemName);
	// getUserInput(R.id.item_input);
    }

    // private void String setItemDescription(String itemPosition) {
    // descriptionEditText.setText("");
    // itemDescriptions.getText().toString();
    // }

    public void itemDisplay() {
	if (getItemNameInput().length() > 0) {
	    itemsArray.add(getItemNameInput());
	    itemNameEditText.setText("");
	    descriptionEditText.setText("");
	}
    }
}
