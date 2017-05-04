package com.wan.yunbrowser;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;



/**
 * Created by Administrator on 2017/4/9.
 */


    public class myapplication extends Application {
        @Override
        public void onCreate()
        {
            super.onCreate();
            ZXingLibrary.initDisplayOpinion(this);

        }
    }


