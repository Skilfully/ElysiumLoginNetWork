package cn.skilfully.elysiumloginnetwork.out;

import cn.skilfully.elysiumloginnetwork.var.COLOR;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * 通用输出类
 */
public class ColorLog {
    ColorLog(){
        super();
    }

    /**
     * 通用输出方法
     * @param color 颜色
     * @param str 输出内容
     */
    public void defaultInfo(String color, String str){
        CommandSender commandSender = Bukkit.getConsoleSender();
        commandSender.sendMessage( COLOR.GOLD + "[NetWork]" + color + str);
    }

    /**
     * 一般输出方法，多了自动恢复功能
     * @param color 颜色
     * @param str 输出内容
     */
    public void outInfo(String color, String str){
        defaultInfo(COLOR.REST+color,str);
    }

    /**
     * 正常过程输出，颜色为绿色
     * @param str 字符
     */
    public void ok(String str){
        outInfo(COLOR.GREEN,"[OK]"+str);
    }

    public void smallOutWarn(String str){
        outInfo(COLOR.YELLOW,"[Warn]"+str);
    }

    public void outWarn(String str){
        outInfo(COLOR.RED,"[Warn]"+str);
    }

    public void outError(String str){
        outInfo(COLOR.DARK_ERD,str);
    }
}
