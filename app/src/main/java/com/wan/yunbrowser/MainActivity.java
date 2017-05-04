package com.wan.yunbrowser;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wan.yunbrowser.Bean.User;
import com.wan.yunbrowser.Utils.HttpUtil;
import com.wan.yunbrowser.fragment.HomeFragment;
import com.wan.yunbrowser.fragment.bookmarkFragment;
import com.wan.yunbrowser.fragment.erweimaFragment;
import com.wan.yunbrowser.fragment.historyFragment;
import com.wan.yunbrowser.fragment.loginFragment;
import com.wan.yunbrowser.fragment.loginOKFragment;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;


public class MainActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;
    NavigationView navView;
    HomeFragment bfragment;
    loginFragment lfragment;
    loginOKFragment lokfragment;
    public bookmarkFragment bookmarkfragment;
    historyFragment historyfragment;
    erweimaFragment efragment;
    CircleImageView touxiang;
    public Bitmap mBitmap;
    View headerView;
    ImageView nav_bg;
    TextView nav_username;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.home);
        headerView = navView.getHeaderView(0);
        nav_username= (TextView)headerView.findViewById(R.id.nav_username);
        touxiang= (CircleImageView) headerView.findViewById(R.id.icon_image);
        nav_bg= (ImageView) headerView.findViewById(R.id.nav_bg);
        bfragment = new HomeFragment(this);
        lfragment = new loginFragment(this);
        bookmarkfragment=new bookmarkFragment(this);
        historyfragment=new historyFragment(this);
        efragment=new erweimaFragment(this);
        loadBingPic();//加载每日一图
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getUsername().length()==0){
                    Snackbar.make(navView,"请先登录",Snackbar.LENGTH_SHORT).show();
                }else{
                    if(User.flag==0){
                        showChoosePicDialog();
                    }else{
                        Snackbar.make(navView,"第三方登录用户不准更改头像",Snackbar.LENGTH_SHORT).show();
                    }

                }

            }
        });
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch(item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,bfragment).commit();
                        break;
                    case R.id.nav_login:
                        if(User.username.length()==0){
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,lfragment).commit();
                        }
                        else{
                            lokfragment=new loginOKFragment(MainActivity.this,User.username);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,lokfragment).commit();
                        }
                        break;
                    case R.id.nav_bookmark:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,bookmarkfragment).commit();
                        break;
                    case R.id.nav_history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,historyfragment).commit();
                        break;
                    case R.id.nav_erweima:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,efragment).commit();
                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        Fragment bfragment = new HomeFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,bfragment).commit();
    }


    private long firstTime = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                   // Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    Snackbar.make(navView,"再按一次退出程序",Snackbar.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    public void openMenu(){
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
    public void setNavSelected(int id){
        navView.setCheckedItem(id);
    }
    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }
    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");

            try {
                upload(mBitmap);

                Snackbar.make(navView,"上传成功",Snackbar.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**上传图片到服务器*/
    private void upload(final Bitmap bitmap)
    {

           new AsyncTask<Bitmap, Void, String>()
           {

               @Override
               protected void onPreExecute() {}

               @Override
               protected String doInBackground(Bitmap... params)
               {
                   try
                   {
                       //此处没有判断是否有sd卡
                       File dirFile = new File("/mnt/sdcard/android/cache");
                       if(!dirFile.exists())
                       {
                           dirFile.mkdirs();
                       }
                       File file = new File(dirFile, "touxiang.png");
                       if (params[0].compress(Bitmap.CompressFormat.PNG, 50,
                               new FileOutputStream(file)))
                       {
                           System.out.println("保存图片成功");
                           HttpClient client = new DefaultHttpClient();

                           HttpPost httpPost = new HttpPost("http://www.wanxiusheng.cn/upload/test");
                           MultipartEntity entity = new MultipartEntity();
                           // 通过RSA加密后的用户名
                           String miUserName = User.username;
                           miUserName = new String(miUserName.getBytes("gbk"),"utf-8");
                           entity.addPart("acc", new StringBody(miUserName));
                           entity.addPart("img", new FileBody(file));
                           httpPost.setEntity(entity);
                           HttpResponse response = client.execute(httpPost);
                       }
                   }
                   catch (Exception e)
                   {
                       Snackbar.make(navView,"上传失败",Snackbar.LENGTH_SHORT).show();
                       e.printStackTrace();

                   }

                   return null;
               }
               @Override
               protected void onPostExecute(String result)
               {
                  new myThread().run();
                   //setIcon("http://www.wanxiusheng.cn/upload/upload/"+User.username+".png");
               }
           }.execute(bitmap);


    }
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setIcon("http://www.wanxiusheng.cn/upload/upload/"+User.username+".png");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    class myThread implements Runnable {
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
                Message message = new Message();
                message.what = 0;
                myHandler.sendMessage(message);

            }
        }


    public void setIcon(String url){
        Random random = new Random();
        //Glide.with(this).load(url+"?_="+random.nextInt(1000)).into(touxiang);
        Glide.with(this)
                .load(url+"?_="+random.nextInt(1000))
                .error(R.drawable.doge)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(touxiang);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Glide.with(this)
                .load(url+"?_="+random.nextInt(1000))
                .error(R.drawable.doge)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(touxiang);

    }
    public void setDefaultIcon(){
        touxiang.setImageResource(R.drawable.doge);
    }
    public void setNavUsrername(String name){
        nav_username.setText(name);
    }
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        try {
            String result=HttpUtil.getRequest(requestBingPic);
            Glide.with(this).load(result).into(nav_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
