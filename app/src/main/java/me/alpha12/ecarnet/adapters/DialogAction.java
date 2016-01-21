package me.alpha12.ecarnet.adapters;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;

/**
 * Created by guilhem on 20/01/2016.
 */
public class DialogAction extends DialogFragment  {

    String[] items;


    public void setItems(ArrayList<String> items){
        this.items = items.toArray(new String[items.size()]);
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actions");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item

            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
