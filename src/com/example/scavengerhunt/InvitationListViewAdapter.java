package com.example.scavengerhunt;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InvitationListViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<String> titles;
    ArrayList<String> huntIds;
    // ArrayList<String> beginDates;
    Format formatter = new SimpleDateFormat("MM-dd-yy");

    public InvitationListViewAdapter(Context context, ArrayList<String> titles,
	    ArrayList<String> iDs) {

	this.context = context;
	this.titles = titles;
	this.huntIds = iDs;
	// this.beginDates = beginDates;

    }

    @Override
    public int getCount() {
	return titles.size();
    }

    @Override
    public Object getItem(int position) {
	return null;
    }

    @Override
    public long getItemId(int position) {
	return 0;
    }

    public void remove(int position) {
	titles.remove(titles.get(position));
	huntIds.remove(huntIds.get(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	TextView tvtitle;
	// TextView tvbeginDate;
	TextView tvhuntId;
	TextView Accept;
	TextView Refuse;

	inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	View itemView = inflater.inflate(R.layout.invitation_listview, parent,
		false);

	tvtitle = (TextView) itemView.findViewById(R.id.tvtitle);
	// tvbeginDate = (TextView) itemView.findViewById(R.id.tvbeginDate);
	tvhuntId = (TextView) itemView.findViewById(R.id.tvhuntId);
	Accept = (TextView) itemView.findViewById(R.id.Accept);
	Refuse = (TextView) itemView.findViewById(R.id.Refuse);

	tvtitle.setText(titles.get(position));
	// tvbeginDate.setText((CharSequence) beginDates.get(position));
	tvhuntId.setText(huntIds.get(position));
	return itemView;
    }
}
