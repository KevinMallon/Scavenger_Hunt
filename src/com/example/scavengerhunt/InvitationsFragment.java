//package com.example.scavengerhunt;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//public class InvitationsFragment extends DialogFragment {
//
//    int position;
//    String title;
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//	LayoutInflater inflater = getActivity().getLayoutInflater();
//
//	View view = inflater.inflate(R.layout.fragment_invitations, null);
//
//	final int position = getArguments().getInt("position");
//	String title = getArguments().getString("title");
//	// View view = getArguments().getView("view");
//	builder.setView(view)
//		.setTitle(title)
//		.setPositiveButton("Accept",
//			new DialogInterface.OnClickListener() {
//			    @Override
//			    public void onClick(DialogInterface dialog, int id) {
//				((PlayingHuntsActivity) getActivity())
//					.onFinishInvitationsFragment(position,
//						"accept");
//			    }
//			})
//		.setNegativeButton("Refuse",
//			new DialogInterface.OnClickListener() {
//			    public void onClick(DialogInterface dialog, int id) {
//				((PlayingHuntsActivity) getActivity())
//					.onFinishInvitationsFragment(position,
//						"refuse");
//			    }
//			});
//	return builder.create();
//    }
// }
