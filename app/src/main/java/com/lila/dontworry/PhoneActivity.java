package com.lila.dontworry;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.lila.dontworry.Logic.Contact;
import com.lila.dontworry.Logic.DBValues;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.DisplayObject;
import com.lila.dontworry.Logic.ObjectType;

import java.util.ArrayList;

public class PhoneActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;
    private ArrayList<String> contactsString;
    private Context self = this;
    private String number_to_call;
    private String person_to_call;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialisation of Activity and Toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        t = (TextView) findViewById(R.id.callText);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.call);

        //permissions to check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("check1");
        }

        //vertical orientation - > number based on the last calls
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, "date DESC");
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int photo = cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_ID);
            String number_to_call = "";
            String person_to_call = "";
            String photo_of_person = "";
            int flag = 0;
            while (cursor.moveToNext() && flag == 0) {
                System.out.println("nam" + cursor.getString(name));
                String call_duration = cursor.getString(duration);
                if (Integer.parseInt(call_duration) > 300) {
                    flag = 1;
                    number_to_call = cursor.getString(number);
                    person_to_call = cursor.getString(name);
                    photo_of_person = cursor.getString(photo);
                }
            }

            final Context self = this;
            final int flag2 = flag;
            final String person_to_call2 = person_to_call;
            final String number_to_call2 = number_to_call;
            final String photo_of_person2 = photo_of_person;

            if (person_to_call2 != "") {
                t.setText("You should call " + person_to_call2 + "!");
                Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(photo_of_person2));
                ImageView imageView = (ImageView) findViewById(R.id.contactPhoto);
                imageView.setImageURI(photoUri);
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(self, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("check2");
                    }
                    //phoning someone
                    if (flag2 == 1) {
                        DisplayObject displayObject = new DisplayObject(ObjectType.CONTACT, person_to_call2);
                        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(self);
                        databaseHandler.addConnected(displayObject, DBValues.CONTACT_QUESTION, DBValues.CONTACT_HINT);
                        System.out.println("new add to DB in PhoneActivity");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number_to_call2));
                        startActivity(callIntent);
                    }
                    //redirect to phone book
                    else {
                        System.out.println("flag22" + flag2);

                        DisplayObject displayObject = new DisplayObject(ObjectType.CONTACT, person_to_call2);
                        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(self);
                        databaseHandler.addConnected(displayObject, DBValues.CONTACT_QUESTION, DBValues.CONTACT_HINT);
                        System.out.println("new add to DB in PhoneActivity");
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, 1);
                    }
                }
            });
        } else { //horizontal orientation shows phone book
            contacts = new ArrayList<>();
            contactsString = new ArrayList<>();
            getContactsFromPhoneBook();

            for (Contact c : contacts) {
                contactsString.add(c.getName());
            }

            ListView listView = findViewById(R.id.contactsList);
            ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsString);
            listView.setAdapter(adapter);
            listView.setSelection(0);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Contact contact = contacts.get(position);
                    t.setText(contact.getName() + "!");
                    ImageView imageView = (ImageView) findViewById(R.id.contactPhoto);
                    if(contact.getPhoto()!=null){
                        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(contact.getPhoto()));
                        imageView.setImageURI(photoUri);
                    }
                    else{
                        imageView.setImageResource(R.mipmap.dontworryfragenzeichen);
                    }
                    number_to_call = contact.getNumber();
                }
            });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(self, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("check2");
                    }
                    DisplayObject displayObject = new DisplayObject(ObjectType.CONTACT, person_to_call);
                    DatabaseHandler databaseHandler = DatabaseHandler.getInstance(self);
                    databaseHandler.addConnected(displayObject, DBValues.CONTACT_QUESTION, DBValues.CONTACT_HINT);
                    System.out.println("new add to DB in PhoneActivity");
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + number_to_call));
                    startActivity(callIntent);
                }
            });
        }
    }

    private void getContactsFromPhoneBook() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                String photo = null;
                if(cursor.getColumnIndex( ContactsContract.Contacts.PHOTO_ID)!=-1){
                    photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                }
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contacts.add(new Contact(name, number, photo));
            }
        }
        cursor.close();
    }

    //back arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
