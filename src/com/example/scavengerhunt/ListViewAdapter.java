package com.example.scavengerhunt;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<Players> {
    Context context;
    LayoutInflater inflater;
    List<Players> playerslist;
    private static SparseBooleanArray mSelectedItemsIds;

    public ListViewAdapter(Context context, int resourceId,
	    List<Players> playerslist) {
	super(context, resourceId, playerslist);
	mSelectedItemsIds = new SparseBooleanArray();
	this.context = context;
	this.playerslist = playerslist;
	inflater = LayoutInflater.from(context);
    }

    public class ViewHolder {
	TextView username;
	TextView userid;
	CheckedTextView text1;
    }

    @Override
    public int getCount() {
	return playerslist.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
	final ViewHolder holder;
	if (view == null) {
	    holder = new ViewHolder();
	    view = inflater.inflate(R.layout.players_listview_item, null);
	    // Locate the TextViews in listview_item.xml
	    holder.username = (TextView) view.findViewById(R.id.username);
	    holder.userid = (TextView) view.findViewById(R.id.userid);
	    view.setTag(holder);
	} else {
	    holder = (ViewHolder) view.getTag();
	}
	// Capture position and set to the TextViews
	holder.username.setText(playerslist.get(position).getUsername());
	holder.userid.setText(playerslist.get(position).getUserid());

	return view;
    }

    public List<Players> getPlayers() {
	return playerslist;
    }

    public void toggleSelection(int position) {
	selectView(position, !mSelectedItemsIds.get(position));
	System.out.println("!mSelectedItemsIds " + mSelectedItemsIds);
	System.out.println("size1 " + mSelectedItemsIds.size());
    }

    public void selectView(int position, boolean value) {
	if (value)
	    mSelectedItemsIds.put(position, value);
	else
	    mSelectedItemsIds.delete(position);
	notifyDataSetChanged();
	System.out.println("mSelectedItemsIds " + mSelectedItemsIds);
	System.out.println("size1 " + mSelectedItemsIds.size());
    }

    public int getSelectedCount() {
	return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
	System.out.println("mSelectedItemsIds " + mSelectedItemsIds);
	System.out.println("size1 " + mSelectedItemsIds.size());
	return mSelectedItemsIds;
    }

}
