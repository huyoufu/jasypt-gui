package com.jk1123.sjgt.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jk1123.sjgt.Constants;
import com.jk1123.sjgt.jasypt.JasyptFactory;
import com.jk1123.sjgt.ui.config.GuiConfig;
import com.jk1123.sjgt.ui.databind.EncryptedData;
import org.jasypt.encryption.StringEncryptor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/19 14:44
 * @description
 */
public class MainFrame {
    private JFrame frame;
    private JPanel root;
    private JLabel passwordLabel;
    private JTextArea sourceTextArea;
    private JTextField passwordTextField;
    private JCheckBox passwordCheckBox;
    private JPanel passwordContainer;
    private JPanel passwordCheckboxContainer;
    private JPanel passwordTextFieldContainer;
    private JPanel passwordLabelContainer;
    private JPanel sourceContainer;
    private JPanel sourceLabelContainer;
    private JLabel sourceLabel;
    private JButton sourceButton;
    private JPanel sourceButtonContainer;
    private JTextArea resultTextArea;
    private JScrollPane sourceTextAreaContainer;
    private JPanel resultContainer;
    private JPanel body;
    private JTextPane footer;
    private JPasswordField passwordField;
    private JPanel footerContainer;
    private JasyptFactory jasyptFactory;

    public void errorDialog(String errkey) {
        errorDialog(errkey, null);

    }

    public void errorDialog(String errkey, Throwable throwable) {
//        JDialog dialog = new JDialog(frame, "错误信息");
//        dialog.setLocationRelativeTo(frame);
//        dialog.setPreferredSize(new Dimension(100, 100));
//        JTextPane jTextPane = new JTextPane();
//
//        jTextPane.setText(msg);
//        dialog.add(jTextPane);
//        dialog.setVisible(true);
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/err");
        String msg = bundle.getString(errkey);
        JOptionPane.showInternalMessageDialog(root, msg);
    }

    public MainFrame() {

        sourceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EncryptedData encryptedData = new EncryptedData();
                if (!loadDataAndValidate(encryptedData)) {
                    return;
                }
                String password = encryptedData.getPasswordField();
                String resource = encryptedData.getSourceTextArea();
                StringEncryptor stringEncryptor = jasyptFactory.defaultEncryptor(password);
                //System.out.println("密码是:" + password);
                //System.out.println("要被加密的资源:" + resource);
                String encrypt = stringEncryptor.encrypt(resource);

                encryptedData.setResultTextArea(encrypt);
                //显示内容
                setData(encryptedData);


            }
        });


        footer.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(event.getEventType())) {
                    try {
                        URL url = event.getURL();
                        Desktop.getDesktop().browse(url.toURI());
                    } catch (Exception e) {
                        errorDialog(Constants.URL_CANT_OPEN, e);
                    }
                }
            }
        });
        passwordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox source = (JCheckBox) e.getSource();
                EncryptedData encryptedData = new EncryptedData();
                getData(encryptedData);
                if (source.isSelected()) {
                    //如果被选中了 要将密码显示
                    passwordField.setEchoChar((char) 0);
                } else {
                    //取消的话 隐鲹
                    passwordField.setEchoChar('*');


                }
            }
        });
    }

    private boolean loadDataAndValidate(EncryptedData encryptedData) {
        getData(encryptedData);
        if (encryptedData.getPasswordField() == null || encryptedData.getPasswordField().trim().equals("")) {
            errorDialog(Constants.PASSWORD_IS_EMPTY);
            return false;
        }
        if (encryptedData.getSourceTextArea() == null || encryptedData.getSourceTextArea().trim().equals("")) {
            errorDialog(Constants.SOURCE_IS_EMPTY);
            return false;
        }
        return true;
    }

    public void init(JasyptFactory jasyptFactory, GuiConfig guiConfig) {
        this.jasyptFactory = jasyptFactory;
        frame = new JFrame(GuiConfig.PROGRAM_TITLE);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //不允许拉伸
        frame.setResizable(false);
        //宽高
        frame.setSize(guiConfig.getWidth(), guiConfig.getHeight());
        frame.setLocationRelativeTo(null);
        //初始化图标
        initIcon();
        //初始化系统托盘
        initTray();
        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private void initTray() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 判断系统是否支持系统托盘, 若不支持， 直接退出
                if (!SystemTray.isSupported()) {
                    System.exit(0);
                    return;
                } else {
                    SystemTray systemTray = SystemTray.getSystemTray();

                    if (systemTray.getTrayIcons().length > 0) {
                        return;
                    }
                    URL url = MainFrame.class.getClassLoader().getResource("img/icon.png");
                    // 创建弹出菜单
                    PopupMenu popup = new PopupMenu();

                    //退出程序选项
                    MenuItem exitItem = new MenuItem("exit");
                    exitItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (JOptionPane.showConfirmDialog(null, "确定退出小工具?") == 0) {
                                System.exit(0);
                            }
                        }
                    });
                    popup.add(exitItem);
                    TrayIcon trayIcon = new TrayIcon(new ImageIcon(url).getImage(), "密码加密工具", popup);
                    try {
                        trayIcon.setImageAutoSize(true);
                        systemTray.add(trayIcon);

                        trayIcon.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                //如果是右键事件
                                if (SwingUtilities.isRightMouseButton(e)) {
                                    //System.out.println("右键菜单");

                                } else {
                                    frame.setVisible(true);
                                }
                            }

                        });
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                    frame.dispose();
                }
            }
        });
    }

    private void initIcon() {
        URL url = MainFrame.class.getClassLoader().getResource("img/icon.png");
        frame.setIconImage(new ImageIcon(url).getImage());
    }


    public void setData(EncryptedData data) {
        passwordField.setText(data.getPasswordField());
        sourceTextArea.setText(data.getSourceTextArea());
        resultTextArea.setText(data.getResultTextArea());
    }

    public void getData(EncryptedData data) {
        data.setPasswordField(passwordField.getText());
        data.setSourceTextArea(sourceTextArea.getText());
        data.setResultTextArea(resultTextArea.getText());
    }

    public boolean isModified(EncryptedData data) {
        if (passwordField.getText() != null ? !passwordField.getText().equals(data.getPasswordField()) : data.getPasswordField() != null) {
            return true;
        }
        if (sourceTextArea.getText() != null ? !sourceTextArea.getText().equals(data.getSourceTextArea()) : data.getSourceTextArea() != null) {
            return true;
        }
        if (resultTextArea.getText() != null ? !resultTextArea.getText().equals(data.getResultTextArea()) : data.getResultTextArea() != null) {
            return true;
        }
        return false;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new BorderLayout(0, 0));
        root.setBackground(new Color(-660010));
        Font rootFont = this.$$$getFont$$$("Consolas", Font.BOLD | Font.ITALIC, 72, root.getFont());
        if (rootFont != null) root.setFont(rootFont);
        root.setMaximumSize(new Dimension(800, 550));
        root.setMinimumSize(new Dimension(800, 550));
        root.setPreferredSize(new Dimension(800, 550));
        root.setRequestFocusEnabled(true);
        root.putClientProperty("html.disable", Boolean.FALSE);
        body = new JPanel();
        body.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, 0));
        root.add(body, BorderLayout.NORTH);
        passwordContainer = new JPanel();
        passwordContainer.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), 0, 0));
        passwordContainer.setBackground(new Color(-396057));
        passwordContainer.setEnabled(true);
        body.add(passwordContainer, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 150), new Dimension(-1, 150), 0, false));
        passwordCheckboxContainer = new JPanel();
        passwordCheckboxContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 0, -1));
        passwordCheckboxContainer.setBackground(new Color(-396057));
        passwordContainer.add(passwordCheckboxContainer, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(130, -1), new Dimension(130, -1), 0, false));
        passwordCheckBox = new JCheckBox();
        Font passwordCheckBoxFont = this.$$$getFont$$$(null, -1, 12, passwordCheckBox.getFont());
        if (passwordCheckBoxFont != null) passwordCheckBox.setFont(passwordCheckBoxFont);
        passwordCheckBox.setText("是否显示密码");
        passwordCheckboxContainer.add(passwordCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), null, 0, false));
        passwordTextFieldContainer = new JPanel();
        passwordTextFieldContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        passwordTextFieldContainer.setBackground(new Color(-396057));
        Font passwordTextFieldContainerFont = this.$$$getFont$$$(null, -1, 18, passwordTextFieldContainer.getFont());
        if (passwordTextFieldContainerFont != null) passwordTextFieldContainer.setFont(passwordTextFieldContainerFont);
        passwordContainer.add(passwordTextFieldContainer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(500, -1), new Dimension(500, -1), 0, false));
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        Font passwordFieldFont = this.$$$getFont$$$(null, -1, 18, passwordField.getFont());
        if (passwordFieldFont != null) passwordField.setFont(passwordFieldFont);
        passwordTextFieldContainer.add(passwordField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordLabelContainer = new JPanel();
        passwordLabelContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        passwordLabelContainer.setBackground(new Color(-396057));
        passwordContainer.add(passwordLabelContainer, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(130, -1), new Dimension(130, -1), 0, false));
        passwordLabel = new JLabel();
        Font passwordLabelFont = this.$$$getFont$$$(null, -1, 18, passwordLabel.getFont());
        if (passwordLabelFont != null) passwordLabel.setFont(passwordLabelFont);
        passwordLabel.setHorizontalAlignment(4);
        passwordLabel.setText("密码:");
        passwordLabelContainer.add(passwordLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), new Dimension(130, -1), 0, false));
        sourceContainer = new JPanel();
        sourceContainer.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), 0, 0));
        sourceContainer.setBackground(new Color(-396057));
        body.add(sourceContainer, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 150), new Dimension(-1, 150), 0, false));
        sourceLabelContainer = new JPanel();
        sourceLabelContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        sourceLabelContainer.setBackground(new Color(-396057));
        sourceContainer.add(sourceLabelContainer, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(130, -1), new Dimension(130, -1), 0, false));
        sourceLabel = new JLabel();
        Font sourceLabelFont = this.$$$getFont$$$(null, -1, 18, sourceLabel.getFont());
        if (sourceLabelFont != null) sourceLabel.setFont(sourceLabelFont);
        sourceLabel.setHorizontalAlignment(4);
        sourceLabel.setHorizontalTextPosition(10);
        sourceLabel.setText("待加密资源:");
        sourceLabelContainer.add(sourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), new Dimension(130, -1), 0, false));
        sourceButtonContainer = new JPanel();
        sourceButtonContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 0, -1));
        sourceButtonContainer.setBackground(new Color(-396057));
        sourceContainer.add(sourceButtonContainer, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(130, -1), new Dimension(130, -1), 0, false));
        sourceButton = new JButton();
        sourceButton.setBackground(new Color(-11013552));
        sourceButton.setHorizontalAlignment(0);
        sourceButton.setText("加密");
        sourceButtonContainer.add(sourceButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), new Dimension(100, -1), 0, false));
        sourceTextAreaContainer = new JScrollPane();
        sourceContainer.add(sourceTextAreaContainer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        sourceTextArea = new JTextArea();
        Font sourceTextAreaFont = this.$$$getFont$$$(null, -1, 18, sourceTextArea.getFont());
        if (sourceTextAreaFont != null) sourceTextArea.setFont(sourceTextAreaFont);
        sourceTextArea.setLineWrap(true);
        sourceTextAreaContainer.setViewportView(sourceTextArea);
        resultContainer = new JPanel();
        resultContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        resultContainer.setForeground(new Color(-1));
        resultContainer.putClientProperty("html.disable", Boolean.TRUE);
        body.add(resultContainer, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 150), new Dimension(-1, 150), 0, false));
        resultContainer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-9309635)), "结果", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        resultTextArea = new JTextArea();
        resultTextArea.setColumns(80);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setText("");
        resultContainer.add(resultTextArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        footerContainer = new JPanel();
        footerContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        footerContainer.setEnabled(true);
        body.add(footerContainer, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 150), new Dimension(-1, 150), new Dimension(-1, 150), 0, true));
        footer = new JTextPane();
        footer.setBackground(new Color(-855310));
        footer.setContentType("text/html");
        footer.setEditable(false);
        footer.setEnabled(true);
        Font footerFont = this.$$$getFont$$$(null, Font.BOLD, 12, footer.getFont());
        if (footerFont != null) footer.setFont(footerFont);
        footer.setText("<html>\n  <head>\n    \n  </head>\n  <body>\n    <div align=\"center\" style=\"margin-top: 0\">\n      &#35813;&#24037;&#20855;&#26088;&#22312;&#25552;&#20379;&#19968;&#20010;&#31616;&#21333;&#30340;gui&#25805;&#20316;jasypt&#21152;&#23494;&#36164;&#28304;.\n    </div>\n    <div align=\"center\">\n      &#22914;&#26524;&#21457;&#29616;bug,&#27426;&#36814;&#20132;&#27969;: <a href=\"https://github.com/huyoufu/jasypt-gui\">&#32852;&#31995;&#25105;- \n      github.com/huyoufu/jasypt-gui</a>\n    </div>\n  </body>\n</html>\n");
        footer.putClientProperty("charset", "UTF-8");
        footer.putClientProperty("html.disable", Boolean.FALSE);
        footerContainer.add(footer, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 100), new Dimension(-1, 100), new Dimension(-1, 100), 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
