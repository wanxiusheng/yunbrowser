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
import android.widget.EditText;
import android.widget.Toast;
import com.wan.yunbrowser.R;
import com.wan.yunbrowser.Utils.HttpUtil;


public class registerFragment extends Fragment {
	public Button register;
	public EditText un;
	public EditText pd;
	public Context mContext;
	public String username;
	public String password;
    public String result;
	public loginFragment lfagment;

	public registerFragment() {
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("ValidFragment")
	public registerFragment(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.register, container, false);
		un = (EditText) rootView.findViewById(R.id.register_username);
		pd = (EditText) rootView.findViewById(R.id.register_password);
		register = (Button) rootView.findViewById(R.id.register);
		lfagment = new loginFragment(mContext);
		register.setOnClickListener(new Button.OnClickListener() {// 创建监听
			public void onClick(View v) {
				if (check()) {
                    String url = HttpUtil.BASE_URL + "register?username="
                            + username + "&password=" + password;

                    try {
                        result = HttpUtil.getRequest(url);
                        if (result.equals("用户名已注册")) {
                            //Toast.makeText(mContext, "用户名已注册",Toast.LENGTH_SHORT).show();
							Snackbar.make(register,"用户名已被注册",Snackbar.LENGTH_SHORT).show();
                        } else if (result.equals("注册成功")) {
                          //  Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
							Snackbar.make(register,"注册成功",Snackbar.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, lfagment)
                                    .commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


				}
			}

		});
		return rootView;
	}

	public boolean check() {

		username = un.getText().toString().trim();
		password = pd.getText().toString().trim();
		if (un.getText().length()<6) {
			//Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
			Snackbar.make(register,"用户名不能为小于6位",Snackbar.LENGTH_SHORT).show();
			return false;
		} else if (pd.getText().length()<6) {
			//Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
			Snackbar.make(register,"密码不能小于6位",Snackbar.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

}
