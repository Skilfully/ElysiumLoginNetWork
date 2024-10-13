package cn.skilfully.elysiumloginnetwork;

import cn.skilfully.elysiumloginnetwork.out.Log;
import cn.skilfully.elysiumloginnetwork.var.COLOR;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElysiumLoginNetWork extends JavaPlugin {
    private final Log logger = Log.getLog();
    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("info");
        logger.outInfo(COLOR.YELLOE,"h");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
