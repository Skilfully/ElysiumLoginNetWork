package cn.skilfully.elysiumloginnetwork.out;

import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

/**
 * 标准日志输出类
 */
public class Log extends ColorLog{
    /**
     * 日志输出实例
     */
    private final Logger logger = getLogger();
    /**
     * 私有化实例
     */
    private static Log log;
    private Log(){
        super();
    }
    public static synchronized Log getLog(){
        if (log == null){
            log = new Log();
        }
        return log;
    }

    /**
     * 信息等级输出
     * @param str 输出信息
     */
    public void info(String str){
        logger.info(str);
    }

    /**
     * 警告等级输出
     * @param str 输出信息
     */
    public void warn(String str){
        logger.warning(str);
    }

    /**
     * 错误等级输出
     * @param str 输出信息
     */
    public void error(String str){
        logger.severe(str);
    }
}
