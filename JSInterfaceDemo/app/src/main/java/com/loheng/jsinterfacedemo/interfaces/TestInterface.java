package com.loheng.jsinterfacedemo.interfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;


import com.gree.plugininterfacelib.BaseInterface;
import com.gree.processor.annotation.NativeInterface;

import java.util.HashMap;


/**
 * 格力内部接口
 */
public class TestInterface extends BaseInterface {

    private static final String TAG = "InnerInterface";
    private volatile static TestInterface sInstance;

    public TestInterface(Context context) {
        super(context);
    }


    @NativeInterface
    public void showToast(){
        Log.i(TAG,"调用了");
    }

    @NativeInterface
    public void showToast(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @NativeInterface
    public void showDialog1(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }

    @NativeInterface
    public void helloWorld(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @NativeInterface(jsName = "show_dialog")
    public void showDialog2(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }
    @NativeInterface(jsName = "show_dialog")
    public void showDialog3(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }
    @NativeInterface(jsName = "show_dialog")
    public void showDialog4(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }
    @NativeInterface(jsName = "show_dialog")
    public void showDialog5(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }    @NativeInterface(jsName = "show_dialog")
    public void showDialog6(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }    @NativeInterface(jsName = "show_dialog")
    public void showDialog7(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }
    @NativeInterface(jsName = "show_dialog")
    public void showDialog8(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }
    @NativeInterface(jsName = "show_dialog")
    public void showDialog9(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }    @NativeInterface(jsName = "show_dialog")
    public void showDialog10(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }
    @NativeInterface(jsName = "show_dialog")
    public void showDialog11(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }

    @NativeInterface(jsName = "show_dialog")
    public void showDialog123(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }

    @NativeInterface(jsName = "show_dialog")
    public void showDialog12(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }

    @NativeInterface(jsName = "show_dialog")
    public void Toolba4r1(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }

    @NativeInterface(jsName = "show_dialog")
    public void Toolba4r2(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolba4r3(Context context){
        new AlertDialog.Builder(mContext).setMessage("helloworld").create().show();

    }

    @NativeInterface(jsName = "show_dialog")
    public void Toolba4r(Context context){
        new Toolbar(context);

    }

    @NativeInterface(jsName = "show_dialog")
    public void Toolbar417(Context context){
        new Toolbar(context);

    }

    @NativeInterface(jsName = "show_dialog")
    public void Toolbar416(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar415(Context context){
        new Toolbar(context);

    }    @NativeInterface(jsName = "show_dialog")
    public void Toolbar414(Context context){
        new Toolbar(context);

    }    @NativeInterface(jsName = "show_dialog")
    public void Toolbar413(Context context){
        new Toolbar(context);

    }    @NativeInterface(jsName = "show_dialog")
    public void Toolbar412(Context context){
        new Toolbar(context);

    }

    @NativeInterface(jsName = "show_dialog")
    public void Toolbar411(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar42(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar41(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar423(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar442(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar2(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar33(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar44(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar(Context context){
        new Toolbar(context);

    }
    @NativeInterface(jsName = "show_dialog")
    public void Toolbar123(Context context){
        new Toolbar(context);

    }







}
