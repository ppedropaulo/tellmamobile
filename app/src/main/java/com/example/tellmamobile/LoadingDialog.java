package com.example.tellmamobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity mActivity){
        activity = mActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }
}
