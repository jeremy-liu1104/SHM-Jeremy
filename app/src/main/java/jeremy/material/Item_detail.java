package jeremy.material;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Item_detail extends ActionBarActivity {
    private TextView itemname;
    private TextView type;
    private TextView price;
    private TextView description;
    private TextView contact;
    private TextView email;
    private TextView address;
    private TextView date;
    private ItemDbAdapter mDb;
    private ImageView pic;
    private static int SELECT_IMAGE = 1;
    private Button save;
    private Button back;
    private String picturePath="";
    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;
    private ImageView date_select;
    private Spinner spinner = null;
    final String [] types ={"Clothes","Tool","Electronic","Furniture","Vehicle","Leisure","Other"};
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("clickid");
        itemname = (TextView)findViewById(R.id.text_itemname_id);
        date = (TextView)findViewById(R.id.textview_itemdate);
        //type = (TextView)findViewById(R.id.text_itemtype_id);
        price = (TextView)findViewById(R.id.textview_itemprice);
        description = (TextView)findViewById(R.id.textView_itemdescription);
        contact = (TextView)findViewById(R.id.textview_itemcontact);
        email = (TextView)findViewById(R.id.textview_itememail);
        address = (TextView)findViewById(R.id.textview_itemaddress);
        pic = (ImageView)findViewById(R.id.image_item);
        save = (Button)findViewById(R.id.login_edit_btn_id2);
        date_select = (ImageView)findViewById(R.id.image_date);
        back = (Button)findViewById(R.id.login_edit_btn_id3);


        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Back", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        pic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, SELECT_IMAGE);
                    }
                }
        );


        mDb = new ItemDbAdapter(this);
        mDb.open();
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDb.open();
                        if(picturePath!="") {
                            mDb.updateInfo(id, itemname.getText().toString(), spinner.getSelectedItem().toString(), date.getText().toString(), price.getText().toString(),
                                    description.getText().toString(), contact.getText().toString(), email.getText().toString(), address.getText().toString(), picturePath);
                        }
                        else{
                            mDb.updateExImage(id, itemname.getText().toString(), spinner.getSelectedItem().toString(), date.getText().toString(), price.getText().toString(),
                                    description.getText().toString(), contact.getText().toString(), email.getText().toString(), address.getText().toString());
                        }
                        mDb.closeclose();
                        Toast.makeText(v.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        //ArrayAdapter<String> adapter = null;

        spinner = (Spinner)findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(Item_detail.this,android.R.layout.simple_spinner_item,types);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                //type.setText(((TextView)arg1).getText());


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        date_select.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Item_detail.this, new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, monthOfYear, dayOfMonth);
                        datePickerDialog.show();
                    }
                }
        );
        readInfo(id);

    }
    public  void readInfo(String id){
        mDb = new ItemDbAdapter(this);
        mDb.open();
        Cursor cursor = mDb.getAllNotesByid(id);
        while(cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex("itemname"));
            String date1 = cursor.getString(cursor.getColumnIndex("date"));
            String type1 = cursor.getString(cursor.getColumnIndex("type"));
            String description1 = cursor.getString(cursor.getColumnIndex("description"));
            //String item1 = cursor.getString(cursor.getColumnIndex("item"));
            String price1 = cursor.getString(cursor.getColumnIndex("price"));
            String email1 = cursor.getString(cursor.getColumnIndex("email"));
            String contact1 = cursor.getString(cursor.getColumnIndex("contact"));
            String address1 = cursor.getString(cursor.getColumnIndex("address"));
            String image1 = cursor.getString(cursor.getColumnIndex("image"));
            itemname.setText(name1);
            price.setText(price1);
            //type.setText(type1);
            spinner.setSelection(adapter.getPosition(type1));
            email.setText(email1);
            contact.setText(contact1);
            address.setText(address1);
            date.setText(date1);
            description.setText(description1);
            pic.setBackground(Drawable.createFromPath(image1));
        }
        cursor.close();
        mDb.closeclose();
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
            pic.setBackground(Drawable.createFromPath(picturePath));
            //text_ava.setText("");
            //email.setText(picturePath);
        }
    }
}
