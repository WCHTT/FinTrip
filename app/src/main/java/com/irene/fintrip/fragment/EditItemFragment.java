package com.irene.fintrip.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.irene.fintrip.R;

/**
 * Created by reneewu on 3/19/2017.
 */

public class EditItemFragment   extends DialogFragment {
    private EditText etBuyer;
    private EditText etPrice;

    public EditItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditItemFragment newInstance(String owner, Double price) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("owner", owner);
        args.putDouble("price", price);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etBuyer = (EditText) view.findViewById(R.id.etBuyerName);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        Button btn = (Button) view.findViewById(R.id.btnEdit);

        String owner = getArguments().getString("owner","");
        etBuyer.setText(owner);

        Double itemPrice = getArguments().getDouble("price",0);
        etPrice.setText(itemPrice.toString());
        etPrice.setRawInputType(Configuration.KEYBOARD_12KEY);

        // Show soft keyboard automatically and request focus to field
        etBuyer.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // edit button onClickListener
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: save data to DB

                // Send back data to activity
                EditItemDialogListener listener = (EditItemDialogListener) getActivity();
                listener.onFinishEditDialog(etBuyer.getText().toString(),etPrice.getText().toString());

                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(String owner, String price);
    }
}
