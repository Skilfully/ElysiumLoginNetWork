package cn.skilfully.elysiumloginnetwork;

import cn.skilfully.elysiumloginnetwork.out.Log;
import cn.skilfully.elysiumloginnetwork.util.FileOperate;
import cn.skilfully.elysiumloginnetwork.var.InsideGlobalVar;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class ElysiumLoginNetWork extends JavaPlugin {
    private final Log logger = Log.getLog();
    @Override
    public void onEnable() {
        // Plugin startup logic
        // 获取工作目录
        InsideGlobalVar.workPath = getDataFolder();
        // 获取插件
        InsideGlobalVar.pluginFile = getFile();

        // 初始化配置文件
        try {
            FileOperate.onEnable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
