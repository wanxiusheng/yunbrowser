package com.wan.yunbrowser.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.wan.yunbrowser.Bean.User;
import com.wan.yunbrowser.MainActivity;
import com.wan.yunbrowser.R;
import com.wan.yunbrowser.Thread.Bookmark;
import com.wan.yunbrowser.Thread.History;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by wan on 2017/4/23.
 */

public class HomeFragment extends Fragment {
    public Context mContext;
    private EditText webURL;
    private ImageButton go;
    private WebView webView;
    private ImageButton goFor;
    private ImageButton goBack;
    private ImageButton mainPage;
    private ImageButton refresh;
    private ProgressBar pb;
    public String webTitle="";
    public ImageButton save;
    public ImageButton showmenu;
    FloatingActionButton share;
    public HomeFragment() {

    }
    @SuppressLint("ValidFragment")
    public HomeFragment(Context mContext) {
        this.mContext=mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.browser, container, false);
        webURL=(EditText) rootView.findViewById(R.id.webURL);
        pb=(ProgressBar)rootView.findViewById(R.id.progressBar);
        showmenu= (ImageButton) rootView.findViewById(R.id.showmenu);
        go=(ImageButton) rootView.findViewById(R.id.go);//访问
        goFor=(ImageButton) rootView.findViewById(R.id.goFor);//前进
        goBack=(ImageButton) rootView.findViewById(R.id.goBack);//后退
        mainPage=(ImageButton) rootView.findViewById(R.id.mainPage);//主页
        refresh=(ImageButton) rootView.findViewById(R.id.refresh);//刷新
        webView=(WebView) rootView.findViewById(R.id.webView1);
        save=(ImageButton) rootView.findViewById(R.id.star);//收藏
        share= (FloatingActionButton) rootView.findViewById(R.id.btn_share);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        webView.loadUrl(User.webAdd);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                webURL.setText(webView.getUrl());
                User.webAdd = webView.getUrl();
                return true;
            }


        });

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webTitle=title;
                User.webAdd = webView.getUrl();
                if(User.getUsername().length()!=0){
                    if((webTitle=="")||(webTitle.equals("321网址导航"))){
                    }else{
                        History myThread = new History(User.username,webTitle,User.webAdd);
                        Thread thread = new Thread(myThread);
                        thread.start();
                    }
                }


            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(newProgress);
                if(newProgress==100){
                    pb.setVisibility(View.GONE);


                }
                super.onProgressChanged(view, newProgress);
            }

        };
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);
        go.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                User.webAdd = webURL.getText().toString().trim();
                if(User.webAdd==null||User.webAdd.length()==0){
                    //Toast.makeText(mContext, "不能输入为空",Toast.LENGTH_SHORT).show();
                    Snackbar.make(share,"不能输入为空",Snackbar.LENGTH_SHORT).show();
                }else if(User.webAdd.startsWith("http://")||User.webAdd.startsWith("https://")){
                    webView.loadUrl(User.webAdd);
                    webURL.setText(webView.getUrl());
                }else{
                    webView.loadUrl("http://"+User.webAdd);
                    webURL.setText(webView.getUrl());
                }
            }

        });
        goBack.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    webURL.setText(webView.getUrl());
                    User.webAdd = webView.getUrl();
                }
            }

        });

        goFor.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                    webURL.setText(webView.getUrl());
                    User.webAdd = webView.getUrl();
                }
            }

        });
        showmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openMenu();
            }
        });

        mainPage.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                webView.loadUrl("file:///android_asset/index/index.html");

                webURL.setText("");
                User.webAdd =webView.getUrl();
            }

        });

        refresh.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                webView.loadUrl(webView.getUrl());
                webURL.setText(webView.getUrl());
                User.webAdd = webView.getUrl();
            }

        });
        save.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                if(User.getUsername().length()==0){
                    //Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
                    Snackbar.make(share,"请先登录",Snackbar.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setMessage("确定收藏"+webTitle+"吗?").setCancelable(true);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            Bookmark bk = new Bookmark(User.username,webTitle,User.webAdd);
                            Thread thread = new Thread(bk);
                            thread.start();
//                            Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                            Snackbar.make(share,"收藏成功",Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();
                }

            }

        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        return rootView;
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

    private void showShare() {
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(webTitle);
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(User.webAdd);
// text是分享文本，所有平台都需要这个字段
        oks.setText("标题:"+webTitle+"\n"+"URL:"+User.webAdd);
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(User.webAdd);
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("快来看看吧");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(User.webAdd);

// 启动分享GUI
        oks.show(mContext);
    }



}
