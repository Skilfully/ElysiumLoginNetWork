package cn.skilfully.elysiumloginnetwork.util;

import cn.skilfully.elysiumloginnetwork.out.Log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * 操作yml文件
 */
public class YamlOperate {

    public static Object getVal(String resourcePath, String key){
        return getVal(resourcesToYamlConfiguration(resourcePath),key);
    }

    public static Object getVal(File file, String key){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return getVal(config,key);
    }

    public static Object getVal(@NotNull YamlConfiguration config, String key){
        return config.get(key);
    }

    /**
     * 添加key和初始值
     * @param file 操作的文件
     * @param key 键
     * @param val 值
     * @throws IOException IOException
     */
    public static void addKey(@NotNull File file, @NotNull String key, @NotNull Object val) throws IOException {
        // 转换为 YamlConfiguration 对象
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        // 设置键 默认值为“”
        config.set(key,val);
        //保存
        config.save(file.getPath());
    }

    /**
     * 获取所有键
     * @param in 文件的File对象
     * @return 包含所有键的Set
     */
    public static @NotNull Set<String> getAllKeys(@NotNull File in){
        // 检测到为资源目录的文件
        if (in.toPath().startsWith("resources/")){
            // 报错，谁用谁倒霉 :D
            return getAllKeys(in.getPath().substring("resources/".length()));
        }
        return getAllKeys(in,"");
    }

    /**
     * 获取所有键
     * @param in 文件的File对象
     * @param path 路径
     * @return 包含所有键的Set
     */
    public static @NotNull Set<String> getAllKeys(File in, String path) {
        return getAllKeys(YamlConfiguration.loadConfiguration(in),path);
    }

    /**
     * 获取所有键
     * @param config 文件的FileConfiguration对象
     * @param path 路径
     * @return 包含所有键的Set
     */
    public static @NotNull Set<String> getAllKeys(@NotNull FileConfiguration config, String path){
        // 创建集合
        Set<String> keys = config.getKeys(true);
        Set<String> allKeys = new java.util.HashSet<>();

        // 循环获取键并合成完整路径
        for (String key : keys) {
            String fullPath = path.isEmpty() ? key : path + "." + key;
            allKeys.add(fullPath);
            // 如果为嵌套目录
            if (config.isConfigurationSection(fullPath)) {
                allKeys.addAll(getAllKeys(config, fullPath));
            }
        }

        return allKeys;
    }

    /**
     * 获取所有键
     * @param resourcePath 在资源文件中的路径
     * @return 包含所有键的Set
     */
    public static @NotNull Set<String> getAllKeys(String resourcePath){
        return getAllKeys(resourcesToYamlConfiguration(resourcePath),"");
    }

    /**
     * 资源文件转yml
     * @param resourcePath 资源文件的路径
     * @return YamlConfiguration对象
     */
    public static YamlConfiguration resourcesToYamlConfiguration(String resourcePath){
        // 获取类加载器
        ClassLoader classLoader = YamlOperate.class.getClassLoader();

        // 加载资源文件
        InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
        try {
            if (inputStream == null){
                Log.getLog().outError("加载源文件失败");
                throw new IllegalAccessException("文件未找到" + resourcePath);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // 将输入流转换为 FileConfiguration 对象
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));

        // 关闭输入流
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
}
