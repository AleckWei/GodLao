package com.gree.plugininterfacelib;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lzh
 * @detail 这个类是由JsInterfaceProcessor自动生成，点击gradle任务的
 *         compileDebugJavaWithJavac或者build 自动生成 
 */
public class InterfaceLib {
    private static Map<String,String> sExternalInterfaceMap = new HashMap<>();

    private static Map<String,String> sTestInterfaceMap = new HashMap<>();

    private static Map<String,String> sGreeInterfaceMap = new HashMap<>();

    public static Map<String,String> getMap(String name){
        switch (name){
            case "com.loheng.jsinterfacedemo.interfaces.ExternalInterface":
                return getExternalInterfaceMap();
            case "com.loheng.jsinterfacedemo.interfaces.TestInterface":
                return getTestInterfaceMap();
            case "com.loheng.jsinterfacedemo.interfaces.GreeInterface":
                return getGreeInterfaceMap();
            default:
                return null;
        }
    }

    private static Map<String,String> getExternalInterfaceMap() {

        if(sExternalInterfaceMap.size() == 0){
            sExternalInterfaceMap.put("show_toast","com.loheng.jsinterfacedemo.interfaces.ExternalInterface$showToast");
            sExternalInterfaceMap.put("getTime","com.loheng.jsinterfacedemo.interfaces.ExternalInterface$getTime");
            sExternalInterfaceMap.put("hideLoading","com.loheng.jsinterfacedemo.interfaces.ExternalInterface$hideLoading");
            sExternalInterfaceMap.put("showLoading","com.loheng.jsinterfacedemo.interfaces.ExternalInterface$showLoading");
        }
        return sExternalInterfaceMap;
    }

    private static Map<String,String> getTestInterfaceMap() {

        if(sTestInterfaceMap.size() == 0){
            sTestInterfaceMap.put("showDialog1","com.loheng.jsinterfacedemo.interfaces.TestInterface$showDialog1");
            sTestInterfaceMap.put("showToast","com.loheng.jsinterfacedemo.interfaces.TestInterface$showToast");
            sTestInterfaceMap.put("helloWorld","com.loheng.jsinterfacedemo.interfaces.TestInterface$helloWorld");
            sTestInterfaceMap.put("show_dialog","com.loheng.jsinterfacedemo.interfaces.TestInterface$Toolbar123");
        }
        return sTestInterfaceMap;
    }

    private static Map<String,String> getGreeInterfaceMap() {

        if(sGreeInterfaceMap.size() == 0){
            sGreeInterfaceMap.put("show_toast","com.loheng.jsinterfacedemo.interfaces.GreeInterface$showToast");
            sGreeInterfaceMap.put("showToast","com.loheng.jsinterfacedemo.interfaces.GreeInterface$showToast");
            sGreeInterfaceMap.put("show_dialog","com.loheng.jsinterfacedemo.interfaces.GreeInterface$showDialog");
        }
        return sGreeInterfaceMap;
    }

}
