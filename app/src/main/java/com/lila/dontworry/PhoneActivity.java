package com.lila.dontworry;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.DisplayObject;
import com.lila.dontworry.Logic.ObjectType;

public class PhoneActivity extends AppCompatActivity {

    private Context self = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialisation of Activity and Toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
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

        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, "date DESC");
                int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int photo = cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_ID);
                String number_to_call= "";
                String person_to_call = "";
                String photo_of_person= "";
                int flag = 0;
                while(cursor.moveToNext()&&flag==0){System.out.println("nam" + cursor.getString(name));
                    String call_duration = cursor.getString(duration);
                    if(Integer.parseInt(call_duration)>300){

                            flag = 1;
                            number_to_call = cursor.getString(number);
                            person_to_call = cursor.getString(name);;
                            photo_of_person = cursor.getString(photo);

                    }
                }

                final Context self = this;
                final int flag2 = flag;
                final String person_to_call2 = person_to_call;
                final String number_to_call2 = number_to_call;
                final String photo_of_person2 = photo_of_person;



                if(person_to_call2!=""){
                    TextView t=(TextView)findViewById(R.id.callText);
                    t.setText("You should call "+person_to_call2+"!");
                    Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(photo_of_person2));
                    ImageView imageView=(ImageView)findViewById(R.id.contactPhoto);
                    imageView.setImageURI(photoUri);
                    DisplayObject displayObject = new DisplayObject(ObjectType.CONTACT, person_to_call);
                    DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
                    databaseHandler.addConnected(displayObject, 0 , 0);
                    System.out.println("new add to DB in PhoneActivity");
                }

                fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(self, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("check2");
                }
                //phoning someone
                if(flag2==1){System.out.println("flag21" + flag2);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+number_to_call2));
                    startActivity(callIntent);
                }
                //redirect to phone book
                else{System.out.println("flag22" + flag2);
                    Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
