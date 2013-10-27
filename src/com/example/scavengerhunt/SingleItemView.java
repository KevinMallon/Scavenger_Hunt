package com.example.scavengerhunt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleItemView extends CreateHuntActivity {
    // Declare Variables
    TextView txtname;
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// Get the view from singleitemview.xml
	setContentView(R.layout.singleitemview);

	// Retrieve data from MainActivity on item click event
	Intent i = getIntent();

	// Get the name
	name = i.getStringExtra("name");

	// Locate the TextView in singleitemview.xml
	txtname = (TextView) findViewById(R.id.name);

	// Load the text into the TextView
	txtname.setText(name);

    }

}
