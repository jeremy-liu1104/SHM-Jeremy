package jeremy.material;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy_Liu on 2016/5/26.
 */
public class UserAdapter extends BaseAdapter {
    private List<Map<String,Object>> user;
    private Context mContext;

    public UserAdapter(Context mContext,List<Map<String,Object>> user){
        this.mContext = mContext;
        this.user = user;
    }
    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.spinner_abstract,null);
        if(convertView!=null) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.id_image);
            TextView textView = (TextView)convertView.findViewById(R.id.id_name);
            TextView textId = (TextView)convertView.findViewById(R.id.id);
            imageView.setBackground(Drawable.createFromPath(user.get(position).get("pic").toString()));
            textView.setText(user.get(position).get("username").toString());
            textId.setText(user.get(position).get("id").toString());
        }
        return convertView;
    }
}
