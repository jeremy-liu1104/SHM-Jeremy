package jeremy.material;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeremy.material.R;

public class MainActivity_Login extends Activity {
    AutoCompleteTextView cardNumAuto;
    EditText passwordET;
    Button logBT;
    Button regBT;
    private SplashScreen mSplashScreen;
    private Handler mHandler;
    CheckBox savePasswordCB;
    SharedPreferences sp;
    String cardNumStr;
    String passwordStr;
    private UserDbAdapter mDbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
        passwordET = (EditText) findViewById(R.id.passwordET);
        logBT = (Button) findViewById(R.id.logBT);
        regBT = (Button) findViewById(R.id.resBT);
        regBT.setOnClickListener(register_button_listener);

        sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
        savePasswordCB = (CheckBox) findViewById(R.id.savePasswordCB);
        savePasswordCB.setChecked(true);// 默认为记住密码
        cardNumAuto.setThreshold(1);// 输入1个字母就开始自动提示
        passwordET.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81
        // 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        mSplashScreen = new SplashScreen(this);
        mSplashScreen.show(R.drawable.image_splash_background,
                SplashScreen.SLIDE_LEFT);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mSplashScreen.removeSplashScreen();
            }
        };
        splashTime();

        cardNumAuto.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
                allUserName = sp.getAll().keySet().toArray(new String[0]);
                // sp.getAll()返回一张hash map
                // keySet()得到的是a set of the keys.
                // hash map是由key-value组成的

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MainActivity_Login.this,
                        android.R.layout.simple_dropdown_item_1line,
                        allUserName);

                cardNumAuto.setAdapter(adapter);// 设置数据适配器

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordET.setText(sp.getString(cardNumAuto.getText()
                        .toString(), ""));// 自动输入密码

            }
        });

        // 登陆
        logBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cardNumStr = cardNumAuto.getText().toString();
                passwordStr = passwordET.getText().toString();
                if((cardNumStr == null||cardNumStr.equalsIgnoreCase("")) || (passwordStr == null||passwordStr.equalsIgnoreCase(""))){
                    Toast.makeText(MainActivity_Login.this, "The user name and password are necessary.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor = mDbHelper.getDiary(cardNumStr);

                    if(!cursor.moveToFirst()){
                        Toast.makeText(MainActivity_Login.this, "The user name doesn't exist.",
                                Toast.LENGTH_SHORT).show();
                    }else if (!passwordStr.equals(cursor.getString(2))) {
                        Toast.makeText(MainActivity_Login.this, "The password is wrong, please enter again.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (savePasswordCB.isChecked()) {// 登陆成功才保存密码
                            sp.edit().putString(cardNumStr, passwordStr).commit();
                        }
                        Toast.makeText(MainActivity_Login.this, "Login successfully. Wait for getting the information of the user...",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("username", cardNumStr);
                        intent.setClass(MainActivity_Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        Spinner spinner = (Spinner)findViewById(R.id.spinner3);

        String[] user_abstract = null;
        ArrayAdapter<String> adapter = null;
        Cursor cursor = mDbHelper.getUnPic();
        StringBuffer sb = new StringBuffer();
        ArrayList<Map<String,Object>> user = new ArrayList<Map<String, Object>>();
        while (cursor.moveToNext()){
            //list = new String[i+1];
            Map<String,Object> map = new HashMap<String, Object>();
            //sb.append(cursor.getString(cursor.getColumnIndex("username")));
            //sb.append("!");
            map.put("username","User:"+cursor.getString(cursor.getColumnIndex("username")));
            map.put("pic",cursor.getString(cursor.getColumnIndex("image")));
            map.put("id",cursor.getString(cursor.getColumnIndex("_id")));
            user.add(map);
        }
        cursor.close();
        //String data = sb.toString();
        //spinner_abstract = data.split("\\!");
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinner_abstract);
        //设置下拉列表风格
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        UserAdapter Uadapter = new UserAdapter(this,user);
        spinner.setAdapter(Uadapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView username = (TextView)findViewById(R.id.id_name);
                cardNumAuto.setText(username.getText().toString().substring(5));


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

;

    }

    private Button.OnClickListener register_button_listener = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity_Login.this,RegisterActivity.class);
            //intent.setClass(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    };
    private void splashTime() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);
            }

        }).start();
    }

}

