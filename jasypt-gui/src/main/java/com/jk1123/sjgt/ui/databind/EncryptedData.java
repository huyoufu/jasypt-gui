package com.jk1123.sjgt.ui.databind;

/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/19 2:40
 * @description
 */
public class EncryptedData{
    private static String passwordTextField4Hidden;
    private static boolean hidden;

    private String sourceTextArea;
    private String resultTextArea;
    private String passwordField;

    public EncryptedData() {
    }

    public String getSourceTextArea() {
        return sourceTextArea;
    }

    public void setSourceTextArea(final String sourceTextArea) {
        this.sourceTextArea = sourceTextArea;
    }

    public String getResultTextArea() {
        return resultTextArea;
    }

    public void setResultTextArea(final String resultTextArea) {
        this.resultTextArea = resultTextArea;
    }

    public static String getPasswordTextField4Hidden() {
        return passwordTextField4Hidden;
    }

    public static void setPasswordTextField4Hidden(String passwordTextField4Hidden) {
        EncryptedData.passwordTextField4Hidden = passwordTextField4Hidden;
    }


    public String getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(final String passwordField) {
        this.passwordField = passwordField;
    }
}