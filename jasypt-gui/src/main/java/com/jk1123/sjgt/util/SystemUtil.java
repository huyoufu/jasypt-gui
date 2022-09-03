package com.jk1123.sjgt.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;


/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/26 22:47
 * @description
 */
public class SystemUtil {
    /**
     * 获取当前进程编号
     * @return
     */
    public static int getPid(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.valueOf(name.split("@")[0]);
    }
    public static boolean existProcess(int pid){
        switch (PlatformDetector.osType()){
            case WINDOWS:
                return existProcess4Windows(pid);
            case MACOS:
                return existProcess4Macos(pid);
            case LINUX:
                return existProcess4Linux(pid);
            default:
                return false;
        }
    }

    /**
     * window系统的判断逻辑
     * @param pid
     * @return
     */
    private static boolean existProcess4Windows(int pid) {
        try {
            String[] command = { "cmd", "/c", "tasklist /fi \"pid eq "+pid+"\" /nh"};
            Process process = Runtime.getRuntime().exec(command);
            String osEncoding = System.getProperty("sun.jnu.encoding");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(),osEncoding));
            String line;
            int lineLen=0;
            while ((line = br.readLine()) != null) {
                lineLen++;
            }
            if (lineLen>1){
                return true;
            }
            process.waitFor();
            br.close();
            return false;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * mac系统的判断
     * @param pid
     * @return
     */
    private static boolean existProcess4Macos(int pid){
        return existProcess4Unix("/bin/sh", pid);
    }

    /**
     * linux系统的判断
     * @param pid
     * @return
     */
    private static boolean existProcess4Linux(int pid){
        return existProcess4Unix("/usr/bin/sh",pid);
    }
    private static boolean existProcess4Unix(String sh, int pid){
        try {
            String[] command = { sh, "-c", "ps -p "+pid+""};
            Process process = Runtime.getRuntime().exec(command);
            String osEncoding = System.getProperty("sun.jnu.encoding");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(),osEncoding));
            String line;
            int lineLen=0;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                lineLen++;
            }
            if (lineLen>1){
                return true;
            }
            process.waitFor();
            br.close();
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(existProcess(405));
    }
}
