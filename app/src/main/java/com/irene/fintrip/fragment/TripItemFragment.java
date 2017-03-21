package com.irene.fintrip.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.irene.fintrip.R;
import com.irene.fintrip.models.Trip;

import org.parceler.Parcels;

/**
 * Created by Irene on 2017/3/21.
 */

public class TripItemFragment extends DialogFragment {

    private EditText etTrip;
    private Button btnSave;
    private String condition;
    private Trip aTrip;

    public TripItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below

    }

    public static TripItemFragment newInstance(Trip trip) {
        TripItemFragment frag = new TripItemFragment();
        if(trip != null)
        {
            Bundle args = new Bundle();
            args.putParcelable("trip", Parcels.wrap(trip));
            frag.setArguments(args);
        }
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etTrip = (EditText) view.findViewById(R.id.etTrip);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        if(getArguments() != null)
        {
            aTrip = (Trip) Parcels.unwrap(getArguments().getParcelable("trip"));
            condition = "edit";
        }
        else
        {
            aTrip = new Trip();
            condition = "new";
        }

        // edit button onClickListener
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: save data to DB
                if(etTrip.getText()!= null && !etTrip.getText().toString().trim().equals(""))
                {
                    aTrip.setListName(etTrip.getText().toString());
                    // Send back data to activity
                    TripItemFragment.TripItemDialogListener listener = (TripItemFragment.TripItemDialogListener) getActivity();
                    listener.onFinishEditDialog(aTrip,condition);

                }
                dismiss();
            }
        });
    }

    public interface TripItemDialogListener {
        void onFinishEditDialog(Trip trip, String condition);
    }

}
