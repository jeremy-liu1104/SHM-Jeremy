package jeremy.material;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Jeremy_Liu on 2016/5/11.
 */
public class tabcontent_1 extends Fragment{
    private UserDbAdapter mDb;
    private ItemDbAdapter mDb2;
    ViewPager mViewPager;
    private SwipeRefreshLayout		swipeRefreshLayout;
    RelativeLayout relativeLayout;
    TextView name;
    TextView id;
    TextView email;
    TextView career;
    TextView address;
    ImageView ava;
    TextView phone;
    TextView item;
    String image1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.tabcontent_1,container,false);
        Button sv_but = (Button)view.findViewById(R.id.login_edit_btn_id);
        name = (TextView)view.findViewById(R.id.text_username_id);
        phone = (TextView)view.findViewById(R.id.text_userphone_id);
        item = (TextView)view.findViewById(R.id.textview_item);
        id = (TextView)view.findViewById(R.id.textview_id);
        email = (TextView)view.findViewById(R.id.textview_email);
        career = (TextView)view.findViewById(R.id.textview_career);
        address = (TextView)view.findViewById(R.id.textview_address);
        ImageView click = (ImageView) view.findViewById(R.id.iamge_click_id);
        ava = (ImageView)view.findViewById(R.id.image_item);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
//        mViewPager.setOffscreenPageLimit(5); // tabcachesize (=tabcount for better performance)
        //mViewPager.setAdapter(new SlidingTabAdapter());


        final String username = ((MainActivity)getActivity()).getUsername();





        getInfo(username);

        ava.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View imgEntryView = inflater.inflate(R.layout.full_image, null); // 加载自定义的布局文件
                        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
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



        //mDb2 = new ItemDbAdapter(getActivity());
        //mDb2.open();
        //Cursor cursor1 = mDb2.getAllNotes(username);
        //HashMap<String, Object> map = new HashMap<String, Object>();
        //int count=0;
        //返回另一个表中某个用户名的个数
        //while(cursor1.moveToNext()){

            //map.put("username", cursor1.getString(cursor1.getColumnIndex("username")));
            //String image1 = cursor.getString(cursor.getColumnIndex("image"));
            //count++;
            //item.setText(count);

            //ava.setBackground(Drawable.createFromPath(image1));
        //}

        //cursor1.close();
        //mDb2.closeclose();
        sv_but.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),UserEdit.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                }
        );


        click.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity_Login.class); // 用法待定，可能指向stuff页面
                        startActivity(intent);
                        getActivity().finish();

                    }
                }
        );

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        //下拉刷新
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                getInfo(username);
                                //((MainActivity)getActivity()).getFresh();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1500);

                    }
                }
        );

        return view;
    }
    public void getInfo(String username){
        mDb2 = new ItemDbAdapter(getActivity());
        mDb2.open();
        String item1 = String.valueOf(mDb2.getCount(username));
        item.setText(item1);
        mDb2.closeclose();
        mDb = new UserDbAdapter(getActivity());
        mDb.open();
        Cursor cursor = mDb.getAllNotes(username);
        while(cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex("name"));
            String phone1 = cursor.getString(cursor.getColumnIndex("phone"));
            //String item1 = cursor.getString(cursor.getColumnIndex("item"));
            String pn1 = cursor.getString(cursor.getColumnIndex("pn"));
            String email1 = cursor.getString(cursor.getColumnIndex("email"));
            String career1 = cursor.getString(cursor.getColumnIndex("career"));
            String address1 = cursor.getString(cursor.getColumnIndex("address"));
            image1 = cursor.getString(cursor.getColumnIndex("image"));
            name.setText(name1);
            phone.setText(phone1);
            //item.setText(((MainActivity)getActivity()).getItemCount());
            id.setText(pn1);
            email.setText(email1);
            career.setText(career1);
            address.setText(address1);
            ava.setBackground(Drawable.createFromPath(image1));
        }
        cursor.close();
        mDb.closeclose();
    }
}
