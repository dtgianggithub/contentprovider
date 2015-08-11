package com.example.giangdam.contentproviderex1;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class ShowContactActivity extends AppCompatActivity {

    Button btnBack;
    ListView lvShowContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        btnBack = (Button)findViewById(R.id.btnBack);
        lvShowContact = (ListView)findViewById(R.id.lvShowContact);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        showAllContact();


    }

    public void showAllContact() {

        Uri uri = Uri.parse("content://contacts/people");
        ArrayList<String> arrayList = new ArrayList<String>();
        CursorLoader loader = new CursorLoader(this, uri,null,null,null,null);
        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            String s ="";
            String idColumnName = ContactsContract.Contacts._ID;
            int idIndex = cursor.getColumnIndex(idColumnName);
            s = cursor.getString(idIndex) + "-";

            String nameColumName = ContactsContract.Contacts.DISPLAY_NAME;
            int nameIndex =  cursor.getColumnIndex(nameColumName);
            s+=cursor.getString(nameIndex);
            cursor.moveToNext();
            arrayList.add(s);
        }

        cursor.close();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lvShowContact.setAdapter(arrayAdapter);

     }


}
