package com.irene.fintrip.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.irene.fintrip.R;

import static com.irene.fintrip.R.id.detailsPic;

/**
 * Created by reneewu on 4/8/2017.
 */

public class DisplayPictureFragment extends DialogFragment {

    public DisplayPictureFragment(){}

    public static DisplayPictureFragment newInstance(String imageUrl){
        DisplayPictureFragment frag = new DisplayPictureFragment();
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        ImageView ivDetailsPic = (ImageView) view.findViewById(detailsPic);
        String imageUrl = getArguments().getString("imageUrl");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivDetailsPic.setClipToOutline(true);
        }
        Glide.with(getContext())
                .load(imageUrl)
                //.load("http://pic.pimg.tw/omifind/1468387801-1461333924.jpg")
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivDetailsPic);

        ImageView btn = (ImageView) view.findViewById(R.id.close);

        // edit button onClickListener
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }
}
