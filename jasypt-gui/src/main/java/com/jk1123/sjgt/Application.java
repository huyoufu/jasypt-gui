package com.jk1123.sjgt;

import com.jk1123.sjgt.jasypt.JasyptFactory;
import com.jk1123.sjgt.jasypt.config.JasyptConfig;
import com.jk1123.sjgt.ui.MainFrame;
import com.jk1123.sjgt.ui.config.GuiConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;

/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/19 2:40
 * @description
 */
public class Application {
    public static void main(String[] args) {

        checkOne();

        JasyptConfig jasyptConfig=loadJasyptConfig();
        JasyptFactory jasyptFactory=new JasyptFactory(jasyptConfig);

        GuiConfig guiConfig=loadGuiConfig();
        MainFrame mainFrame = new MainFrame();
        mainFrame.init(jasyptFactory,guiConfig);

        writePid();
    }

    private static void writePid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        String home = System.getProperty("user.home");
        try {
            File parent = new File(home, Constants.CONFIG_DIRECTORY_NAME);
            if (!parent.exists()){
                parent.mkdirs();
            }


            FileOutputStream outputStream = new FileOutputStream(new File(home, Constants.PID_FILE_NAME));
            outputStream.write(pid.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void checkOne() {
        //TODO:保证只有一个进行再运行 如果有 则唤醒/显示出原来的界面


    }

    private static GuiConfig loadGuiConfig() {
        //TODO: 实现配置文件的保存操作
        return new GuiConfig();
    }

    private static JasyptConfig loadJasyptConfig() {
        //TODO: 实现配置文件的保存操作
        return new JasyptConfig();
    }

    private static JasyptConfig loadConfig() {
        //TODO: 实现配置文件的保存操作
        return null;
    }

}
