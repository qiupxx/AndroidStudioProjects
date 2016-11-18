package com.example.peixuan.todolist;

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

/**
 * Created by peixuan on 16/7/18.
 */
public class ToDoContentProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.paad.todoprovider/todoitems");

    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task";
    public static final String KEY_CREATION_DATE = "creation_date";

    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        //构造底层数据库,直到调用getWritableDatabase()/getReadableDatabase()才创建数据库
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(),
                MySQLiteOpenHelper.DATABASE_NAME, null, MySQLiteOpenHelper.DATABASE_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
            default:
                break;
        }
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    //创建UriMatcher匹配所有的行或一行
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //匹配所有的行
        uriMatcher.addURI("com.padd.todoprovider", "todoitems", ALLROWS);
        //匹配单独一行
        uriMatcher.addURI("com.padd.todoprovider", "todoitems/#", SINGLE_ROW);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case (ALLROWS): {
                return "vnd.android.cursor.dir/vnd.paad.todoitems";
            }
            case (SINGLE_ROW): {
                return "vnd.android.cursor.item/vnd.paad.todotems";
            }
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();

        String nullColumnHack = null;
        long id = db.insert(MySQLiteOpenHelper.DATABASE_TABLE, nullColumnHack, values);
        if (id > -1) {
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
            default:
                break;
        }
        //to return the number of deleted items, you must specify a where clasue,
        //to delete all row and return a value, pass in "1".
        if (selection == null)
            selection = "1";

        int deletCount = db.delete(MySQLiteOpenHelper.DATABASE_TABLE,
                selection, selectionArgs);
        //通知任何监视我们数据库数据改变的组件
        getContext().getContentResolver().notifyChange(uri, null);
        return deletCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
            default:
                break;
        }

        int updateCount = db.update(MySQLiteOpenHelper.DATABASE_TABLE,
                values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "todoDatabase.db";
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_TABLE = "todoItemTable";

        //SQL语句创建数据库
        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE +
                " (" + KEY_ID + " integer primary key autoincrement, " + KEY_TASK +
                " text not null, " + KEY_CREATION_DATE + " long);";


        public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * 当磁盘没有存在的数据库时创建一个
         *
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        /**
         * 版本不匹配时，需要更新到当前的版本时被调用
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //最简单的方式就是抛弃一个表，重新创建一个
            db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
}
