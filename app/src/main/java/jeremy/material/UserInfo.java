package jeremy.material;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Jeremy_Liu on 2016/5/2.
 */
public class UserInfo extends Fragment {

    private UserDbAdapter mDbHelper;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabcontent_1,container,false);
        EditText name = (EditText)view.findViewById(R.id.editText1);
        EditText gender = (EditText)view.findViewById(R.id.editText2);
        EditText phone = (EditText)view.findViewById(R.id.editText3);
        EditText email = (EditText)view.findViewById(R.id.editText4);
        EditText address = (EditText)view.findViewById(R.id.editText5);

        String Name = name.getText().toString();
        String Gender = gender.getText().toString();
        String Phone = phone.getText().toString();
        String Email = email.getText().toString();
        String Address = address.getText().toString();

        //mDbHelper.insertInfo(Name,Gender,Phone,Email,Address);
        return view;
    }
}
