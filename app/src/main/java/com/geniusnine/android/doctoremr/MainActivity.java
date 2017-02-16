package com.geniusnine.android.doctoremr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /////
    private MobileServiceClient mClient;
    private MobileServiceTable<Patient> mPatient;
    private PatientAdapter mAdapter;
    private ListView listViewPatient;

    private EditText editTextFname;
    private EditText editTextLname;
    private Button buttonAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFname = (EditText) findViewById(R.id.editTextFname);
        editTextLname = (EditText) findViewById(R.id.editTextLname);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://doctorfe.azurewebsites.net",
                    this);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            // Get the Mobile Service Table instance to use

            mPatient = mClient.getTable(Patient.class);


            // Create an adapter to bind the items with the view
            mAdapter = new PatientAdapter(this, R.layout.row_list_patient);
            listViewPatient = (ListView) findViewById(R.id.listViewPatientName);
            listViewPatient.setAdapter(mAdapter);


            // Load the items from the Mobile Service
            showAll();

        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            // createAndShowDialog(e, "Error");
        }
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });


        // Get the Mobile Service Table instance to use


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void checkItemInTable(Patient item) throws ExecutionException, InterruptedException {
        mPatient.update(item).get();
    }

    public void setItem()
   {

   }
    /**
     * Mark an item as completed
     *
     * @param item The item to mark
     */
    public void checkItem(final Patient item) {
        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table
        // item.setComplete(true);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    // checkItemInTable(item);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mAdapter.remove(item);
                            mPatient.delete(item);


                        }
                    });
                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };
        task.execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showAll() {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<Patient> results = mPatient.execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mAdapter.clear();
                            for (Patient item : results) {
                                mAdapter.add(item);
                            }
                        }
                    });
                } catch (Exception exception) {
                    //createAndShowDialog(exception, "Error");
                }
                return null;
            }
        };
        //runAsyncTask(task);
        task.execute();
    }

     public void addItem(View view) {
        if (mClient == null) {
            return;
        }
         // Create a new item
        final Patient item = new Patient();

        item.setFname(editTextFname.getText().toString());
        item.setLname(editTextLname.getText().toString());

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final Patient entity = mPatient.insert(item).get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mAdapter.add(entity);

                        }
                    });
                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        task.execute();

        editTextFname.setText("");
        editTextLname.setText("");
        showAll();
    }

    public void Update(final Patient item,String fname,String lname) {
        if (mClient == null) {
            return;
        }
        item.setFname(fname);
        item.setLname(lname);

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final Patient entity = mPatient.update(item).get();
                    // mPatient.update(item).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          mPatient.update(entity);
                           mAdapter.remove(entity);

                        }
                    });
                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        task.execute();
      showAll();
    }
}
