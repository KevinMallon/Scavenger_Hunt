package com.example.scavengerhunt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class HasWinnerDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	// final String winner = getArguments().getString("winner");
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	builder.setMessage("Sorry, another player has already won this hunt.")
		.setPositiveButton("Hunt List",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(getActivity(),
					PlayingHuntsActivity.class);
				startActivity(intent);
			    }
			});
	return builder.create();
    }

}
