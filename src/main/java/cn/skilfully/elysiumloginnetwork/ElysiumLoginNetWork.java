package cn.skilfully.elysiumloginnetwork;

import cn.skilfully.elysiumloginnetwork.out.Log;
import cn.skilfully.elysiumloginnetwork.util.FileOperate;
import cn.skilfully.elysiumloginnetwork.util.YamlOperate;
import cn.skilfully.elysiumloginnetwork.var.InsideGlobalVar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.xyit.elysiumlogin.updateStream.NetWork;

import java.io.File;
import java.io.IOException;

public final class ElysiumLoginNetWork extends JavaPlugin {
    private final Log logger = Log.getLog();
    @Override
    public void onEnable() {
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

        // 检插功能是否开启

        if (YamlOperate.getVal(new File(InsideGlobalVar.workPath+"\\config\\config.yml"),"isOpen").toString().equals("false")){
            logger.ok("检测到功能未开启，以关闭插件");
            disablePlugin();
            return;
        }

        // 在主插件通知自检成功
        NetWork.ok();

        // 启动对应的网络流程

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.ok("shut down");
    }

    public void disablePlugin(){
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
