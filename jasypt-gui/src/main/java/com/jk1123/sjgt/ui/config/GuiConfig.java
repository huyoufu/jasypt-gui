package com.jk1123.sjgt.ui.config;

/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/21 22:57
 * @description
 */
public class GuiConfig {
    public static final String PROGRAM_TITLE="jasypt密码生成工具";
    public static final int DEFAULT_WIDTH=800;
    public static final int DEFAULT_HEIGHT=600;
    private int width=DEFAULT_WIDTH;
    private int height=DEFAULT_HEIGHT;

    public int getWidth() {
        return width;
    }

    public GuiConfig setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public GuiConfig setHeight(int height) {
        this.height = height;
        return this;
    }
}
