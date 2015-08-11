package com.example.giangdam.contentproviderex1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button btnShowContact,btnAccessCallLog,btnAccessMedia,btnAccessBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAccessBookmark = (Button) findViewById(R.id.btnAccessBookmark);
        btnShowContact = (Button) findViewById(R.id.btnShowContact);
        btnAccessCallLog = (Button) findViewById(R.id.btnAccessCallLog);
        btnAccessMedia = (Button) findViewById(R.id.btnAccessMedia);


        btnAccessMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessMediaStore();
            }
        });

        btnShowContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(MainActivity.this,ShowContactActivity.class);
                startActivity(intent);
            }
        });

        btnAccessBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessBookmark();
            }
        });

        btnAccessCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessCallLog();
            }
        });

    }

    public void accessCallLog() {
        String[] projection = new String[]{
                CallLog.Calls.DATE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION
        };

        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,projection,null,null,null);

        cursor.moveToFirst();
        String s= "";

        while (!cursor.isAfterLast())
        {
            for(int i = 0; i< cursor.getColumnCount();i++)
            {
                s+= cursor.getString(i)+ "-";
            }
            s+="\n";
            cursor.moveToNext();
        }

        cursor.close();
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    public void accessBookmark() {

        String []projection ={Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL};

        Cursor cursor = getContentResolver().query(Browser.BOOKMARKS_URI,projection,null,null,null);

        cursor.moveToFirst();
        String s= "";
        int titleIndex = cursor.getColumnIndex(Browser.BookmarkColumns.TITLE);
        int urlIndex = cursor.getColumnIndex(Browser.BookmarkColumns.URL);
        while (!cursor.isAfterLast())
        {
            s+= cursor.getString(titleIndex) + "-" + cursor.getString(urlIndex) ;
            cursor.moveToNext();

        }

        cursor.close();
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    public  void accessMediaStore() {

    }


}
