package jeremy.material;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy_Liu on 2016/5/15.
 */
public class tabcontent_3 extends Fragment {
    ItemDbAdapter mDb;
    // 存储数据的数组列表
    ArrayList<HashMap<String, Object>> listData;
    // 适配器
    SimpleAdapter listItemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout3;
    private String ItemID;
    private TextView textView;
    private TextView from;
    private List<Map<String, Object>> list;
    final String[] types = {"Clothes","Tool", "Electronic", "Furniture","Vehicle","Leisure","Other"};
    int[] EXPANDABLE_LISTVIEW_IMG = new int[]{
            R.drawable.ic_category_55, R.drawable.ic_category_50,
            R.drawable.ic_category_45, R.drawable.ic_category_60,
            R.drawable.ic_category_65, R.drawable.ic_category_30,
            R.drawable.ic_category_70};
    public static String str = null; // 将StringBuffer转化成字符串
    public static StringBuffer sb = new StringBuffer(); // StringBuffer便于字符串的增删改查操作
    private ExpandableListView expandableListView;
    private android.widget.ExpandableListAdapter adapter;
    private String[][] textArray = new String[types.length][];
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tabcontent_3, container, false);

        textView = (TextView)view.findViewById(R.id.text_id);
        from = (TextView)view.findViewById(R.id.text_contact);
        final String username = ((MainActivity)getActivity()).getUsername();

        //this.deleteDatabase("users.db");//先删除原表，不然会一直添加数据
        mDb = new ItemDbAdapter(getActivity());
        mDb.open();
        textArray = new String[types.length][mDb.getCountAll()];
        //final ListView list = (ListView) view.findViewById(R.id.list_items2);
        expandableListView = (ExpandableListView) view.findViewById(R.id.ExpandableListView);

        //this.getActivity().registerForContextMenu(view.findViewById(R.id.list_items2));//用于长按跳出菜单

        listData = new ArrayList<HashMap<String, Object>>();
        // 获取表的内容
        //getAll();
        initData();


//        listItemAdapter = new SimpleAdapter(getActivity(),
//                listData,// 数据源
//                R.layout.item,// ListItem的XML实现
//                // 动态数组与ImageItem对应的子项
//                new String[]{"id","image", "itemname", "date", "from","price","type"},
//                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
//                new int[]{R.id.text_id,R.id.image, R.id.itemname, R.id.date, R.id.text_contact,R.id.text_price,R.id.type});
//        list.setAdapter(listItemAdapter);
//        setListViewHeight(list);


        setListViewHeight(expandableListView);
         //给一级菜单的选项设置监听
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        // 给一级菜单下面的二级菜单的选项设置监听接口
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
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
                    textView = (TextView) view.findViewById(R.id.text_id);
                    //HashMap<String, Object> map = new HashMap<String, Object>();
                    //String itemid = (map.get("id").toString()).substring(3);
                    String itemid = (textView.getText().toString().substring(3));
                    String[] data = textArray[groupPosition][childPosition].split("\\@");
                    Intent intent = new Intent(getActivity(), Hall_detail.class);
                    from = (TextView)view.findViewById(R.id.text_contact);
                    intent.putExtra("clickid", data[0]);//0是id
                    intent.putExtra("username",username);
                    intent.putExtra("from",data[4]);//4是from
                    startActivity(intent);
            //}
                return false;
            }
        });

        swipeRefreshLayout3 = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout3);
        //���þ��ڵ���ɫ
        swipeRefreshLayout3.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout3.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //new Handler().postDelayed(new Runnable() {
                            //public void run() {
                                //listData.clear();
                                //getAll();
                                //setListViewHeight(list);
                                //listItemAdapter.notifyDataSetChanged();
                                initData();
                                setListViewHeight(expandableListView);
                                swipeRefreshLayout3.setRefreshing(false);
                            //}
                        //}, 1500);
                    }
                }
        );

//            list.setOnItemClickListener(
//                    new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            textView = (TextView)view.findViewById(R.id.text_id);
//                            from = (TextView)view.findViewById(R.id.text_contact);
//                            String itemid = (textView.getText().toString()).substring(3);
//                            Intent intent = new Intent(getActivity(),Hall_detail.class);
//                            intent.putExtra("clickid",itemid);
//                            intent.putExtra("username",username);
//                            intent.putExtra("from",from.getText().toString());
//                            startActivity(intent);
//                        }
//                    }
//            );

        //长按事件
        // 长按事件响应
//        View.OnCreateContextMenuListener listviewLongPress = new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v,
//                                            ContextMenu.ContextMenuInfo menuInfo) {
//                // TODO Auto-generated method stub
//                menu.add(0, 1, Menu.NONE, "Edit");
//                menu.add(0, 2, Menu.NONE, "Delete");

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
//                                        // 获取位置索引
//                                        int mListPos = info.position;
//                                        // 获取对应HashMap数据内容
//                                        HashMap<String, Object> map = listData
//                                                .get(mListPos);
//                                        // 获取id
//                                        int id = Integer.valueOf((map.get("id")
//                                                .toString()));
//                                        // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
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
//            }
//        };
//        list.setOnCreateContextMenuListener(listviewLongPress);


        final Spinner spinner = (Spinner)view.findViewById(R.id.spinner4);
        ImageView search = (ImageView)view.findViewById(R.id.search_image);

        //String[] user_abstract = null;
        ArrayAdapter<String> adapter = null;



        //String data = sb.toString();
        //spinner_abstract = data.split("\\!");
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinner_abstract);
        //设置下拉列表风格
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去


        search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText search_edit = (EditText)view.findViewById(R.id.search_edit);
                        Cursor cursor = mDb.getUnPicIn(search_edit.getText().toString());
                        //int count = cursor.getInt(0);
                        ArrayList<Map<String,Object>> user = new ArrayList<Map<String, Object>>();
                        if(!cursor.isAfterLast()) {
                            while (cursor.moveToNext()) {
                                //list = new String[i+1];
                                Map<String, Object> map = new HashMap<String, Object>();
                                //sb.append(cursor.getString(cursor.getColumnIndex("username")));
                                //sb.append("!");
                                map.put("id", cursor.getString(cursor.getColumnIndex("_id")));
                                map.put("username", "From:"+cursor.getString(cursor.getColumnIndex("username")));
                                map.put("pic", cursor.getString(cursor.getColumnIndex("image")));
                                user.add(map);
                            }
                        }
                        else {
                            Toast.makeText(v.getContext(), "Not found", Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();
                        UserAdapter Uadapter = new UserAdapter(getActivity(),user);
                        spinner.setAdapter(Uadapter);
                        //spinner.setVisibility(View.VISIBLE);//设置默认显示
                        //spinner.performClick();
                    }
                }
        );

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(),Hall_detail.class);
                        TextView textid = (TextView)view.findViewById(R.id.id);
                        TextView textfrom = (TextView)view.findViewById(R.id.id_name);
                        intent.putExtra("clickid",textid.getText().toString());
                        intent.putExtra("from",textfrom.getText().toString().substring(5));
                        intent.putExtra("username",username);
                        startActivity(intent);
                        Toast.makeText(view.getContext(), "Found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        return view;
    }
    public void getAll(){
        Cursor cursor = mDb.getAll();
        while (cursor.moveToNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", "IN:"+cursor.getString(cursor.getColumnIndex("_id")));
            map.put("itemname", "Item:"+cursor.getString(cursor.getColumnIndex("itemname")));
            map.put("date", "Date:"+cursor.getString(cursor.getColumnIndex("date")));
            map.put("image", cursor.getString(cursor.getColumnIndex("image")));
            map.put("from","From:"+cursor.getString(cursor.getColumnIndex("username")));
            map.put("price","Price:"+cursor.getString(cursor.getColumnIndex("price")));
            map.put("type",cursor.getString(cursor.getColumnIndex("type")));
            listData.add(map);
            //((MainActivity)getActivity()).setItemcount(map.);
        }
        cursor.close();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();//获得AdapterContextMenuInfo,以此来获得选择的listview项
        //ItemID= (String) listData.get(menuInfo.position).get("ID");

        int getPosition=menuInfo.position;

        HashMap<String, Object> map = new HashMap<String, Object>();

        String from = map.get("from").toString();
        switch(item.getItemId()) {                    //点击不同项 所触发的不同事件
            case 1:

                if(((MainActivity)getActivity()).getUsername()==from) {
                    //String itemid = (map.get("id").toString());
                    //Intent intent = new Intent(getActivity(), Item_detail.class);
                    //intent.putExtra("clickid", itemid);
                    //startActivity(intent);
                    Toast.makeText(getView().getContext(), "你有权改", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getView().getContext(), "对不起，你没有权限编辑此信息", Toast.LENGTH_SHORT).show();
//        Bundle sentId=new Bundle();
//     sentId.putString("ID", ItemID);
//     intent.putExtras(sentId);

                break;
            case 2:
                if(((MainActivity)getActivity()).getUsername()==from) {
                    final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
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
                                            int mListPos = info.position;
                                            // 获取对应HashMap数据内容
                                            HashMap<String, Object> map = listData
                                                    .get(mListPos);
                                            // 获取id
                                            int id = Integer.valueOf((map.get("id")
                                                    .toString()));
                                            // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                                            if (mDb.delete(id)) {
                                                // 移除listData的数据
                                                listData.remove(mListPos);
                                                listItemAdapter.notifyDataSetChanged();
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
                }
                else
                    Toast.makeText(getView().getContext(), "对不起，你没有权限编辑此信息", Toast.LENGTH_SHORT).show();
                break;}

        return true;
    }


    public void setListViewHeight(ListView listView) {
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

        listView.setLayoutParams(params);
    }
    // 初始化数据的填充
    private void initData() {
        mDb = new ItemDbAdapter(getActivity());
        mDb.open();
        textArray = new String[types.length][mDb.getCountAll()];
        // TODO Auto-generated method stub
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", EXPANDABLE_LISTVIEW_IMG[i]);
            map.put("txt", types[i]);
            list.add(map);
        }
        // 获取表的内容

        for (int i = 0; i < types.length; i++) {
            Cursor cursor = mDb.getAllNotesByType(types[i]);
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
}
