package com.loheng.jsinterfacedemo.interfaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;


import com.gree.plugininterfacelib.BaseInterface;
import com.gree.processor.annotation.NativeInterface;
import com.loheng.jsinterfacedemo.OnRequestListener;


import java.util.Calendar;


/**
 * 发给外部的接口
 */
public class ExternalInterface extends BaseInterface {


    private ProgressDialog mProgressBar;

    public ExternalInterface(Context context) {
        super(context);
    }

    @NativeInterface
    public void showLoading() {
        mProgressBar = new ProgressDialog(mContext);
        mProgressBar.show();
    }

    @NativeInterface
    public void hideLoading() {
        Toast.makeText(mContext, "hide loading", Toast.LENGTH_SHORT).show();
    }

    @NativeInterface
    public String getTime() {
        return Calendar.getInstance().getTime().toString();
    }

    @NativeInterface
    public String getTime(OnRequestListener onRequestListener) {
        onRequestListener.onOk("hhhh");
        return Calendar.getInstance().getTime().toString();
    }

    @NativeInterface(jsName = "show_toast")
    public void showToast(String string) {
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }

}
