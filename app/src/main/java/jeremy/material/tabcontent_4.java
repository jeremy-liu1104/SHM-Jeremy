package jeremy.material;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Jeremy_Liu on 2016/5/15.
 */
public class tabcontent_4 extends Fragment {
    private ItemDbAdapter mDb;
    private EditText itemname;
    private EditText price;
    private EditText contact;
    private EditText email;
    private EditText address;
    private EditText description;
    private EditText type;
    private EditText date;
    private ImageView pic;
    private static int SELECT_IMAGE = 1;
    public static final int RESULT_OK  = -1;
    private String picturePath="";
    private TextView text_on_pic;
    private Uri selectedImage;
    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;
    private Spinner spinner = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabcontent_4,container,false);
        final String username = ((MainActivity)getActivity()).getUsername();
        itemname = (EditText) view.findViewById(R.id.edit_itemname);
        date = (EditText) view.findViewById(R.id.edit_date);
        //type = (EditText) view.findViewById(R.id.edit_type);
        price = (EditText) view.findViewById(R.id.edit_price);
        description = (EditText) view.findViewById(R.id.edit_description);
        contact = (EditText) view.findViewById(R.id.edit_contact);
        email = (EditText) view.findViewById(R.id.edit_email);
        address = (EditText) view.findViewById(R.id.edit_address);
        pic = (ImageView)view.findViewById(R.id.image_pic);
        text_on_pic = (TextView)view.findViewById(R.id.text_picture);
        Button save_button = (Button)view.findViewById(R.id.button_save_item);
        ImageView date_select = (ImageView)view.findViewById(R.id.date_select);


        ArrayAdapter<String> adapter = null;
        final String [] types ={"Clothes","Tool","Electronic","Furniture","Vehicle","Leisure","Other"};
        spinner = (Spinner)view.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,types);
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
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
        save_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDb = new ItemDbAdapter(getActivity());
                        mDb.open();

                        mDb.createUser(username,itemname.getText().toString(),spinner.getSelectedItem().toString(),date.getText().toString(),price.getText().toString(),description.getText().toString(),contact.getText().toString(),email.getText().toString(),address.getText().toString(),picturePath);
                        mDb.closeclose();
                        Toast.makeText(v.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                        clearAll();
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

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //ava.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            pic.setBackground(Drawable.createFromPath(picturePath));
            text_on_pic.setText("");
            //email.setText(picturePath);
        }
    }
    public void clearAll(){
        itemname.setText("");
        date.setText("");
        //type.setText("");
        price.setText("");
        description.setText("");
        contact.setText("");
        email.setText("");
        address.setText("");
        pic.setBackground(Drawable.createFromPath(""));
        picturePath="";
        text_on_pic.setText("Picture");
    }

}
