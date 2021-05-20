package com.gree.plugininterfacelib;

import android.content.Context;
import android.nfc.FormatException;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lzh
 * @mail 560182@gree.com.cn
 * @time 5/15/21 12:22 PM
 * @detail 这个是方法中转站，跳转到对应方法位置
 * 使用方式：三个方法是重点
 * 1 初始化 new {@link #MethodRouter(Context)}
 * 2 读取要加载的接口s {@link #loadInterface }
 * 3 调用方法执行 {@link #invoke(String, Object...)}
 */
public class MethodRouter {

    private static final String TAG = "InterfaceProxy";

    /**
     * js-->native 方法名称映射表
     */
    private Map<String, String> methodMap = new HashMap<>();
    /**
     * 接口实例表
     */
    private ArrayMap<String, Object> objMap = new ArrayMap<>();
    /**
     * webView activity的上下文
     * 记得释放，接口文件中不要胡乱使用导致内存泄漏
     */
    private Context mContext;

    /**
     * 构造函数 要传入上下文
     *
     * @param context activity上下文
     */
    public MethodRouter(Context context) {
        mContext = context;
    }

    /**
     * 加载 单个 接口文件class
     *
     * @param cls class
     * @return
     */
    public MethodRouter loadInterface(Class<?> cls) {
        //绑定需要处理注解的类
        Map<String, String> map = InterfaceLib.getMap(cls.getName());
        if (map != null) {
            methodMap.putAll(map);
        } else {
            Log.e(TAG, cls.getName() + "加载失败");
        }
        return this;
    }

    /**
     * 加载 多个 接口文件class
     *
     * @param classesStr 字符串形式传入
     * @return
     */
    public MethodRouter loadInterface(String[] classesStr) throws ClassNotFoundException {
        for (String strCls : classesStr) {
            Class<?> tmpCls = Class.forName(strCls);
            //绑定需要处理注解的类
            Map<String, String> map = InterfaceLib.getMap(strCls);
            if (map != null) {
                methodMap.putAll(map);
            } else {
                Log.e(TAG, tmpCls.getName() + "加载失败");
            }
        }
        return this;
    }

    /**
     * 加载 单个 接口文件class
     *
     * @param classStr 字符串形式传入
     * @return
     */
    public MethodRouter loadInterface(String classStr) throws ClassNotFoundException {
        //绑定需要处理注解的类
        Map<String, String> map = InterfaceLib.getMap(classStr);
        if (map != null) {
            methodMap.putAll(map);
        } else {
            Log.e(TAG, classStr + "加载失败");
        }
        return this;
    }

    /**
     * 加载接口文件class
     *
     * @param cls 字符串形式传入
     */
    public MethodRouter loadInterface(Class<?>[] cls) {
        //绑定需要处理注解的类
        long mill1 = System.currentTimeMillis();
        for (Class<?> tmpCls : cls) {

//            if (!NativeAnnotationProcessor.bind(this, tmpCls)) {
//                Log.e(TAG,tmpCls.getName() + "加载失败" );
//            }

            Map<String, String> map = InterfaceLib.getMap(tmpCls.getName());
            if (map != null) {
                methodMap.putAll(map);
            } else {
                Log.e(TAG, tmpCls.getName() + "加载失败");
            }

        }
        Log.d(TAG, (System.currentTimeMillis() - mill1) + "耗费时间");

        return this;
    }

    /**
     * 关键的执行方法
     *
     * @param MethodName 调用方法的名称
     * @param args       调用方法的参数
     */
    public Object invoke(String MethodName, Object... args) {

        // step 1:找到映射表的方法名称和类名
        String[] fullStr;
        try {
            fullStr = findMethod(MethodName);

        } catch (NoSuchMethodException | FormatException e) {
            e.printStackTrace();
            //异常
            return null;
        }
        //方法名称
        String methodName = fullStr[1];
        //类名
        String className = fullStr[0];

        //step 2:执行真正的函数
        return invokeMethod(className, methodName, args);
    }

    /**
     * 通过js想调用的方法，找到原生的方法
     *
     * @param name js那边想要调用的方法名称
     * @return 返回数组 [0] --> 类名称   ，[1] --> 方法名称
     */
    private String[] findMethod(String name) throws NoSuchMethodException, FormatException {
        //找出 "类名 $ 方法名" 的字符串
        String fullName = methodMap.get(name);
        if (fullName == null) {
            throw new NoSuchMethodException("Method not found.");
        }
        String[] nameStrings = fullName.split("\\$");
        if (nameStrings.length != 2) {
            // 符合规范 能够通过 $ 分割出来 才能使用，否则抛出异常
            throw new FormatException("Class name format error.Please check the input method.");
        }
        return nameStrings;
    }

    /**
     * 运行某个类的某个方法
     *
     * @param methodName 方法名称
     * @param args       不定长参数
     */
    private Object invokeMethod(String className, String methodName, Object... args) {
        Object result = null;
        try {
            //通过字符串的类名找到对应class类
            Class<?> clz = Class.forName(className);
            //拿到方法
            Method method = getOverloadMethod(methodName, clz, args);
            //创建实例
            Object obj = makeInterfaceObj(clz);
            //调用方法
            result = method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建接口类对象实例
     *
     * @param clz 类 class对象
     * @return 返回创建好的对象实例
     */
    private Object makeInterfaceObj(Class<?> clz) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Object object = objMap.get(clz.getName());

        if (object == null) {
            //找到有参数的方法
            Constructor constructor = getOverloadConstructor(clz, new Object[]{Context.class});
            //创建实例
            object = constructor.newInstance(mContext);
            //创建好了之后放入map里面，下次不用创建
            objMap.put(clz.getName(), object);
        }
        return object;
    }


    /**
     * 找到方法
     * 因为涉及重载方法，自带的getMethod 和 getDeclaredMethod都不能满足，因此自己实现
     *
     * @param methodName 方法名称
     * @param cls        方法所在的类
     * @param args       携带的参数，为了通过参数找到对应重载方法
     * @return 找到返回方法，找不到返回null
     */
    private Method getOverloadMethod(String methodName, Class<?> cls, Object[] args) throws NoSuchMethodException {
        Method method = null;
        //找到类中左右的方法
        Method[] methods = cls.getDeclaredMethods();
        for (Method tmpMethod : methods) {
            //找到匹配名称的类
            if (tmpMethod.getName().equals(methodName)) {
                //找到参数长度匹配的类
                if (tmpMethod.getGenericParameterTypes().length == args.length) {
                    boolean isFound = true;
                    for (int i = 0; i < args.length; i++) {
                        try {
                            //查看是否能够强转，如果不抛出异常，说明该参数匹配
                            args[i].getClass().asSubclass(((Class<?>) tmpMethod.getParameterTypes()[i]));
                        } catch (ClassCastException e) {
                            //抛出异常，说明参数类型不匹配，直接退出
                            isFound = false;
                            break;
                        }
                    }
                    if (isFound) {
                        //如果全部匹配，说明找到
                        method = tmpMethod;
                        break;
                    }
                }
            }
        }
        if (method == null) {
            throw new NoSuchMethodException("未找到该方法");
        }
        return method;
    }


    /**
     * 找到方法
     * 因为涉及重载方法，自带的getMethod 和 getDeclaredMethod都不能满足，因此自己实现
     *
     * @param cls  对应构造函数所在的类
     * @param args 传入的参数
     * @return 找到返回方法，找不到返回null
     */
    private Constructor getOverloadConstructor(Class<?> cls, Object[] args) throws NoSuchMethodException {
        Constructor method = null;
        //找到类中左右的方法
        Constructor[] constructors = cls.getDeclaredConstructors();
        for (Constructor tmpConstruct : constructors) {
            //找到匹配名称的类
            //找到参数长度匹配的类
            if (tmpConstruct.getGenericParameterTypes().length == args.length) {
                boolean isFound = true;
                for (int i = 0; i < args.length; i++) {
                    //查看是否能够强转，如果不抛出异常，说明该参数匹配
                    if (!args[i].toString().equals(tmpConstruct.getParameterTypes()[i].toString())) {
                        isFound = false;
                        break;
                    }
                }
                if (isFound) {
                    //如果全部匹配，说明找到
                    method = tmpConstruct;
                    break;
                }
            }

        }
        if (method == null) {
            throw new NoSuchMethodException("未找到该构造方法");
        }
        return method;
    }

    /**
     * 将类放入表格中
     *
     * @param jsName     js 所调用名称
     * @param nativeName 原生方法对应的名称和类名 格式：类名$方法名
     */
    public void put(String jsName, String nativeName) {
        methodMap.put(jsName, nativeName);
    }
}
