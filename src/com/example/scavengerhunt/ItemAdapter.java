//package com.example.scavengerhunt;
//
//import java.util.List;
//
//import android.content.ClipData.Item;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//public class ItemAdapter extends ArrayAdapter<Item> {
//    private Context mContext;
//    private List<Item> mItems;
//
//    public ItemAdapter(Context context, List<Item> objects) {
//	super(context, R.layout.item_row, objects);
//	this.mContext = context;
//	this.mItems = objects;
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//	if (convertView == null) {
//	    LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
//	    convertView = mLayoutInflater.inflate(R.layout.item_row, null);
//	}
//
//	Item item = mItems.get(position);
//
//	TextView descriptionView = (TextView) convertView
//		.findViewById(R.id.item_description);
//
//	descriptionView.setText(item.getDescription());
//
//	return convertView;
//    }
//
// }