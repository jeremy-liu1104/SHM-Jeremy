package jeremy.material;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.ResultSet;

/**
 * Created by Jeremy_Liu on 2016/5/15.
 */
public class ItemDbAdapter {




    public static final String KEY_ROWID = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ITEMNAME = "itemname";
    public static final String KEY_TYPE = "type";
    public static final String KEY_DATE = "date";
    public static final String KEY_PRICE = "price";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";

    public static final String KEY_IMAGE = "image";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table items (_id integer primary key autoincrement, " + "username text not null, itemname text not null, type text not null, date text not null, price text not null, description contact not null, contact text not null, email text not null, address text not null, image text not null);";
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
    private static final String DATABASE_NAME = "item";
    private static final String DATABASE_TABLE = "items";
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
            db.execSQL("DROP TABLE IF EXISTS items");
            onCreate(db);
        }
    }

    public ItemDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public ItemDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void closeclose() {
        mDbHelper.close();
    }

    public void createUser(String username,String itemname,String type,String date,String price,String description, String contact, String email,String address,String image) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME,username);
        initialValues.put(KEY_ITEMNAME,itemname);
        initialValues.put(KEY_TYPE,type);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_PRICE,price);
        initialValues.put(KEY_DESCRIPTION,description);
        initialValues.put(KEY_CONTACT,contact);
        initialValues.put(KEY_EMAIL,email);
        initialValues.put(KEY_ADDRESS,address);
        initialValues.put(KEY_IMAGE,image);
        mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    //public List<String> getInfo(String username){
    // Cursor cursor = mDb.q
    //}

    //更新数据库
    public void updateInfo(String id,String itemname,String type,String date,String price,String description,String contact,String email, String address,String image){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ITEMNAME,itemname);
        cv.put(KEY_TYPE,type);
        cv.put(KEY_DATE,date);
        cv.put(KEY_PRICE,price);
        cv.put(KEY_DESCRIPTION,description);
        cv.put(KEY_CONTACT,contact);
        cv.put(KEY_EMAIL,email);
        cv.put(KEY_ADDRESS,address);

        cv.put(KEY_IMAGE,image);
        mDb.update(DATABASE_TABLE,cv,"_id=?",new String[]{id});
    }
    public void updateExImage(String id,String itemname,String type,String date,String price,String description,String contact,String email, String address){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ITEMNAME,itemname);
        cv.put(KEY_TYPE,type);
        cv.put(KEY_DATE,date);
        cv.put(KEY_PRICE,price);
        cv.put(KEY_DESCRIPTION,description);
        cv.put(KEY_CONTACT,contact);
        cv.put(KEY_EMAIL,email);
        cv.put(KEY_ADDRESS,address);

        //cv.put(KEY_IMAGE,image);
        mDb.update(DATABASE_TABLE,cv,"_id=?",new String[]{id});
    }

    //查询所有，无需参数
    public Cursor getAll() {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,KEY_USERNAME,KEY_ITEMNAME,
                KEY_TYPE, KEY_DATE, KEY_PRICE, KEY_DESCRIPTION, KEY_CONTACT, KEY_EMAIL, KEY_ADDRESS,KEY_IMAGE }, null, null, null, null, null);
    }
    //查询username为特定值时的表
    public Cursor getAllNotes(String username) {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,KEY_USERNAME,KEY_ITEMNAME,
                KEY_TYPE, KEY_DATE, KEY_PRICE, KEY_DESCRIPTION, KEY_CONTACT, KEY_EMAIL, KEY_ADDRESS, KEY_IMAGE }, "username=?", new String[] {username}, null, null, null);
    }
    public Cursor getAllNotesByid(String id) {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,KEY_USERNAME,KEY_ITEMNAME,
                KEY_TYPE, KEY_DATE, KEY_PRICE, KEY_DESCRIPTION, KEY_CONTACT, KEY_EMAIL, KEY_ADDRESS,KEY_IMAGE }, "_id=?", new String[] {id}, null, null, null);
    }

    public Cursor getAllNotesByType(String type) {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,KEY_USERNAME,KEY_ITEMNAME,
                KEY_TYPE, KEY_DATE, KEY_PRICE, KEY_DESCRIPTION, KEY_CONTACT, KEY_EMAIL, KEY_ADDRESS,KEY_IMAGE }, "type=?", new String[] {type}, null, null, null);
    }
    public Cursor getAllNotesByTypeName(String type,String username) {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,KEY_USERNAME,KEY_ITEMNAME,
                KEY_TYPE, KEY_DATE, KEY_PRICE, KEY_DESCRIPTION, KEY_CONTACT, KEY_EMAIL, KEY_ADDRESS,KEY_IMAGE }, "type=? and username=?", new String[] {type,username}, null, null, null);
    }
    public Cursor getDiary(String username) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_ITEMNAME}, KEY_ITEMNAME + "='" + username+"'", null, null,
                        null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    // 删除一条数据
    public boolean delete(int id) {
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        try {
            mDb.delete(DATABASE_TABLE, whereClause, whereArgs);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
    public int getCountAll(){
        Cursor c = mDb.rawQuery("select count(*) from items",null);//select count(*) fron items
        //Cursor c = mDb.rawQuery("select count(*) from items",null);
        if (c != null && c.moveToFirst()) {
            int count = c.getInt(0);
            c.close();
            return  count;

        }
        else
            c.close();
        return  0;
    }
    public int getCount(String index){
        Cursor c = mDb.rawQuery("select count(*) from items where username=?",new String[]{index});//select count(*) fron items
        //Cursor c = mDb.rawQuery("select count(*) from items",null);
        if (c != null && c.moveToFirst()) {
            int count = c.getInt(0);
            c.close();
            return  count;

        }
        else
        c.close();
            return  0;
    }
    public int getCountByType(String index){
        Cursor c = mDb.rawQuery("select count(*) from items where type=?",new String[]{index});//select count(*) fron items
        //Cursor c = mDb.rawQuery("select count(*) from items",null);
        if (c != null && c.moveToFirst()) {
            int count = c.getInt(0);
            c.close();
            return  count;

        }
        else
            c.close();
        return  0;
    }
    public Cursor getUnPicIn(String itemname) {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_ITEMNAME,
                KEY_IMAGE,KEY_USERNAME }, "itemname=?", new String[]{itemname}, null, null, null);
    }
    }

