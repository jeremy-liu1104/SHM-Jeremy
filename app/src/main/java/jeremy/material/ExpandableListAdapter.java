package jeremy.material;

/**
 * Created by Jeremy_Liu on 2016/5/20.
 */
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import jeremy.material.ItemDbAdapter;
import jeremy.material.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private String[][] text_array;
    private Context context;
    private List<Map<String, Object>> list;
    private String[] Tool;
    private String[] Electronic;
    private String[] Furniture;
    private String[] Other;
    private String[] Clothes;
    private String[] Transport;
    private String[] Leisure;


    public ExpandableListAdapter(Context context,
                                 List<Map<String, Object>> list, String[][] array,String[] Clothes,String[] Tool, String[] Electronic, String[] Furniture,String[] Transport,String[] Leisure, String[] Other) {
        this.context = context;
        // 子菜单的选项的数据
        this.text_array = array;
        // 父菜单的选项的数据
        this.list = list;

        this.Tool = Tool;
        this.Electronic = Electronic;
        this.Furniture = Furniture;
        this.Other = Other;
        this.Clothes = Clothes;
        this.Transport = Transport;
        this.Leisure = Leisure;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     *
     * 获取第一级菜单的选的总数目
     */
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     *
     * 获取一级菜单下面二级菜单的选项的总数目
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        //return text_array[groupPosition].length;
        if(groupPosition==0) {
            if(Clothes==null){
                return 0;
            }
            else
            return Clothes.length;
        }
        else if(groupPosition==1){
            if(Tool==null){
                return 0;
            }
            else
                return Tool.length;
        }
        else if(groupPosition==2) {
            if(Electronic==null){
                return 0;
            }
            else
            return Electronic.length;
        }
        else if(groupPosition==3) {
            if(Furniture==null){
                return 0;
            }
            else
            return Furniture.length;
        }
        else if(groupPosition==4){
            if(Transport==null){
                return 0;
            }
            else
            return Transport.length;
        }
        else if(groupPosition==5){
            if(Leisure==null){
                return 0;
            }
            else
                return Leisure.length;
        }
        else {
            if(Other==null){
                return 0;
            }
            else
                return Other.length;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     *
     * 获取一级菜单的具体的选项的内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return list.get(groupPosition).get("txt");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     *
     * 获取一级菜单下第二级菜单的具体的选项的内容
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return text_array[groupPosition][childPosition];
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     *
     * 获取第一级菜单的选项的id
     */
    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     *
     * 获取一级菜单下二级菜单选项的id
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     *
     * 指定位置相应的组视图(指定视图相应的id)
     */
    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 *
	 * 对一级菜单的标签的内容进行设置
	 */

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        convertView = (LinearLayout) convertView.inflate(context,
                R.layout.item_expandablelist_layout, null);
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.img_icon);
        TextView textView = (TextView) convertView
                .findViewById(R.id.expandablelist_item_txt);
        // 是否可以点击扩展开来,设置字体显示的位置
        if (isExpanded) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.group_down, 0);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.group_up, 0);
        }

        // 设置图片和字体的内容
        imageView.setImageResource(Integer.parseInt(list.get(groupPosition)
                .get("img").toString()));
        textView.setText(list.get(groupPosition).get("txt")
                .toString());

        return convertView;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
     * android.view.View, android.view.ViewGroup)
     *
     *
     * 设置一级菜单下二级菜单的内容
     */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = (LinearLayout) convertView.inflate(context,
                R.layout.item, null);

        TextView id = (TextView) convertView.findViewById(R.id.text_id);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView from = (TextView) convertView.findViewById(R.id.text_contact);
        TextView price = (TextView) convertView.findViewById(R.id.text_price);
        TextView itemname = (TextView) convertView.findViewById(R.id.itemname);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        if(!text_array[groupPosition][childPosition].matches("\\*")&&text_array[groupPosition][childPosition]!=null) {
            String[] split = text_array[groupPosition][childPosition].split("\\@");
            if(split.length==7) {
                id.setText("IN:"+split[0]);
                itemname.setText("Item:"+split[1]);
                date.setText("Date:"+split[2]);
                image.setBackground(Drawable.createFromPath(split[3]));
                from.setText("From:"+split[4]);
                price.setText("Price:"+split[5]);
                type.setText("Type:"+split[6]);
            }else
                isLastChild=true;
        }
        else
            isLastChild=true;
        return convertView;
    }

    /**
     * 当选择子节点的时候，调用该方法
     *
     * */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
}
