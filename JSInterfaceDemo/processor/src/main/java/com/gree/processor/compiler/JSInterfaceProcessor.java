package com.gree.processor.compiler;


import com.google.auto.service.AutoService;
import com.gree.processor.annotation.NativeInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @author lzh
 * @mail 560182@gree.com.cn
 * @time 5/19/21 3:06 PM
 * @detail 这是个注解处理器，处理一些自定义注解的，提供给{jsInterface#MethodRouter}框架使用
 *         一般不用更改，如果修改了模块的名称(原本为jsInterfaceLib)那就对应修改{@link #MOVE_TO_MODULE}
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.loheng.annotation.NativeInterface"})
public class JSInterfaceProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private Map<String,Map<String,String>> mClassMap;
    /**
     *   最终生成的包名
     */
    private final static String PACKAGE_NAME = "com.gree.plugininterfacelib" ;

    /**
     * 写入的文件名称
     */
    public static final String FILE_NAME = "InterfaceLib";

    /**
     * /代码目录/jsInterfaceLib/java/对应包的目录
     */
    public static final String PATH = PACKAGE_NAME + "." + FILE_NAME;

    /**
     * 调用这个processor的模块
     */
    public static final String USED_MODULE = "app";

    /**
     * 移动到对应的模块
     */
    public static final String MOVE_TO_MODULE = "pluginInterfaceLib";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        mClassMap = new HashMap<>(8);
        HashMap<String,String> methodList = new HashMap<>(8);

        String className = "";
        String nativeName = "";
        //找到环境中 带着这个注解的element
        for (Element element : roundEnv.getElementsAnnotatedWith(NativeInterface.class)) {
            //方法所在类
            TypeElement classElement = (TypeElement) element.getEnclosingElement();
            //可执行的方法的Element
            ExecutableElement execElement = (ExecutableElement) element;
            //注解的js名称
            String jsName = element.getAnnotation(NativeInterface.class).jsName();

            //className发生变化的时候，说明切换到下一个文件了，存储和刷新数据
            if (!classElement.toString().equals(className)) {
                //全类名
                className = classElement.toString();
                methodList = new HashMap<>();
                mClassMap.put(className, methodList);
            }
            //全方法名称
            nativeName = execElement.getSimpleName().toString();
            jsName = jsName.isEmpty() ? nativeName : jsName;
            methodList.put(jsName, className + "$" + nativeName);

        }
        //生成文件
        generateFile(PATH);

        return true;
    }

    private void generateFile(String path) {
        OutputStreamWriter mWriter = null;
        messager.printMessage(Diagnostic.Kind.NOTE, "全类名：" + path);
        try {

            //找到项目目录的相对路径
            FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "tmpFile");
            String resourcePath = resource.toUri().getPath();
            resource.delete();
            messager.printMessage(Diagnostic.Kind.NOTE, "生成一个文件，准备获取相对路径:" + resourcePath);

            //截取我们需要的路径，然后切换模块 从 app 切换到>>> MODULE_NAME
            String relative = resourcePath.substring(0, resourcePath.indexOf(USED_MODULE) + 4).replaceAll(USED_MODULE, MOVE_TO_MODULE);

            //找到对应模块下的路径
            String javaPath = "src/main/java/";
            //最终生成的名称路径
            String appPath = relative + javaPath + PACKAGE_NAME.replace(".", "/");
            messager.printMessage(Diagnostic.Kind.NOTE, "appPath:" + appPath);

            File file = new File(appPath);

            if (!file.exists()) {
                file.mkdirs();
            }
            //此处就是稳健的写入了 记得加个.java
            File outPutFile = new File(file, FILE_NAME + ".java");
            if (outPutFile.exists()) {
                //删除原本存在的
                outPutFile.delete();
            }
            outPutFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(outPutFile);

            mWriter = new OutputStreamWriter(fos, "UTF-8");
            if (PACKAGE_NAME != null) {
                mWriter.write("package " + PACKAGE_NAME + ";\n\n");
            }
            mWriter.write("import java.util.HashMap;\n");
            mWriter.write("import java.util.Map;\n\n");
            mWriter.write("/**\n" + " * @author lzh\n");
            mWriter.write(" * @detail 这个类是由JsInterfaceProcessor自动生成，点击gradle任务的\n" +
                            " *         compileDebugJavaWithJavac或者build 自动生成");
            mWriter.write(" \n */\n");
            mWriter.write("public class " + FILE_NAME + " {\n");
            //写定义
            writeMapDefine(mWriter);
            //写getMap方法
            writeGetMapMethod(mWriter);
            //写具体方法
            writeGetSpecificMethod(mWriter);

            mWriter.write("}\n");
        }catch (FilerException e){

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mWriter != null) {
                try {
                    mWriter.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 写getMapMethod
     * @param writer
     * @throws IOException
     */
    private void writeGetMapMethod(Writer writer) throws IOException {
        writer.write("    public static Map<String,String> getMap(String name){\n");
        writer.write("        switch (name){\n");
        Iterator<String> iterator = mClassMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            int index = key.lastIndexOf(".");
            String simpleClassName = key.substring(index + 1);

            writer.write("            case \"" + key + "\":\n");
            writer.write("                return get" + simpleClassName + "Map();\n");
        }
        writer.write("            default:\n" + "                return null;\n");
        writer.write("        }\n    }\n\n");
    }

    /**
     * 写方法定义和新建
     * @param writer
     * @throws IOException
     */
    private void writeMapDefine(Writer writer) throws IOException {
        Iterator<String> iterator = mClassMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            int index = key.lastIndexOf(".");
            String simpleClassName = key.substring(index + 1);
            writer.write("    private static Map<String,String> s" + simpleClassName + "Map = new HashMap<>();\n\n");
        }
    }

    /**
     * 写具体的get方法
     * @param writer
     */
    private void writeGetSpecificMethod(Writer writer) throws IOException {
        Iterator<String> iterator = mClassMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            int index = key.lastIndexOf(".");
            String simpleClassName = key.substring(index + 1);
            Map<String,String> jsNativeMap = mClassMap.get(key);
            String mapName = "s" + simpleClassName + "Map";
            writer.write("    private static Map<String,String> get" + simpleClassName + "Map" + "() {\n\n");
            writer.write("        if(" + mapName + ".size() == 0){\n");
            Iterator<String> jsIterator = jsNativeMap.keySet().iterator();
            while (jsIterator.hasNext()) {
                String jsName = jsIterator.next();
                String nativeName = jsNativeMap.get(jsName);
                writer.write("            " + mapName + ".put(\"" + jsName + "\",\"" + nativeName + "\");\n");
            }
            writer.write("        }\n");
            writer.write("        return " + mapName + ";\n");
            writer.write("    }\n\n");
        }
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}