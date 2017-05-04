package com.wan.yunbrowser.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.wan.yunbrowser.Bean.User;
import com.wan.yunbrowser.MainActivity;
import com.wan.yunbrowser.R;

public class loginOKFragment extends Fragment {
    public Button offline;
    public TextView tv;
    public Context mContext;
    public String username;
    public loginFragment lfragment;
	public loginOKFragment() {
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("ValidFragment")
	public loginOKFragment(Context mContext, String username) {
		// TODO Auto-generated constructor stub
		this.mContext=mContext;
		this.username=username;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.loginok, container, false);
		lfragment=new loginFragment(mContext);
		offline = (Button) rootView.findViewById(R.id.button1);
		tv=(TextView) rootView.findViewById(R.id.textview1);
		tv.setText(username+"\n"+"已经登录");
		offline.setOnClickListener(new Button.OnClickListener(){//创建监听    
            public void onClick(View v) {   
            	User.username="";
                User.password="";
				User.flag=0;
				((MainActivity)getActivity()).setDefaultIcon();
				((MainActivity)getActivity()).setNavUsrername("");
               // Toast.makeText(mContext, "下线成功", Toast.LENGTH_SHORT).show();
				Snackbar.make(tv,"下线成功",Snackbar.LENGTH_SHORT).show();
            	 getActivity().getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, lfragment)
					.commit();
            	 
            }
        });
		return rootView;
	}
	

}
