package jeremy.material;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Hall_detail extends ActionBarActivity {
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
    private Button share;
    private Button back;
    private String username;
    private String from;
    String image1;
    public static final String APP_ID = "wx80e64619ff085393";
    private IWXAPI api;
    private String share_WeChat;
    ArrayList<ArrayList<String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_detail);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("clickid");
        username = intent.getStringExtra("username");
        from = intent.getStringExtra("from");

        //wechat
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        //registe app_id into WeChat
        api.registerApp(APP_ID);

        //listData = new ArrayList<>();
        //mDb = new ItemDbAdapter(this);
        //mDb.open();
        //Cursor cursor = mDb.getAllNotesByid(id);
       //while (cursor.moveToNext()){
            //ArrayList<String> arrayList = new ArrayList<String>();

            //arrayList.add("Hi! I want to sell "+cursor.getString(cursor.getColumnIndex("itemname"))+" ");
            //arrayList.add("The price is "+cursor.getString(cursor.getColumnIndex("price"))+" ");
            //arrayList.add("For more information, please contact "+cursor.getString(cursor.getColumnIndex("email"))+" ");

            //listData.add(arrayList);
          // final String share_WeChat = "Hi! Iw want to sell "+cursor.getString(cursor.getColumnIndex("itemname"))+".";
                    //+cursor.getString(cursor.getColumnIndex("price"))+" SEK. Please contact "
                    //+cursor.getString(cursor.getColumnIndex("email"));
        //}
        //username = intent.getStringExtra("username");
        //from = intent.getStringExtra("from");

        itemname = (TextView)findViewById(R.id.text_itemname_id2);
        date = (TextView)findViewById(R.id.textview_itemdate2);
        type = (TextView)findViewById(R.id.text_itemtype_id2);
        price = (TextView)findViewById(R.id.textview_itemprice2);
        description = (TextView)findViewById(R.id.textView_itemdescription2);
        contact = (TextView)findViewById(R.id.textview_itemcontact2);
        email = (TextView)findViewById(R.id.textview_itememail2);
        address = (TextView)findViewById(R.id.textview_itemaddress2);
        pic = (ImageView)findViewById(R.id.image_item2);


        share = (Button)findViewById(R.id.login_edit_btn_id3);
        back = (Button)findViewById(R.id.login_edit_btn_id4);
        readInfo(id);
        //itemname.setText(username+"\n"+from+"\n"+id);
        pic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = LayoutInflater.from(Hall_detail.this);
                        View imgEntryView = inflater.inflate(R.layout.full_image, null); // 加载自定义的布局文件
                        final AlertDialog dialog = new AlertDialog.Builder(Hall_detail.this).create();
                        ImageView img = (ImageView)imgEntryView.findViewById(R.id.fullimage);
                        img.setBackground(Drawable.createFromPath(image1));
                        dialog.setView(imgEntryView); // 自定义dialog
                        dialog.show();
// 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                        imgEntryView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                dialog.cancel();
                            }
                        });
                    }
                }
        );
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Back", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        share.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!username.matches(from)){
                                            new AlertDialog.Builder(Hall_detail.this)
                    /* 弹出窗口的最上头文字 */
                        .setTitle("WARNING")
                    /* 设置弹出窗口的图式 */
                        .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                        .setMessage("This stuff does not belong to you.Are you sure to share it to your friends?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                       //留给微信API分享
                                        Send_to_WeChat(id);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        // 什么也没做

                                    }
                                }).show();
                        }
                        else
                        //如果身份相同，就直接跳往分享界面
                            Send_to_WeChat(id);
                    }
                }
        );
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
            image1 = cursor.getString(cursor.getColumnIndex("image"));
            itemname.setText(name1);
            price.setText(price1);
            type.setText(type1);
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

    //for api part
    //1.make bitmap into byte array
    private byte[] bmptoByte(final Bitmap bitmap, final boolean needRecycle){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if(needRecycle){
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try{
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private String buildTran(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //2.send to WeChat
    private void Send_to_WeChat(String id){
        String number = id;
        if (number == null || number.length() == 0) {
            return;
        }

        //send text only
        /*WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        //send txt to WeChat
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject= textObject;
        msg.description = text;

        //request to client
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTran("Text");
        req.message = msg;

        //send to TimeLine
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        //send to WeChat
        Toast.makeText(Hall_detail.this, String.valueOf(api.sendReq(req)), Toast.LENGTH_LONG).show();
        api.sendReq(req);*/


        //send image only
        /*mDb = new ItemDbAdapter(this);
        mDb.open();
        Cursor cursor = mDb.getAllNotesByid(id);
        while(cursor.moveToNext()){
            image1 = cursor.getString(cursor.getColumnIndex("image"));
        }
        Bitmap bitmap = BitmapFactory.decodeFile(image1);
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description = text;
        msg.title = text;

        //scale bitmap size
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 150, true);
        bitmap.recycle();
        msg.thumbData = bmptoByte(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTran("Image");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        Toast.makeText(this,String.valueOf(api.sendReq(req)),Toast.LENGTH_LONG).show();*/

        //send URL
        WXWebpageObject webpage = new WXWebpageObject();
        String url = "www.baidu.com";
        webpage.webpageUrl = url;

        mDb = new ItemDbAdapter(this);
        mDb.open();
        Cursor cursor = mDb.getAllNotesByid(id);
        while(cursor.moveToNext()){
            image1 = cursor.getString(cursor.getColumnIndex("image"));
            share_WeChat = "Hi! I want to sell "+cursor.getString(cursor.getColumnIndex("itemname"))+" with "+cursor.getString(cursor.getColumnIndex("price"))
                            +"SEK. Please contact: "+cursor.getString(cursor.getColumnIndex("email"))+".";
        }

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share_WeChat;
        msg.description = share_WeChat;

        Bitmap bitmap = BitmapFactory.decodeFile(image1);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 150, true);
        bitmap.recycle();
        msg.thumbData = bmptoByte(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTran("Webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        Toast.makeText(this,String.valueOf(api.sendReq(req)),Toast.LENGTH_LONG).show();


    }
}
