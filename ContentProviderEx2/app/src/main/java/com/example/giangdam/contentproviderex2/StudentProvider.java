package com.example.giangdam.contentproviderex2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Giang.Dam on 7/24/2015.
 */
public class StudentProvider extends ContentProvider {


    static  final  String PROVIDER_NAME = "com.example.mycontentprovider.College";
    static final String URI = "content://" + PROVIDER_NAME + "/students";


    static final Uri CONTENT_URI = Uri.parse(URI);


    static final String _ID = "id";
    static final String NAME = "name";
    static final String GRADE= "grade";

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static final int STUDENTS = 1;
    static final int STUDENTS_ID = 2;

    static final UriMatcher uriMatcher ;
    static { uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            uriMatcher.addURI(PROVIDER_NAME,"students",STUDENTS);
            uriMatcher.addURI(PROVIDER_NAME,"students/#",STUDENTS_ID);

    }


    private SQLiteDatabase db;

    static final  String DATABASE_NAME = "college";
    static final String STUDENTS_TABLE_NAME = "students";

    static final  int DATABASE_VERSION = 1;
    static  final  String CREATE_STUDENTS_TABLE = "CREATE TABLE IF NOT EXISTS "+STUDENTS_TABLE_NAME+"  ("+_ID+" integer primary key autoincrement, "+NAME+" text not null, "+GRADE+" text not null)";


    private  static  class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_STUDENTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + STUDENTS_TABLE_NAME );
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();


        return (db != null) ? true: false ;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        SQLiteQueryBuilder qb  = new SQLiteQueryBuilder();
        qb.setTables(STUDENTS_TABLE_NAME);


        switch(uriMatcher.match(uri)){
            case STUDENTS:
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;
            case STUDENTS_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }


        if(sortOrder == null || sortOrder.equals(""))
        {
            sortOrder = NAME;
        }

        Cursor cursor = qb.query(db,projection,selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case STUDENTS:
                return "vnd.android.cursor.dir/com.example.students";
            case STUDENTS_ID:
                return "vnd.android.cusor.item/com.example.students";
            default:
                throw new IllegalArgumentException("Unsupport URIL " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {


        long rowID  = db.insert(STUDENTS_TABLE_NAME,"", values);
        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,rowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        try {
            throw new SQLException("Failed to add record into " + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case STUDENTS:
                count = db.delete(STUDENTS_TABLE_NAME,selection, selectionArgs);
                break;
            case STUDENTS_ID:
                count = db.delete(STUDENTS_TABLE_NAME,_ID + "=" + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection)  ?  " and (" + selection + ')' : ""),selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknow URI "+ uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri))
        {
            case STUDENTS:
                count = db.update(STUDENTS_TABLE_NAME, values, selection,selectionArgs);
                break;
            case STUDENTS_ID:
                count = db.update(STUDENTS_TABLE_NAME,values, _ID + "=" + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " and (" + selection +  ')':""),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
