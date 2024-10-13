package cn.skilfully.elysiumloginnetwork.out;

import cn.skilfully.elysiumloginnetwork.var.COLOR;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ColorLog {
    ColorLog(){
        super();
    }

    public void outInfo(String color, String str){
        CommandSender commandSender = Bukkit.getConsoleSender();
        commandSender.sendMessage(color+str);
    }
}
