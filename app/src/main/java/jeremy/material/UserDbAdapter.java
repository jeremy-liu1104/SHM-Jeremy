package jeremy.material;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;

import java.util.List;

/**
 * Created by Jeremy_Liu on 2016/5/1.
 */
public class UserDbAdapter {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROWID = "_id";

    public static final String KEY_NAME = "name";
    public static final String KEY_PN = "pn";
    public static final String KEY_ITEM = "item";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CAREER = "career";
    public static final String KEY_IMAGE = "image";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table user (_id integer primary key autoincrement, " + "username text not null, password text not null, name text not null, phone text not null, item text not null, pn text not null, email text not null, career text not null, address text not null, image text not null);";
    /*private static final String DATABASE_CREATE = "create table user(_id integer primary key autoincrement," +
            "username text not null, " +
            "password text not null, " +
            "name text not null," +
            "phone text not null," +
            "item text not null," +
            "id text not null," +
            "email text not null," +
            "career text not null," +
            "address text not null);"; */
    private static final String DATABASE_NAME = "database";
    private static final String DATABASE_TABLE = "user";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS user");
            onCreate(db);
        }
    }

    public UserDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public UserDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void closeclose() {
        mDbHelper.close();
    }

    public void createUser(String username, String password) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASSWORD, password);
        initialValues.put(KEY_NAME,"Not Set");
        initialValues.put(KEY_PHONE,"Not Set");
        initialValues.put(KEY_ITEM, "Not Set");
        initialValues.put(KEY_PN,"Not Set");
        initialValues.put(KEY_EMAIL,"Not Set");
        initialValues.put(KEY_CAREER,"Not Set");
        initialValues.put(KEY_ADDRESS,"Not Set");
        initialValues.put(KEY_IMAGE,"");
        mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    //public List<String> getInfo(String username){
       // Cursor cursor = mDb.q
    //}

    //更新数据库
    public void updateInfo(String username,String name, String phone,String pn,String email,String career,String address,String image){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,name);
        cv.put(KEY_PHONE,phone);
        //cv.put(KEY_ITEM,item);
        cv.put(KEY_PN,pn);
        cv.put(KEY_EMAIL,email);
        cv.put(KEY_CAREER,career);
        cv.put(KEY_ADDRESS,address);
        cv.put(KEY_IMAGE,image);
        mDb.update(DATABASE_TABLE,cv,"username=?",new String[]{username});
    }


    public void updateExImage(String username,String name, String phone,String pn,String email,String career,String address){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,name);
        cv.put(KEY_PHONE,phone);
        //cv.put(KEY_ITEM,item);
        cv.put(KEY_PN,pn);
        cv.put(KEY_EMAIL,email);
        cv.put(KEY_CAREER,career);
        cv.put(KEY_ADDRESS,address);
        //cv.put(KEY_IMAGE,image);
        mDb.update(DATABASE_TABLE,cv,"username=?",new String[]{username});
    }


    public Cursor getAllNotes(String username) {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
                KEY_PASSWORD, KEY_NAME, KEY_PHONE, KEY_ITEM, KEY_PN, KEY_EMAIL, KEY_CAREER, KEY_ADDRESS, KEY_IMAGE }, "username=?", new String[] {username}, null, null, null);
    }

    public Cursor getUnPic() {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
                KEY_IMAGE }, null, null, null, null, null);
    }
    public Cursor getDiary(String username) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
                                KEY_PASSWORD}, KEY_USERNAME + "='" + username+"'", null, null,
                        null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
