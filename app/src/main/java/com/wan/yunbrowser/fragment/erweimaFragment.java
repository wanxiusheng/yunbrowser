package com.wan.yunbrowser.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wan.yunbrowser.Bean.User;
import com.wan.yunbrowser.MainActivity;
import com.wan.yunbrowser.R;

public class erweimaFragment extends Fragment{

	private Context mContext;
	public erweimaFragment() {
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("ValidFragment")
	public erweimaFragment(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	private Button scan;
	private TextView tv;
	private HomeFragment bfragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.erweima, container, false);
		scan=(Button) rootView.findViewById(R.id.scan);
		tv=(TextView) rootView.findViewById(R.id.tv1);
        scan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText("");
				Intent startScan = new Intent(mContext,CaptureActivity.class);
				startActivityForResult(startScan,0);
				
			}
		});
		return rootView;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {


		/**
		 * 处理二维码扫描结果
		 */
		if (requestCode == 0) {
			//处理扫描结果（在界面上显示）
			if (null != data) {
				Bundle bundle = data.getExtras();
				if (bundle == null) {
					return;
				}
				if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
					String result = bundle.getString(CodeUtils.RESULT_STRING);
					if(result.contains("http")){
					User.webAdd = result;
					bfragment = new HomeFragment(mContext);
					getActivity().getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container, bfragment)
						.commit();
						((MainActivity)getActivity()).setNavSelected(R.id.nav_home);
					}else {
						tv.setText(result);
					}

				} else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
					Toast.makeText(mContext, "解析二维码失败", Toast.LENGTH_LONG).show();
					Snackbar.make(scan,"收藏成功",Snackbar.LENGTH_SHORT).show();
				}
			}
		}
		}
	}


