//package com.example.scavengerhunt;
//
//import android.app.DatePickerDialog;
//import android.app.DialogFragment;
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.TextView;

//public class TitleFragment extends DialogFragment {
//    private EditText mEditText;
//
//    public EditTitleDialog() {
//        // Empty constructor required for DialogFragment
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_title, container);
//        mEditText = (EditText) view.findViewById(R.id.txt_title);
//        getDialog().setTitle("Hello");
//
//        return view;
//    }

package com.example.scavengerhunt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class TitleFragment extends DialogFragment {
    private EditText mEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	LayoutInflater inflater = getActivity().getLayoutInflater();

	View view = inflater.inflate(R.layout.fragment_title, null);
	mEditText = (EditText) view.findViewById(R.id.title);

	builder.setView(view)
		.setPositiveButton("Save",
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int id) {
				((MainMenuActivity) getActivity())
					.onFinishTitleFragment(mEditText
						.getText().toString());
			    }
			})
		.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				TitleFragment.this.getDialog().cancel();
			    }
			});
	return builder.create();
    }
}

// @Override
// public void onTitleSet(Title view) {

// StringBuilder task = new StringBuilder();
// task.append(month + 1).append("-").append(day).append("-").append(year)
// .append(" ");
// if (getTag() == "startDatePicker") {
// ((TextView) getActivity().findViewById(R.id.editStartDate))
// .setText(task);
// }
// if (getTag() == "endDatePicker") {
// ((TextView) getActivity().findViewById(R.id.editEndDate))
// .setText(task);
// }

// }
//
// private String getHuntTitleInput() {
// return getUserInput(R.id.textbox_Title);
// }
//
// private String getUserInput(int id) {
// EditText input = (EditText) findViewById(id);
// return input.getText().toString();
// }
//
// }