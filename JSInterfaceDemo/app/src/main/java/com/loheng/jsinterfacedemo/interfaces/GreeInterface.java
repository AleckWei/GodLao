package com.loheng.jsinterfacedemo.interfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gree.plugininterfacelib.BaseInterface;
import com.gree.processor.annotation.NativeInterface;


/**
 * 格力内部接口
 */
public class GreeInterface extends BaseInterface {

    private static final String TAG = "InnerInterface";
    private volatile static GreeInterface sInstance;

    public GreeInterface(Context context) {
        super(context);
    }


    @NativeInterface
    public void showToast(){
        Log.i(TAG,"调用了");
    }

    @NativeInterface(jsName = "show_toast")
    public void showToast(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @NativeInterface(jsName = "show_dialog")
    public void showDialog(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }



}
