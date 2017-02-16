package com.geniusnine.android.doctoremr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by AndriodDev8 on 15-02-2017.
 */

public class PatientAdapter extends ArrayAdapter<Patient> {

    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public PatientAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;

        final Patient currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.row_list_patient, parent, false);
        }

        row.setTag(currentItem);

        final TextView textViewPatientName = (TextView) row.findViewById(R.id.textViewPatientName);
        textViewPatientName.setText(currentItem.getFname() + " " + currentItem.getLname());
        final Button buttondelete = (Button) row.findViewById(R.id.btndelete);
        buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) mContext;
                activity.checkItem(currentItem);
            }
        });
        final Button buttonupdate = (Button) row.findViewById(R.id.btnupdate);
        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.update_alret_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Update");
                final EditText editTextfname = (EditText) alertLayout.findViewById(R.id.editTextfname);
                editTextfname.setText(currentItem.getFname());
                final EditText editTextlname = (EditText) alertLayout.findViewById(R.id.editTextlname);
                editTextlname.setText(currentItem.getLname());
                alertDialogBuilder.setView(alertLayout);
                alertDialogBuilder.setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String fname;
                                String lname;
                                fname = editTextfname.getText().toString().trim();
                                lname = editTextlname.getText().toString().trim();
                                MainActivity activity = (MainActivity) mContext;
                                activity.Update(currentItem,fname,lname);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }


        });


        return row;
    }

}

