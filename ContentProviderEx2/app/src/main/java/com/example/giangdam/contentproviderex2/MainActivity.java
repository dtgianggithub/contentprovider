package com.example.giangdam.contentproviderex2;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    Button btnAdd, btnRetrieve;
    EditText txtName, txtGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnRetrieve = (Button)findViewById(R.id.btnRetrieve);

        txtName = (EditText)findViewById(R.id.txtName);
        txtGrade = (EditText)findViewById(R.id.txtGrade);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(StudentProvider.NAME, txtName.getText().toString());
                contentValues.put(StudentProvider.GRADE,txtGrade.getText().toString());


                Uri uri = getContentResolver().insert(StudentProvider.CONTENT_URI, contentValues);
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        });

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URI = "content://com.example.mycontentprovider.College/students";
                Uri students = Uri.parse(URI);
                Cursor cursor = getContentResolver().query(students, null, null, null,"name");
                if(cursor.moveToFirst())
                {
                    do{

                        String data = "";
                        data += cursor.getString(cursor.getColumnIndex(StudentProvider._ID)) + ",";
                        data += cursor.getString(cursor.getColumnIndex(StudentProvider.NAME)) + ",";
                        data += cursor.getString(cursor.getColumnIndex(StudentProvider.GRADE)) + ",";

                        Toast.makeText(getBaseContext(), data, Toast.LENGTH_LONG).show();

                    }while (cursor.moveToNext());
                }

                cursor.close();
            }
        });
    }


}
