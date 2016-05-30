package jeremy.material;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;




/**
 * Created by Jeremy_Liu on 2016/5/15.
 */
public class tabcontent_2 extends Fragment {
    ItemDbAdapter mDb;
    final String[] types = {"Type","Tool", "Electronic", "Furniture","Transport","Leisure","Other"};
    int[] EXPANDABLE_LISTVIEW_IMG = new int[]{
            R.drawable.ic_category_40, R.drawable.ic_category_50,
            R.drawable.ic_category_45, R.drawable.ic_category_60,
            R.drawable.ic_category_65, R.drawable.ic_category_30,
            R.drawable.ic_category_70};
    // 存储数据的数组列表
    ArrayList<HashMap<String, Object>> listData;
    // 适配器
    SimpleAdapter listItemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout2;
    private String ItemID;
    private TextView textView;
    public static String str = null; // 将StringBuffer转化成字符串
    public static StringBuffer sb = new StringBuffer(); // StringBuffer便于字符串的增删改查操作
    //private String username = ((MainActivity)getActivity()).getUsername();
    /**
     * 定义可扩展的listView
     */
    private ExpandableListView expandableListView;

    /**
     * 图片指示回退
     */
    private ImageView backImage;

    /**
     * 定义可扩展listView的适配器
     */

    private ExpandableListAdapter adapter;

    private List<Map<String, Object>> list;
    private List<Map<String, Object>> sublist;


    //private String[][] textArray;
    private ArrayList<Map<String, Object>>[][] listdataB;
    private EditText test;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tabcontent_2, container, false);

        textView = (TextView) view.findViewById(R.id.text_id);
        final String username = ((MainActivity) getActivity()).getUsername();

        //expandableListView = (ExpandableListView) view.findViewById(R.id.ExpandableListView);


        //this.deleteDatabase("users.db");//先删除原表，不然会一直添加数据
        mDb = new ItemDbAdapter(getActivity());
        mDb.open();

        final ListView list = (ListView) view.findViewById(R.id.list_items);

       // this.getActivity().registerForContextMenu(view.findViewById(R.id.list_items));//用于长按跳出菜单

        listData = new ArrayList<HashMap<String, Object>>();
        //test = (EditText)view.findViewById(R.id.test);
        getInfo(username);
        // 初始化数据
        //initData();

//        SourceDateList = filledData(listData);
//        adapter = new SortAdapter(getActivity(), SourceDateList);
//        list.setAdapter(adapter);
        listItemAdapter = new SimpleAdapter(getActivity(),
                listData,// 数据源
                R.layout.item,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[]{"id", "image", "itemname", "date", "from", "price", "type"},
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.text_id, R.id.image, R.id.itemname, R.id.date, R.id.text_contact, R.id.text_price, R.id.type});
        list.setAdapter(listItemAdapter);

        setListViewHeight(list);
        //setListViewHeight(expandableListView);
        // 给一级菜单的选项设置监听
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v,
//                                        int groupPosition, long id) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//        });
//
//        // 给一级菜单下面的二级菜单的选项设置监听接口
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                // TODO Auto-generated method stub
//                textView = (TextView) view.findViewById(R.id.text_id);
//                //HashMap<String, Object> map = new HashMap<String, Object>();
//                //String itemid = (map.get("id").toString()).substring(3);
//                String itemid = (textView.getText().toString().substring(3));
//                if(itemid.matches("")){
//                    Toast.makeText(view.getContext(), "This is a SAMPLE,cannot be edited.", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//                else {
//                    Intent intent = new Intent(getActivity(), Item_detail.class);
//                    intent.putExtra("clickid", itemid);
//                    startActivity(intent);
//                }
//                return false;
//            }
//        });
//        expandableListView.setOnItemLongClickListener(
//                new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
//                        //final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//                        new AlertDialog.Builder(getActivity())
//                    /* 弹出窗口的最上头文字 */
//                                .setTitle("DELETE THIS ITEM")
//                    /* 设置弹出窗口的图式 */
//                                .setIcon(android.R.drawable.ic_dialog_info)
//                    /* 设置弹出窗口的信息 */
//                                .setMessage("Are you sure to delete it?")
//                                .setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(
//                                                    DialogInterface dialoginterface, int i) {
//
//                                                // 获取位置索引
//                                                //int mListPos = info.position;
//                                                // 获取对应HashMap数据内容
//                                                //HashMap<String, Object> map = listData.get(mListPos);
//                                                // 获取id
//                                                //int id = Integer.valueOf((map.get("id").toString()).substring(3));
//                                                textView = (TextView) view.findViewById(R.id.text_id);
//                                                String id = (textView.getText().toString().substring(3));
//                                                if(id.matches("")){
//                                                    Toast.makeText(view.getContext(), "This is a SAMPLE,cannot be deleted.", Toast.LENGTH_SHORT).show();
//                                                }
//                                                // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
//                                                else {
//                                                    mDb.open();
//                                                    mDb.delete(Integer.parseInt(id));
//                                                    if (mDb.delete(Integer.parseInt(id))) {
//                                                        // 移除listData的数据
//                                                        //listData.remove(mListPos);
//                                                        //listItemAdapter.notifyDataSetChanged();
//                                                        initData();
//                                                    }
//                                                }
//                                            }
//                                        })
//                                .setNegativeButton("No",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(
//                                                    DialogInterface dialoginterface, int i) {
//                                                // 什么也没做
//
//                                            }
//                                        }).show();
//                        return false;
//                    }
//                }
//        );

        //点击事件
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        textView = (TextView) view.findViewById(R.id.text_id);
                        //HashMap<String, Object> map = new HashMap<String, Object>();
                        //String itemid = (map.get("id").toString()).substring(3);
                        String itemid = (textView.getText().toString()).substring(3);
                        Intent intent = new Intent(getActivity(), Item_detail.class);
                        intent.putExtra("clickid", itemid);
                        startActivity(intent);
                    }
                }
        );


        //长按事件
        // 长按事件响应
        View.OnCreateContextMenuListener listviewLongPress = new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
//                // TODO Auto-generated method stub
                menu.add(0, 1, Menu.NONE, "Edit");
                menu.add(0, 2, Menu.NONE, "Delete");
//
//                final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//                new AlertDialog.Builder(getActivity())
//                    /* 弹出窗口的最上头文字 */
//                        .setTitle("删除当前数据")
//                    /* 设置弹出窗口的图式 */
//                        .setIcon(android.R.drawable.ic_dialog_info)
//                    /* 设置弹出窗口的信息 */
//                        .setMessage("确定删除当前记录")
//                        .setPositiveButton("是",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(
//                                            DialogInterface dialoginterface, int i) {
//
//                                        // 获取位置索引
//                                        int mListPos = info.position;
//                                        // 获取对应HashMap数据内容
//                                        HashMap<String, Object> map = listData
//                                                .get(mListPos);
//                                        // 获取id
//                                        int id = Integer.valueOf((map.get("id").toString()).substring(3));
//                                        //String id = (textView.getText().toString()).substring(3);
//                                        // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
//                                        mDb.delete(id);
//                                        if (mDb.delete(id)) {
//                                            // 移除listData的数据
//                                            listData.remove(mListPos);
//                                            listItemAdapter.notifyDataSetChanged();
//                                        }
//                                    }
//                                })
//                        .setNegativeButton("否",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(
//                                            DialogInterface dialoginterface, int i) {
//                                        // 什么也没做
//
//                                    }
//                                }).show();
            }
        };
        list.setOnCreateContextMenuListener(listviewLongPress);
        //expandableListView.setOnCreateContextMenuListener(listviewLongPress);

        swipeRefreshLayout2 = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout2);
        //���þ��ڵ���ɫ
        swipeRefreshLayout2.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout2.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //new Handler().postDelayed(new Runnable() {
                        //public void run() {
                        listData.clear();

                        //initData();

                        getInfo(username);
//                                SourceDateList = filledData(listData);
//                                adapter = new SortAdapter(getActivity(), SourceDateList);

                        setListViewHeight(list);
                        //setListViewHeight(expandableListView);
                        listItemAdapter.notifyDataSetChanged();
                        //list.setAdapter(adapter);

                        swipeRefreshLayout2.setRefreshing(false);
                        //}
                        //}, 1500);
                    }
                }
        );


        return view;
    }

    public void getInfo(String username) {
        mDb.open();
        Cursor cursor = mDb.getAllNotes(username);
        while (cursor.moveToNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", "IN:" + cursor.getString(cursor.getColumnIndex("_id")));
            map.put("itemname", "Item:" + cursor.getString(cursor.getColumnIndex("itemname")));
            map.put("date", "Date:" + cursor.getString(cursor.getColumnIndex("date")));
            map.put("image", cursor.getString(cursor.getColumnIndex("image")));
            map.put("from", "From:" + cursor.getString(cursor.getColumnIndex("username")));
            map.put("price", "Price:" + cursor.getString(cursor.getColumnIndex("price")));
            map.put("type", cursor.getString(cursor.getColumnIndex("type")));
            listData.add(map);


            //((MainActivity)getActivity()).setItemcount(map.);
        }
        cursor.close();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();//获得AdapterContextMenuInfo,以此来获得选择的listview项
        //ExpandableListView.ExpandableListContextMenuInfo menuInfo = (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
        ItemID= (String) listData.get(menuInfo.position).get("ID");

        int getPosition = menuInfo.position;
        switch (item.getItemId()) {                    //点击不同项 所触发的不同事件
            case 1:
                //String itemid = (textView.getText().toString()).substring(3);
                //final ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
                //int id = info.targetView.te;
                //HashMap<String, Object> map = listData.get(mListPos);
                //String itemid = (map.get("id").toString());
                String itemid = (textView.getText().toString().substring(3));
                Intent intent = new Intent(getActivity(), Item_detail.class);
                intent.putExtra("clickid", itemid);
                startActivity(intent);

//        Bundle sentId=new Bundle();
//     sentId.putString("ID", ItemID);
//     intent.putExtras(sentId);

                break;
            case 2:

                final AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) menuInfo;
                new AlertDialog.Builder(getActivity())
                    /* 弹出窗口的最上头文字 */
                        .setTitle("Delete")
                    /* 设置弹出窗口的图式 */
                        .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                        .setMessage("Do you want to remove this item???")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        // 获取位置索引

                                        int mListPos = info2.position;
                                        // 获取对应HashMap数据内容
                                        HashMap<String, Object> map = listData.get(mListPos);
                                        // 获取id
                                        int id = Integer.valueOf((map.get("id").toString()).substring(3));
                                        //String itemid = (textView.getText().toString().substring(3));
                                        // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                                        mDb.open();
                                        if (mDb.delete(id)) {
                                            // 移除listData的数据
                                            listData.remove(mListPos);
                                            listItemAdapter.notifyDataSetChanged();
                                            //initData();
                                        }
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        // 什么也没做

                                    }
                                }).show();                           //删除
                break;
        }

        return true;
    }


    public void setListViewHeight(ListView listView) {
        mDb = new ItemDbAdapter(getActivity());
        mDb.open();
// 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount            ()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        mDb.closeclose();
        listView.setLayoutParams(params);
    }

    // 初始化数据的填充
    private void initData() {
        mDb = new ItemDbAdapter(getActivity());
        mDb.open();
        // TODO Auto-generated method stub
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", EXPANDABLE_LISTVIEW_IMG[i]);
            map.put("txt", types[i]);
            list.add(map);
        }
        // 获取表的内容
        String[][] textArray = new String[types.length][mDb.getCountAll()];
        for (int i = 0; i < types.length; i++) {
            Cursor cursor = mDb.getAllNotesByTypeName(types[i],((MainActivity)getActivity()).getUsername());
            while (cursor.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();

                sb.append(cursor.getString(cursor.getColumnIndex("_id")));
                sb.append("@"); // 在每列数据后面做标记，将来便于做拆分
                sb.append(cursor.getString(cursor.getColumnIndex("itemname")));
                sb.append("@"); // 在每列数据后面做标记，将来便于做拆分
                sb.append(cursor.getString(cursor.getColumnIndex("date")));
                sb.append("@"); // 在每列数据后面做标记，将来便于做拆分
                sb.append(cursor.getString(cursor.getColumnIndex("image")));
                sb.append("@"); // 在每列数据后面做标记，将来便于做拆分
                sb.append(cursor.getString(cursor.getColumnIndex("username")));
                sb.append("@"); // 在每列数据后面做标记，将来便于做拆分
                sb.append(cursor.getString(cursor.getColumnIndex("price")));
                sb.append("@"); // 在每列数据后面做标记，将来便于做拆分
                sb.append(cursor.getString(cursor.getColumnIndex("type")));
                sb.append("$"); // 在每列数据后面做标记，将来便于做拆分

//                textArray[i][j] = cursor.getString(cursor.getColumnIndex("_id")) +" "+ cursor.getString(cursor.getColumnIndex("itemname")) + " "+
//                        cursor.getString(cursor.getColumnIndex("date")) +" "+ cursor.getString(cursor.getColumnIndex("image")) + " "+
//                        cursor.getString(cursor.getColumnIndex("username")) +" "+ cursor.getString(cursor.getColumnIndex("price")) + " "+
//                        cursor.getString(cursor.getColumnIndex("type"));
                //textArray[i][j]="a b c d e f g";
                //((MainActivity)getActivity()).setItemcount(map.);

            }
            cursor.close();
            sb.append("%");
            for(int j = 0;j<mDb.getCountAll();j++){
                textArray[i][j]="*"; //初始化二维数组
            }


        }
        str = sb.toString();
        sb.delete(0,sb.length());
        String[] Type = null;
        String[] Tool = null;
        String[] Electronic = null;
        String[] Furniture = null;
        String[] Transport = null;
        String[] Leisure = null;
        String[] Other = null;
        String[] category = str.split("\\%");

        if(category.length==1){
            Type = category[0].split("\\$");
            if(Type!=null) {
                if(Type.length==0){
                    //Type=null;
                }
                else {
                    for (int i = 0; i < Type.length; i++) {
                        textArray[0][i] = Type[i];
                    }
                }
            }
        }
        else if(category.length==2){
            Type = category[0].split("\\$");
            Tool = category[1].split("\\$");
            if(Type!=null) {
                if(category[0].matches("")){
                    Type=null;
                }
                else {
                    for (int i = 0; i < Type.length; i++) {
                        textArray[0][i] = Type[i];
                    }
                }
            }
            if(Tool!=null) {
                if(Tool.length==0){
                    //Tool=null;
                }
                else {
                    for (int i = 0; i < Tool.length; i++) {
                        textArray[1][i] = Tool[i];
                    }
                }
            }
        }
        else if(category.length==3){
            Type = category[0].split("\\$");
            Tool = category[1].split("\\$");
            Electronic = category[2].split("\\$");
            if(Type!=null) {
                if(category[0].matches("")){
                    Type=null;
                }
                else {
                    for (int i = 0; i < Type.length; i++) {
                        textArray[0][i] = Type[i];
                    }
                }
            }
            if(Tool!=null) {
                if(category[1].matches("")){
                    Tool=null;
                }
                else {
                    for (int i = 0; i < Tool.length; i++) {
                        textArray[1][i] = Tool[i];
                    }
                }
            }
            if(Electronic!=null) {
                if(Electronic.length==0){
                    //Electronic=null;
                }else {
                    for (int i = 0; i < Electronic.length; i++) {
                        textArray[2][i] = Electronic[i];
                    }
                }
            }
        }
        else if(category.length==4){
            Type = category[0].split("\\$");
            Tool = category[1].split("\\$");
            Electronic = category[2].split("\\$");
            Furniture = category[3].split("\\$");
            if(Type!=null) {
                if(category[0].matches("")){
                    Type=null;
                }
                else {
                    for (int i = 0; i < Type.length; i++) {
                        textArray[0][i] = Type[i];
                    }
                }
            }
            if(Tool!=null) {
                if(category[1].matches("")){
                    Tool=null;
                }
                else {
                    for (int i = 0; i < Tool.length; i++) {
                        textArray[1][i] = Tool[i];
                    }
                }
            }
            if(Electronic!=null) {
                if(category[2].matches("")){
                    Electronic=null;
                }else {
                    for (int i = 0; i < Electronic.length; i++) {
                        textArray[2][i] = Electronic[i];
                    }
                }
            }
            if(Furniture!=null) {
                if(Furniture.length==0){
                    //Furniture=null;
                }else {
                    for (int i = 0; i < Furniture.length; i++) {
                        textArray[3][i] = Furniture[i];
                    }
                }
            }
        }
        else if(category.length==5){
            Type = category[0].split("\\$");
            Tool = category[1].split("\\$");
            Electronic = category[2].split("\\$");
            Furniture = category[3].split("\\$");
            Transport = category[4].split("\\$");
            if(Type!=null) {
                if(category[0].matches("")){
                    Type=null;
                }
                else {
                    for (int i = 0; i < Type.length; i++) {
                        textArray[0][i] = Type[i];
                    }
                }
            }
            if(Tool!=null) {
                if(category[1].matches("")){
                    Tool=null;
                }
                else {
                    for (int i = 0; i < Tool.length; i++) {
                        textArray[1][i] = Tool[i];
                    }
                }
            }
            if(Electronic!=null) {
                if(category[2].matches("")){
                    Electronic=null;
                }else {
                    for (int i = 0; i < Electronic.length; i++) {
                        textArray[2][i] = Electronic[i];
                    }
                }
            }
            if(Furniture!=null) {
                if(category[3].matches("")){
                    Furniture=null;
                }else {
                    for (int i = 0; i < Furniture.length; i++) {
                        textArray[3][i] = Furniture[i];
                    }
                }
            }
            if(Transport!=null) {
                if(Transport.length==0){
                    //Transport=null;
                }else {
                    for (int i = 0; i < Transport.length; i++) {
                        textArray[4][i] = Transport[i];
                    }
                }
            }
        }
        else if(category.length==6){
            Type = category[0].split("\\$");
            Tool = category[1].split("\\$");
            Electronic = category[2].split("\\$");
            Furniture = category[3].split("\\$");
            Transport = category[4].split("\\$");
            Leisure = category[5].split("\\$");
            if(Type!=null) {
                if(category[0].matches("")){
                    Type=null;
                }
                else {
                    for (int i = 0; i < Type.length; i++) {
                        textArray[0][i] = Type[i];
                    }
                }
            }
            if(Tool!=null) {
                if(category[1].matches("")){
                    Tool=null;
                }
                else {
                    for (int i = 0; i < Tool.length; i++) {
                        textArray[1][i] = Tool[i];
                    }
                }
            }
            if(Electronic!=null) {
                if(category[2].matches("")){
                    Electronic=null;
                }else {
                    for (int i = 0; i < Electronic.length; i++) {
                        textArray[2][i] = Electronic[i];
                    }
                }
            }
            if(Furniture!=null) {
                if(category[3].matches("")){
                    Furniture=null;
                }else {
                    for (int i = 0; i < Furniture.length; i++) {
                        textArray[3][i] = Furniture[i];
                    }
                }
            }
            if(Transport!=null) {
                if(category[4].matches("")){
                    Transport=null;
                }else {
                    for (int i = 0; i < Transport.length; i++) {
                        textArray[4][i] = Transport[i];
                    }
                }
            }
            if(Leisure!=null) {
                if(Leisure.length==0){
                    //Leisure=null;
                }else {
                    for (int i = 0; i < Leisure.length; i++) {
                        textArray[5][i] = Leisure[i];
                    }
                }
            }
        }
        else if(category.length==7) {
                Type = category[0].split("\\$");
                Tool = category[1].split("\\$");
                Electronic = category[2].split("\\$");
                Furniture = category[3].split("\\$");
                Transport = category[4].split("\\$");
                Leisure = category[5].split("\\$");
                Other = category[6].split("\\$");
            if(Type!=null) {
                if(category[0].matches("")){
                    Type=null;
                }
                else {
                    for (int i = 0; i < Type.length; i++) {
                        textArray[0][i] = Type[i];
                    }
                }
            }
            if(Tool!=null) {
                if(category[1].matches("")){
                    Tool=null;
                }
                else {
                    for (int i = 0; i < Tool.length; i++) {
                        textArray[1][i] = Tool[i];
                    }
                }
            }
            if(Electronic!=null) {
                if(category[2].matches("")){
                    Electronic=null;
                }else {
                    for (int i = 0; i < Electronic.length; i++) {
                        textArray[2][i] = Electronic[i];
                    }
                }
            }
            if(Furniture!=null) {
                if(category[3].matches("")){
                    Furniture=null;
                }else {
                    for (int i = 0; i < Furniture.length; i++) {
                        textArray[3][i] = Furniture[i];
                    }
                }
            }
            if(Transport!=null) {
                if(category[4].matches("")){
                    Transport=null;
                }else {
                    for (int i = 0; i < Transport.length; i++) {
                        textArray[4][i] = Transport[i];
                    }
                }
            }
            if(Leisure!=null) {
                if(category[5].matches("")){
                    Leisure=null;
                }else {
                    for (int i = 0; i < Leisure.length; i++) {
                        textArray[5][i] = Leisure[i];
                    }
                }
            }
            if(Other!=null) {
                if(Other.length==0){
                    //Other=null;
                }else {
                    for (int i = 0; i < Other.length; i++) {
                        textArray[6][i] = Other[i];
                    }
                }
            }
        }
//        if(Type.length==0){
//            Type=null;
//        }
//        if(Tool.length==0){
//            Tool=null;
//        }
//        if(Electronic.length==0){
//            Electronic=null;
//        }
//        if(Furniture.length==0){
//            Furniture=null;
//        }
//        if(Transport.length==0){
//            Transport=null;
//        }
//        if(Leisure.length==0){
//            Leisure=null;
//        }
//        if(Other.length==0){
//            Other=null;
//        }
            adapter = new jeremy.material.ExpandableListAdapter(getActivity(), list, textArray,Type, Tool, Electronic, Furniture,Transport,Leisure, Other);
            expandableListView.setAdapter(adapter);

        //test.setText(str+"\n"+category);

        //sublist = new ArrayList<Map<String, Object>>();

    }

    //adapter = new ExpandableListAdapter(this, list, textArray,sublist);

    }
