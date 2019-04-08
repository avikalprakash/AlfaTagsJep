package com.organization.jeptagsalpha.Temperature;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.organization.jeptagsalpha.Database.DatabaseHandler;
import com.organization.jeptagsalpha.R;

import java.util.ArrayList;


//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class TempHome extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    static final int CUSTOM_DIALOG_ID = 0;
ManageProfiles.Contact_Adapter cAdapter;
    ListView dialog_ListView;
    ArrayList<TempPojo> contact_data = new ArrayList<TempPojo>();
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_home);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Control"));
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Chart"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
       /* Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);*/

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temp_home, menu);
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
            Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;
        contact_data.clear();
        db = new DatabaseHandler(this);
        ArrayList<TempPojo> contact_array_from_db = db.Get_Contacts();
        switch(id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(TempHome.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Profile Select");

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

                    @Override
                    public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
                        Toast.makeText(TempHome.this,
                                "OnCancelListener",
                                Toast.LENGTH_LONG).show();
                    }});

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

                    @Override
                    public void onDismiss(DialogInterface dialog) {
// TODO Auto-generated method stub
                        Toast.makeText(TempHome.this,
                                "OnDismissListener",
                                Toast.LENGTH_LONG).show();
                    }});

//Prepare ListView in dialog
                for (int i = 0; i < contact_array_from_db.size(); i++) {

                    int tidno = contact_array_from_db.get(i).getID();
                    String name = contact_array_from_db.get(i).getName();
                    String interval = contact_array_from_db.get(i).get_interval();
                    String upper = contact_array_from_db.get(i).get_upper();
                    String lower = contact_array_from_db.get(i).get_lower();
                    TempPojo cnt = new TempPojo();
                    cnt.setID(tidno);
                    cnt.setName(name);
                    cnt.set_interval(interval);
                    cnt.set_upper(upper);
                    cnt.set_lower(lower);

                    contact_data.add(cnt);
                }
                db.close();
                dialog_ListView = (ListView)dialog.findViewById(R.id.dialoglist);

                cAdapter = new ManageProfiles.Contact_Adapter(TempHome.this, R.layout.listview_row,
                        contact_data);
                dialog_ListView.setAdapter(cAdapter);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
// TODO Auto-generated method stub
                        Toast.makeText(TempHome.this,
                                parent.getItemAtPosition(position).toString() + " clicked",
                                Toast.LENGTH_LONG).show();
                        dismissDialog(CUSTOM_DIALOG_ID);
                    }});

                break;
        }

        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
// TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog, bundle);

        switch(id) {
            case CUSTOM_DIALOG_ID:
//
                break;
        }
    }

}
