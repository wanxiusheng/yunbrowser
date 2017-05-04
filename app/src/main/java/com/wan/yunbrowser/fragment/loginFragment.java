package com.wan.yunbrowser.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mob.tools.utils.UIHandler;
import com.wan.yunbrowser.Bean.User;
import com.wan.yunbrowser.MainActivity;
import com.wan.yunbrowser.R;
import com.wan.yunbrowser.Utils.HttpUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class loginFragment extends Fragment {
    public Context mContext;
    public Button login;
    public TextView register;
    public EditText un;
    public EditText pd;
    public String username;
    public String password;
    public registerFragment rfragment;
    public loginOKFragment lokfragment;
    public String result;
	public Button qqlogin;
	public Button weibologin;
    public loginFragment() {
		}
	@SuppressLint("ValidFragment")
	public loginFragment(Context mContext) {
		this.mContext=mContext;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.login, container, false);
		login= (Button) rootView.findViewById(R.id.login);
		register=(TextView) rootView.findViewById(R.id.goto_register);
		un=(EditText) rootView.findViewById(R.id.username);
		pd=(EditText) rootView.findViewById(R.id.password);
		rfragment=new registerFragment(mContext);
		qqlogin= (Button) rootView.findViewById(R.id.qqlogin);
		weibologin= (Button) rootView.findViewById(R.id.weibologin);

		register.setOnClickListener(new Button.OnClickListener(){//创建监听    
            public void onClick(View v) {    
          
            	getActivity().getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, rfragment)
				.commit();
            }    
  
        });
		login.setOnClickListener(new Button.OnClickListener(){//创建监听    
            public void onClick(View v) {

				if (check()) {
					String url= HttpUtil.BASE_URL+"login"+"?username="+username+"&password="+password;
					try {
						result=HttpUtil.getRequest(url);
						if(result.equals("登陆成功")){
							//Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
							Snackbar.make(login,"登陆成功",Snackbar.LENGTH_SHORT).show();
							User.username=username;
							User.password=password;
							User.flag=0;
							((MainActivity)getActivity()).setNavUsrername("点击修改头像");
							((MainActivity)getActivity()).setDefaultIcon();
							((MainActivity)getActivity()).setIcon("http://www.wanxiusheng.cn/upload/upload/"+username+".png");
							lokfragment=new loginOKFragment(mContext,User.username);
							getActivity().getSupportFragmentManager()
									.beginTransaction()
									.replace(R.id.fragment_container, lokfragment)
									.commit();
						}else if(result.equals("用户名不存在")){
							//Toast.makeText(mContext, "用户名不存在", Toast.LENGTH_SHORT).show();
							Snackbar.make(login,"用户名不存在",Snackbar.LENGTH_SHORT).show();
						}else if(result.equals("密码错误")){
							//Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
							Snackbar.make(login,"密码错误",Snackbar.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
        });

		weibologin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareSDK.initSDK(mContext);
				final Platform weibo = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
                weibo.SSOSetting(false);
				weibo.setPlatformActionListener(new PlatformActionListener() {
					@Override
					public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = res;
                        UIHandler.sendMessage(msg, new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 0:
										//Toast.makeText(mContext,weibo.getDb().getUserId()+weibo.getDb().getUserIcon()+weibo.getDb().getUserName(),Toast.LENGTH_LONG).show();
                                        HashMap<String, Object> res= (HashMap<String, Object>) msg.obj;
                                       String weiboname= (String) res.get("screen_name");
                                        String url = HttpUtil.BASE_URL + "register?username="
                                                + weiboname + "&password=" + "123456";
										((MainActivity)getActivity()).setIcon(weibo.getDb().getUserIcon());
                                        try {
                                            result = HttpUtil.getRequest(url);
                                            if (result.equals("用户名已注册")) {
                                                //登录
                                                User.username=weiboname;
                                                User.password="123456";
												User.flag=1;
												((MainActivity)getActivity()).setNavUsrername(weiboname);
                                                lokfragment=new loginOKFragment(mContext,User.username);
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.fragment_container, lokfragment)
                                                        .commit();
                                            } else if (result.equals("注册成功")) {
                                                User.username=weiboname;
                                                User.password="123456";
												User.flag=1;
                                                lokfragment=new loginOKFragment(mContext,User.username);
                                                ((MainActivity)getActivity()).setNavUsrername(weiboname);
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.fragment_container, lokfragment)
                                                        .commit();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                }
                                return false;
                            }
                        });
                    }

					@Override
					public void onError(Platform platform, int i, Throwable throwable) {
                        Toast.makeText(mContext,"授权失败",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(Platform platform, int i) {
                        Toast.makeText(mContext,"取消",Toast.LENGTH_SHORT).show();
					}
				});
				weibo.showUser(null);//执行登录，登录后在回调里面获取用户资料
			}
		});




		qqlogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareSDK.initSDK(mContext);
				final Platform qq = ShareSDK.getPlatform(mContext, QQ.NAME);
				qq.SSOSetting(false);
				qq.setPlatformActionListener(new PlatformActionListener() {
					@Override
					public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = res;

						UIHandler.sendMessage(msg, new Handler.Callback() {
							@Override
							public boolean handleMessage(Message msg) {
								switch (msg.what) {
									case 0:

										//Toast.makeText(mContext,qq.getDb().getUserId()+qq.getDb().getUserIcon()+qq.getDb().getUserName(),Toast.LENGTH_LONG).show();
										HashMap<String, Object> res= (HashMap<String, Object>) msg.obj;
                                        String tx= (String) res.get("figureurl_qq_2");
										String qqname=qq.getDb().getUserName();
										String url = HttpUtil.BASE_URL + "register?username="
												+qqname+"&password="+"123456";
										((MainActivity)getActivity()).setIcon(tx);
										try {
											result = HttpUtil.getRequest(url);
											if (result.equals("用户名已注册")) {
												//登录
												User.username=qqname;
												User.password="123456";
												User.flag=2;
												((MainActivity)getActivity()).setNavUsrername(qqname);
												lokfragment=new loginOKFragment(mContext,qqname);
												getActivity().getSupportFragmentManager()
														.beginTransaction()
														.replace(R.id.fragment_container, lokfragment)
														.commit();
											} else if (result.equals("注册成功")) {
												User.username=qqname;
												User.password="123456";
												User.flag=2;
												((MainActivity)getActivity()).setNavUsrername(qqname);
												lokfragment=new loginOKFragment(mContext,qqname);
												getActivity().getSupportFragmentManager()
														.beginTransaction()
														.replace(R.id.fragment_container, lokfragment)
														.commit();
											}
										} catch (Exception e) {
                                            Toast.makeText(mContext,"失败",Toast.LENGTH_SHORT).show();
											e.printStackTrace();

										}
								}
								return false;
							}
						});
					}




					@Override
					public void onError(Platform platform, int i, Throwable throwable) {
						Toast.makeText(mContext,"授权失败",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(Platform platform, int i) {
						Toast.makeText(mContext,"取消",Toast.LENGTH_SHORT).show();
					}
				});
				qq.showUser(null);//执行登录，登录后在回调里面获取用户资料
			}
		});
		
		return rootView;
	}
public boolean check(){
	username=un.getText().toString().trim();
	password=pd.getText().toString().trim();
	if(un.getText().length()==0){
		//Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
		Snackbar.make(login,"用户名不能为空",Snackbar.LENGTH_SHORT).show();
		return false;
	}else if(pd.getText().length()==0){
		//Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
		Snackbar.make(login,"密码不能为空",Snackbar.LENGTH_SHORT).show();
		return false;
	}else{
		return true;
	}
	  
}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
