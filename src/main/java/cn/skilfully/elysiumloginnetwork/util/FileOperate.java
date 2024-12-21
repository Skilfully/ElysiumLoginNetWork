package cn.skilfully.elysiumloginnetwork.util;

import cn.skilfully.elysiumloginnetwork.out.Log;
import cn.skilfully.elysiumloginnetwork.var.InsideGlobalVar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 操作文件的类
 */
public class FileOperate {
    /**
     * 初始化文件系统
     * @throws IOException IOException
     */
    public static void onEnable() throws IOException {
        //创建工作目录
        if (!InsideGlobalVar.workPath.exists()) {
            Log.getLog().ok("初次启动，正在创建目录并初始化");
            if (!InsideGlobalVar.workPath.mkdir())
                Log.getLog().outWarn("创建工作目录失败");
        }

        // 检查文件完整性
        Log.getLog().ok("正在自检...");
        // 补齐缺失文件
        Log.getLog().ok("正在补齐文件....");
        unZipFolder();
        // 检查文件完整性
        Log.getLog().ok("正在检查文件完整性....");
        complete();
        // 完成
        Log.getLog().ok("自检成功...");
    }

    /**
     * 检查文件完整性
     */
    private static void complete() throws IOException {
        // 获取所有源配置文件
        Set<String> sources = getAllFileInResources();
        // 获取所有工作目录配置文件
        Set<String> works = getAllFileInWorkPath();
        // 判断是否完整
        for (String sou : sources){
            for (String work : works){
                File souFile = new File(sou);
                File workFile = new File(work);

                String souName = souFile.getName();
                String workName = workFile.getName();

                // 判断是否是一个文件
                if (Objects.equals(souName,workName)){
                    if (!isFileComplete(sou,workFile)){
                        Log.getLog().outWarn("文件 " + souName + " 不完整，正在修复文件.....");
                        fileFix(sou,workFile);
                    }
                }

            }
        }
    }

    /**
     *
     * @deprecated 我都不知道为什么要有这个方法，他什么都不会干，用{@link #fileFix }去
     * @param source 原始文件
     * @param fixed 需要修复的文件
     */
    @Deprecated
    private static void fileFix(File source, File fixed){
        Set<String> sourcesKeys = YamlOperate.getAllKeys(source);
        Set<String> fixedKeys = YamlOperate.getAllKeys(fixed);
    }

    /**
     * 修复文件缺失部分
     * @param sourcePath 资源文件的路径
     * @param fixed 需要修复的文件的File对象
     */
    private static void fileFix(String sourcePath, File fixed) throws IOException {
        // 获取需要修复的文件键集合
        Set<String> sourcesKeys = YamlOperate.getAllKeys(sourcePath);
        Set<String> fixedKeys = YamlOperate.getAllKeys(fixed);

        // 获取迭代器
        Iterator<String> ISource = sourcesKeys.iterator();
        Iterator<String> IFixe = fixedKeys.iterator();

        // 删除都有的元素，只保留差集
        Set<String> tmp = new HashSet<>(sourcesKeys);
        sourcesKeys.removeAll(fixedKeys);
        fixedKeys.removeAll(tmp);

        if (ISource.equals(IFixe)){
            return;
        }

        // 修复
        // 当只缺少键时
        if (fixedKeys.isEmpty()){
            Log.getLog().outWarn("配置文件 " + fixed.getName() + " 缺少必须键！正在补全.....");
            // 补充键
            for (String key : sourcesKeys){
                YamlOperate.addKey(fixed,key,YamlOperate.getVal(sourcePath,key));
            }
            Log.getLog().ok("成功补全 " + fixed.getName() + " 请填写正确值！");
            return;
        }

        // 当只多余时
        if (sourcesKeys.isEmpty()){
            // 懒
            Log.getLog().outWarn("配置文件 " + fixed.getName() + " 有多余键！请手动删除！自检过程中只保证必须键正确！！！");
            return;
        }

        // 当完全不一样时
        Log.getLog().smallOutWarn("配置文件 " + fixed.getName() + " 有多余键并且缺失必须键！请手动删除多余键！自检过程中只保证必须键正确！！！");
        for (String key : sourcesKeys){
            YamlOperate.addKey(fixed,key,YamlOperate.getVal(sourcePath,key));
        }
    }

    /**
     * 获取resources里的所有配置文件
     * @return 包含所有配置文件路径的Set集合
     */
    private static @NotNull Set<String> getAllFileInResources() {
        // 创建集合
        Set<String> set = new HashSet<>();

        // 获取插件JAR文件的路径
        try (JarFile jarFile = new JarFile(InsideGlobalVar.pluginFile)) {

            // 获取资源文件夹的条目
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith("resources/config/") && !entry.isDirectory()) {
                    set.add(entryName);
                }
            }
        }catch (IOException e){
            Log.getLog().error(e.getMessage());
        }
        return set;
    }

    /**
     * 获取工作目录里的所有配置文件
     * @return 包含所有配置文件路径的Set集合
     */
    private static @NotNull Set<String> getAllFileInWorkPath(){
        return getAllFileInPath(InsideGlobalVar.workPath);
    }

    /**
     * 获取指定目录里的所有配置文件
     * @param path 目录
     * @return 包含所有配置文件路径的Set集合
     */
    private static @NotNull Set<String> getAllFileInPath(@NotNull File path){
        // 创建集合
        Set<String> set = new HashSet<>();
        //获取所有文件
        File[] files = path.listFiles();
        // 如果文件存在
        if (files != null){
            for (File file : files){
                if (file.isDirectory()){
                    // 嵌套目录 递归
                    set.addAll(getAllFileInPath(file));
                }else{
                    //添加
                    set.add(file.getPath());
                }
            }
        }

        return set;
    }

    /**
     * 文件完整性
     * @param source 原始配置文件
     * @param in 需要比较的配置文件
     * @return true完整 false不完整
     */
    private static boolean isFileComplete(File source, File in){
        return YamlOperate.getAllKeys(source).equals(YamlOperate.getAllKeys(in));
    }

    private static boolean isFileComplete(String path, @NotNull File in){
        return YamlOperate.getAllKeys(path).equals(YamlOperate.getAllKeys(in));
    }

    /**
     * 解压整个resources文件夹到工作目录
     * @throws IOException IOException
     */
    private static void unZipFolder() throws IOException {
        // 获取插件JAR文件的路径
        JarFile jarFile = new JarFile(InsideGlobalVar.pluginFile);

        // 获取资源文件夹的条目
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();

            // 检查是否在resources文件夹中
            if (entryName.startsWith("resources/config/") && !entry.isDirectory()){
                if (!isFileExist(entryName)) {
                    Log.getLog().outWarn("检测到 " + entryName.substring("resources/config/".length()) + " 文件不存在，正在解压....");

                    // 获取目标文件路径
                    File targetFile = new File(InsideGlobalVar.workPath+"/config/",entryName.substring("resources/config/".length()));

                    // 确保目标目录存在
                    File targetDir = targetFile.getParentFile();
                    if (!targetDir.exists() && !targetDir.mkdirs()){
                        throw new IOException("无法创建目标目录:" + targetDir.getAbsolutePath());
                    }

                    InputStream inputStream= jarFile.getInputStream(entry);
                    Files.copy(inputStream,targetFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

                    // 关闭输入流
                    inputStream.close();
                    Log.getLog().ok("解压 " + entryName.substring("resources/config/".length()) + " 成功");
                }
            }
        }

        jarFile.close();
    }

    /**
     * 解压单个文件到指定目录
     * @param resourcePath 配置文件路径
     * @param targetFile 指定目录
     */
    private static void unZipFile(String resourcePath, @NotNull File targetFile){
        try{
            // 确保目标目录存在
            if (!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();
            }

            // 获取资源文件的输入流
            InputStream inputStream = FileOperate.class.getResourceAsStream(resourcePath);
            if (inputStream == null){
                throw new FileNotFoundException("资源文件未找到" + resourcePath.substring("resources/".length()));
            }

            // 复制文件
            Files.copy(inputStream,targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            //关闭输入流
            inputStream.close();

            Log.getLog().ok("解压" + resourcePath.substring("resources/".length()) + "成功");
        } catch (IOException e) {
            Log.getLog().error("解压失败") ;
        }
    }

    /**
     * 文件是否存在
     * @param path 文件路径
     * @return true存在，false不存在
     */
    private static boolean isFileExist(@NotNull String path){
        return new File(InsideGlobalVar.workPath,path.substring("resources/".length())).exists();
    }
}
