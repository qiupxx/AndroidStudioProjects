package com.example.peixuan.earthquakefeed;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class EarthquakeProvider extends ContentProvider {
    //公开provider的URI
    public static final Uri CONTENT_URI = Uri.parse("content://com.peixuan.earthquakeprovider/earthquakes");

    //定义表列名字
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_DETAILS = "details";
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_LOCATION_LAT = "latitude";
    public static final String KEY_LOCATION_LNG = "longitude";
    public static final String KEY_MAGNITUDE = "magnitude";
    public static final String KEY_LINK = "link";

    EarthquakeDatabaseHelper dbHelper;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int count;
        switch (uriMatcher.match(uri)) {
            case QUAKES:
                count = database.delete(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, selection, selectionArgs);
                break;
            case QUAKE_ID:
                String segment = uri.getPathSegments().get(1);
                count = database.delete(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE,
                        KEY_ID + "=" + segment + (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    //定义常量，区分不同的RUI请求
    private static final int QUAKES = 1;
    private static final int QUAKE_ID = 2;
    private static final int SEARCH = 3;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI("com.peixuan.earthquakeprovider", "earthquakes", QUAKES);
        uriMatcher.addURI("com.peixuan.earthquakeprovider", "earthquakes/#", QUAKE_ID);
        uriMatcher.addURI("com.peixuan.earthquakeprovider", SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
        uriMatcher.addURI("com.peixuan.earthquakeprovider", SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH);
        uriMatcher.addURI("com.peixuan.earthquakeprovider", SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH);
        uriMatcher.addURI("com.peixuan.earthquakeprovider", SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case (QUAKES):
                return "vnd.android.cursor.dir/vnd.peixuan.earthquakes";

            case (QUAKE_ID):
                return "vnd.android.cursor.item/vnd.peixuan.earthquakes";
            case (SEARCH):
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long rowID = database.insert(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE,
                "quake", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        dbHelper = new EarthquakeDatabaseHelper(context, EarthquakeDatabaseHelper.DATABASE_NAME,
                null, EarthquakeDatabaseHelper.DATABASE_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE);

        switch (uriMatcher.match(uri)) {
            case QUAKE_ID:
                queryBuilder.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            case SEARCH:
                queryBuilder.appendWhere(KEY_SUMMARY + " LIKE \"%" + uri.getPathSegments().get(1) + "%\"");
                //重设搜索的表来源
                queryBuilder.setProjectionMap(SEARCH_PROJECTION_MAP);
                break;
            default:
                break;
        }
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = KEY_DATE;
        } else {
            orderBy = sortOrder;
        }

        Cursor c = queryBuilder.query(database, projection, selection, selectionArgs,
                null, null, orderBy);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int count;
        switch (uriMatcher.match(uri)) {
            case QUAKES:
                count = database.update(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, values, selection, selectionArgs);
                break;
            case QUAKE_ID:
                String segment = uri.getPathSegments().get(1);
                count = database.update(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, values,
                        KEY_ID + "=" + segment + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    //创建一个SQLiteOpenHelper class 负责打开／创建／管理数据库版本控制
    private static class EarthquakeDatabaseHelper extends SQLiteOpenHelper {
        private static final String TAG = "EarthquakeProvider";
        private static final String DATABASE_NAME = "earthquake.db";
        private static final int DATABASE_VERSION = 1;
        private static final String EARTHQUAKE_TABLE = "earthquakes";

        //SQLite创建表语句
        private static final String DATABASE_CREATE =
                "create table " + EARTHQUAKE_TABLE + " ("
                        + KEY_ID + " integer primary key autoincrement, "
                        + KEY_DATE + " INTEGER, "
                        + KEY_DETAILS + " TEXT, "
                        + KEY_SUMMARY + " TEXT, "
                        + KEY_LOCATION_LAT + " FLOAT, "
                        + KEY_LOCATION_LNG + " FLOAT, "
                        + KEY_MAGNITUDE + " FLOAT, "
                        + KEY_LINK + " TEXT);";

        //底层的数据库
        private SQLiteDatabase earthquakeDB;

        public EarthquakeDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public EarthquakeDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + EARTHQUAKE_TABLE);
            onCreate(db);
        }
    }

    //创建HashMap提供搜索建议
    private static final HashMap<String, String> SEARCH_PROJECTION_MAP;

    static {
        SEARCH_PROJECTION_MAP = new HashMap<String, String>();
        SEARCH_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, KEY_SUMMARY
                + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        SEARCH_PROJECTION_MAP.put("_id", KEY_ID + " AS " + "_id");
    }
}
