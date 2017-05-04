package com.wan.yunbrowser.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.wan.yunbrowser.Adapter.BookmarkAdapter;
import com.wan.yunbrowser.Bean.Bookmark;
import com.wan.yunbrowser.Bean.User;
import com.wan.yunbrowser.MainActivity;
import com.wan.yunbrowser.R;
import com.wan.yunbrowser.Utils.HttpUtil;
import com.wan.yunbrowser.Utils.stringToJson;

public class bookmarkFragment extends Fragment {
	public Context mContext;
	public String result;
	public List<Bookmark> list;
	public String[] title;
	public String[] url;
	public Fragment bfragment;
	RecyclerView recyclerView;
	BookmarkAdapter adapter;
    public bookmarkFragment(){

	}
	@SuppressLint("ValidFragment")
	public bookmarkFragment(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.bookmark_simple, container, false);
	    if(User.username==""){
	    	//Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
			Snackbar.make(container,"请先登录",Snackbar.LENGTH_SHORT).show();
	    return rootView;
	    }else{
			recyclerView= (RecyclerView) rootView.findViewById(R.id.mylist);
			LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
			recyclerView.setLayoutManager(layoutManager);
			try {
				list=stringToJson.getBookmarkList("bookmarks", getData());
			} catch (JSONException e) {
				//Toast.makeText(mContext,"获取网络书签失败，请重试",Toast.LENGTH_SHORT).show();
				Snackbar.make(container,"获取网络书签失败，请重试",Snackbar.LENGTH_SHORT).show();
			}
			adapter=new BookmarkAdapter(list, new BookmarkAdapter.MyclickListener(){
				@Override
				public void urlclick(String url) {
					User.webAdd = url;
					bfragment = new HomeFragment(mContext);
					getActivity().getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container, bfragment)
							.commit();
					((MainActivity)getActivity()).setNavSelected(R.id.nav_home);
				}

				@Override
				public void titleclick(String url) {
					User.webAdd = url;
					bfragment = new HomeFragment(mContext);
					getActivity().getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container, bfragment)
							.commit();
					((MainActivity)getActivity()).setNavSelected(R.id.nav_home);
				}


				@Override
				public void imageclick(final String title){
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
							.setMessage("确定删除"+title+"吗?").setCancelable(true);
					builder.setPositiveButton("确定", new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Map<String,String> map= new HashMap<String,String>();
							map.put("username",User.username);
							map.put("title", title);
							String url1=HttpUtil.BASE_URL+"deleteBookmark";
							try {
								HttpUtil.postRequest(url1,map);
							} catch (Exception e) {
								e.printStackTrace();
							}
							//Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
							Snackbar.make(recyclerView,"删除成功",Snackbar.LENGTH_SHORT).show();
							refeshView();
						}
					});
					builder.create().show();

				}
			});
			recyclerView.setAdapter(adapter);
			
			return rootView;
		}
	    }
		


	public String getData() {
		String url = HttpUtil.BASE_URL + "getBookmark" + "?username="
				+ User.username;
		try {
			result = HttpUtil.getRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result;
		}

	}
	
	void refeshView(){
        try {
            list=stringToJson.getBookmarkList("bookmarks", getData());
        } catch (JSONException e) {
            //Toast.makeText(mContext,"获取网络书签失败，请重试",Toast.LENGTH_SHORT).show();
			Snackbar.make(recyclerView,"获取网络书签失败，请重试",Snackbar.LENGTH_SHORT).show();
        }
        adapter=new BookmarkAdapter(list, new BookmarkAdapter.MyclickListener(){
            @Override
            public void urlclick(String url) {
                User.webAdd = url;
                bfragment = new HomeFragment(mContext);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, bfragment)
                        .commit();
                ((MainActivity)getActivity()).setNavSelected(R.id.nav_home);
            }

            @Override
            public void titleclick(String url) {
                User.webAdd = url;
                bfragment = new HomeFragment(mContext);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, bfragment)
                        .commit();
                ((MainActivity)getActivity()).setNavSelected(R.id.nav_home);
            }


            @Override
            public void imageclick(final String title){
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setMessage("确定删除"+title+"吗?").setCancelable(true);
                builder.setPositiveButton("确定", new OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Map<String,String> map= new HashMap<String,String>();
                        map.put("username",User.username);
                        map.put("title", title);
                        String url1=HttpUtil.BASE_URL+"deleteBookmark";
                        try {
                            HttpUtil.postRequest(url1,map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       // Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
						Snackbar.make(recyclerView,"删除成功",Snackbar.LENGTH_SHORT).show();
                        refeshView();
                    }
                });
                builder.create().show();

            }
        });
        recyclerView.setAdapter(adapter);

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

