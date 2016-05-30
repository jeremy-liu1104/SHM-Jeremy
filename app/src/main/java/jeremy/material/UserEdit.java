package jeremy.material;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.RequestDirector;

import SlidingTabs.SlidingTabAdapter;


public class UserEdit extends Activity {

    private ImageView ava;
    private static int SELECT_IMAGE = 1;
    private UserDbAdapter mDb;
    private EditText name;
    private EditText phone;
    private EditText pn;
    private EditText email;
    private EditText address;
    private EditText career;
    private String picturePath="";
    private TextView text_ava;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        text_ava = (TextView)findViewById(R.id.text_ava);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        mDb = new UserDbAdapter(this);
        mDb.open();
        name = (EditText)findViewById(R.id.editText1);
        phone = (EditText)findViewById(R.id.editText2);
        pn = (EditText)findViewById(R.id.editText3);
        email = (EditText)findViewById(R.id.editText4);
        career = (EditText)findViewById(R.id.editText5);
        address = (EditText)findViewById(R.id.editText6);
        readInfo(username);
        ava = (ImageView) findViewById(R.id.imageView);
        Button save_button = (Button)findViewById(R.id.save_button);
        Button back_button = (Button)findViewById(R.id.back_button);
        ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_IMAGE);

            }
        });
        save_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDb.open();
                        if(picturePath!="") {
                            mDb.updateInfo(username, name.getText().toString(), phone.getText().toString(), pn.getText().toString(), email.getText().toString(), career.getText().toString(), address.getText().toString(), picturePath);
                        }
                        else{
                            mDb.updateExImage(username, name.getText().toString(), phone.getText().toString(), pn.getText().toString(), email.getText().toString(), career.getText().toString(), address.getText().toString());
                        }
                        //Intent intent1 = new Intent(UserEdit.this,MainActivity.class);
                        //startActivity(intent1);
                        Toast.makeText(v.getContext(), "已保存", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        back_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "已返回   " +
                                "oo" +
                                "    " +
                                "" +
                                "" +
                                "", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //ava.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            ava.setBackground(Drawable.createFromPath(picturePath));
            text_ava.setText("");
            //email.setText(picturePath);
        }
    }

    public void readInfo(String username){
        Cursor cursor = mDb.getAllNotes(username);
        while(cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex("name"));
            String phone1 = cursor.getString(cursor.getColumnIndex("phone"));
            //String item1 = cursor.getString(cursor.getColumnIndex("item"));
            String pn1 = cursor.getString(cursor.getColumnIndex("pn"));
            String email1 = cursor.getString(cursor.getColumnIndex("email"));
            String career1 = cursor.getString(cursor.getColumnIndex("career"));
            String address1 = cursor.getString(cursor.getColumnIndex("address"));
            String image1 = cursor.getString(cursor.getColumnIndex("image"));
            name.setText(name1);
            phone.setText(phone1);
            pn.setText(pn1);
            email.setText(email1);
            career.setText(career1);
            address.setText(address1);
            //ava.setBackground(Drawable.createFromPath(image1));
        }
        cursor.close();
    }
}
