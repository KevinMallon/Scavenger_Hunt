package com.example.scavengerhunt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ItemFoundDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	final String item = getArguments().getString("item");

	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	builder.setMessage(item)
		.setPositiveButton("Found it!",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				((PlayHuntActivity) getActivity())
					.onFoundItemDialog(item);
			    }
			})
		.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				ItemFoundDialogFragment.this.getDialog()
					.cancel();
			    }
			});
	return builder.create();
    }

}
