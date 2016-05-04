package org.mythtv.android.app.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.mythtv.android.app.R;

/**
 * Created by dmfrey on 4/21/16.
 */
public class AddLiveStreamDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder
                .setTitle( R.string.add_live_stream_title )
                .setPositiveButton( R.string.add_live_stream_positive_label, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick( DialogInterface dialog, int which ) {

                        getTargetFragment().onActivityResult( getTargetRequestCode(), Activity.RESULT_OK, null );

                    }

                })
                .setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick( DialogInterface dialog, int which ) {

                        getTargetFragment().onActivityResult( getTargetRequestCode(), Activity.RESULT_CANCELED, null );

                    }

                });

        return builder.create();
    }

}