package com.example.scavengerhunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    String[] itemName;
    String[] itemDescription;
    LayoutInflater inflater;

    public ItemAdapter(Context context, String[] itemName,
	    String[] itemDescription) {
	this.context = context;
	this.itemName = itemName;
	this.itemDescription = itemDescription;

    }

    @Override
    public int getCount() {
	return itemName.length;
    }

    @Override
    public Object getItem(int position) {
	return null;
    }

    @Override
    public long getItemId(int position) {
	return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	// Declare Variables
	TextView txtitemName;
	TextView txtitemDescription;

	inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	View itemView = inflater.inflate(R.layout.listview_item, parent, false);

	// Locate the TextViews in listview_item.xml
	txtitemName = (TextView) itemView.findViewById(R.id.itemName);
	txtitemDescription = (TextView) itemView
		.findViewById(R.id.itemDescription);

	// Capture position and set to the TextViews
	txtitemName.setText(itemName[position]);
	txtitemDescription.setText(itemDescription[position]);

	return itemView;
    }
}