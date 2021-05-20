package com.gree.plugininterfacelib;

import android.content.Context;

/**
 * @author lzh
 * @mail 560182@gree.com.cn
 * @time 5/15/21 8:51 AM
 * @detail 基础接口类 必须继承 保证一些必要参数的加载
 */
public class BaseInterface {

    /**
     * 对应activity的上下文
     */
    protected Context mContext;

    /**
     * 带参构造方法 必须写，否则反射创建会找不到，因此必须继承这个方法
     * @param context activity 上下文
     */
    public BaseInterface(Context context) {
        mContext = context;
    }
}
