package com.jk1123.sjgt.util;

import java.lang.management.ManagementFactory;


/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/28 2:28
 * @description 获取操作系统类型:
 * System.getProperty("os.name");
 * 在不同的操作系统返回的值:
 * <a href="https://www.mindprod.com/jgloss/properties.html#OSNAME">参考这里</a>
 * AIX (Advanced Interactive eXecutive)
 * Digital Unix
 * FreeBSD
 * HP (Hewlett Packard) UX
 * Irix
 * Linux
 * Mac OS (Operating System)
 * Mac OS X
 * MPE/iX
 * Netware 4.11
 * OS/2
 * Solaris
 * Windows 2000
 * Windows 7
 * Windows 8
 * Windows 10
 * Windows 95
 * Windows 98
 * Windows NT
 * Windows Vista
 * Windows XP
 *
 * 我们这里只考虑 macos windows 和linux 其他 的不管
 */
public class PlatformDetector {
    public enum OsType {
        WINDOWS, MACOS, LINUX, OTHER
    }

    /**
     * 获取操作系统的名字
     *
     * @return
     */

    public static OsType osType() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")){
            return OsType.WINDOWS;
        }
        if (osName.startsWith("Linux")){
            return OsType.LINUX;
        }
        if (osName.startsWith("Mac")){
            return OsType.MACOS;
        }
        return OsType.OTHER;
    }
}
