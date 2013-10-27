package com.example.scavengerhunt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseObject;

public class ItemsActivity extends Activity {
    private static final String TAG = "ItemsActivity";
    private Button addButton;
    private Button finishedButton;
    private EditText itemNameEditText;
    private EditText descriptionEditText;
    private String itemName;
    private String description;
    ListView ListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.items_activity);
	itemNameEditText = (EditText) findViewById(R.id.item_input);
	descriptionEditText = (EditText) findViewById(R.id.description_input);
	ListView = (ListView) findViewById(R.id.item_list);

	// Adapter = new ItemAdapter(this, new ArrayList<Item>());
	// ListView.setAdapter(Adapter);
    }

    private String getItemNameInput() {
	return getUserInput(R.id.item_input);
    }

    private String getItemDescriptionInput() {
	return getUserInput(R.id.description_input);
    }

    private String getUserInput(int id) {
	EditText input = (EditText) findViewById(id);
	return input.getText().toString();
    }

    public void createItem(View v) {
	if (getItemNameInput().length() > 0) {
	    ParseObject item = new ParseObject("Item");
	    item.put("itemName", getItemNameInput());
	    item.put("description", getItemDescriptionInput());
	    item.saveEventually();
	    itemNameEditText.setText("");
	    descriptionEditText.setText("");
	}
    }

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

}
