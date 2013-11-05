package com.example.scavengerhunt;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ItemsActivity extends CreateHuntActivity {
    private static final String TAG = "ItemsActivity";
    private Button addButton;
    private Button finishedButton;
    private EditText itemNameEditText;
    private TextView itemTextView;
    private String itemName;
    private EditText descriptionEditText;
    String itemList;
    ArrayAdapter<String> m_adapter;
    ArrayList<String> m_listItems = new ArrayList<String>();
    ArrayList<String> itemsArray = new ArrayList<String>();
    ArrayList<String> itemAndDescriptionArray = new ArrayList<String>();
    ListView lv;

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

			Intent intent = new Intent(ItemsActivity.this,
				CreateHuntActivity.class);
			Bundle b = new Bundle();
			b.putStringArrayList("items", itemsArray);

			// Add the bundle to the intent.
			intent.putExtras(b);

			// start the CreateHuntActivity
			startActivity(intent);

			// Intent intent = new Intent();
			// System.out.println(itemsArray.size());
			// intent.putExtra("items", itemsArray);
			// setResult(RESULT_OK, intent);
			// finish();
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

    // public void getGame() {
    // Bundle extras = getIntent().getExtras();
    // String hunt = extras.getString("huntId");
    // }

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

// // m_adapter = new ArrayAdapter<String>(this, R.layout.items_activity,
// // m_listItems);
// // lv.setAdapter(m_adapter);
// // final String input = itemNameEditText.getText().toString();
//
// }

// private void setupButtonCallbacks() {
// findViewById(R.id.addButton).setOnClickListener(new OnClickListener() {
// public void onClick(View v) {
// String item = itemNameEditText.getText().toString();
// StringBuilder str = new StringBuilder().append(itemList)
// .append(',').append(' ').append(item);
// // str.append(item);
// itemTextView.setText(str);
// itemList = itemlist + item;
// createItem();
// };
// });
// }

// String itemList = itemNameEditText.getText().toString(); // gets you the
// // contents of
// // edit text
// itemTextView.setText(itemList);
// public void updateData() {
// ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
// query.findInBackground(new FindCallback<Item>() {
//
// @Override
// public void done(List<Item> tasks, ParseException error) {
// if (tasks != null) {
// mAdapter.clear();
// mAdapter.addAll(tasks);
// }
// }
// });
// }
// public String getDescription(){
// return getString("description");
// }
//
// public void setDescription(String description){
// put("description", description);
// }
//
// public String getPoints(){
// return getString("points");
// }
//
// public void setPoints(String points){
// put("description", points);
// }