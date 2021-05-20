package com.loheng.jsinterfacedemo.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


import com.gree.plugininterfacelib.MethodRouter;
import com.loheng.jsinterfacedemo.interfaces.ExternalInterface;
import com.loheng.jsinterfacedemo.interfaces.GreeInterface;
import com.loheng.jsinterfacedemo.OnRequestListener;
import com.loheng.jsinterfacedemo.interfaces.TestInterface;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 网页
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    private MethodRouter mRouter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        setContentView(mWebView);
        init();

        mWebView.loadUrl("file:///android_asset/index.html");
    }

    private void init() {
        //------重点配置------
        mRouter = new MethodRouter(this).
                loadInterface(new Class[]{ExternalInterface.class, GreeInterface.class, TestInterface.class});
        //-----重点配置------

        webSetting();

        mWebView.addJavascriptInterface(new JSInterface(), "AndroidFunction");
    }


    private void webSetting() {
        WebSettings settings = mWebView.getSettings();
        //设置setting属性
        settings.setJavaScriptEnabled(true);//支持js
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//设置缓存方式  不使用缓存只从网络获取数据
        settings.setDomStorageEnabled(true);//开启DOM storage API功能（HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
        settings.setDefaultTextEncodingName("utf-8");//设置默认编码
        settings.setUseWideViewPort(false);//将图片调整到适合webview的大小
        settings.setSupportZoom(true);//支持缩放
        settings.setDisplayZoomControls(false);//设定缩放控件隐藏
        settings.setAllowFileAccess(true);//设置可以访问文件
        settings.setBuiltInZoomControls(true);//设置支持缩放
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);//支持自动加载图片
        settings.setSavePassword(false);//关闭密码保存提醒 如果选择”是”，密码会被明文保到 /data/data/com.package.name/databases/webview.db 中，这样就有被盗取密码的危险
    }


    @SuppressLint("JavascriptInterface")
    public class JSInterface {

        @JavascriptInterface
        public void showToast(final String funcName) {
            mRouter.invoke("show_toast", funcName);
        }

        @JavascriptInterface
        public void showLoading() {
            mRouter.invoke("showLoading");
        }

        @JavascriptInterface
        public void hideLoading() {
            mRouter.invoke("hideLoading");
        }

        @JavascriptInterface
        public String getTime() {
            return (String) mRouter.invoke("getTime");
        }
    }

}
