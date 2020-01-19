package com.hanzl.chat_obj;

/*
 * QQMain，主界面
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.net.*;
//import java.io.*;

public class QQMain extends JFrame implements ActionListener, Runnable, WindowListener {
    private Neter net;
    private Recorder rcd = new Recorder();

    public void setNeter(Neter net) {
        this.net = net;
        // 启动接收线程
        Thread t = new Thread(this);
        t.start();
    }

    JTextField txtMess = new JTextField();
    JComboBox cmbUser = new JComboBox();
    JTextArea txtContent = new JTextArea();

    QQMain() {
        this.setSize(300, 400);

        // new组件
        JButton btnSend = new JButton("发送");

        // 设置消息历史不可编辑
        txtContent.setEditable(false);
        JScrollPane spContent = new JScrollPane(txtContent);

        // 注册事件监听
        btnSend.addActionListener(this);
        this.addWindowListener(this);

        // 布置小面板
        JPanel panSmall = new JPanel();
        panSmall.setLayout(new GridLayout(1, 2));
        panSmall.add(cmbUser);
        panSmall.add(btnSend);

        // 布置大面板
        JPanel panBig = new JPanel();
        panBig.setLayout(new GridLayout(2, 1));
        panBig.add(txtMess);
        panBig.add(panSmall);

        // 布置窗体
        this.setLayout(new BorderLayout());
        this.add(panBig, BorderLayout.NORTH);
        this.add(spContent, BorderLayout.CENTER);

//		// 读聊天记录
//		rcd.read();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // 非法输入内容拦截
        if(cmbUser.getSelectedItem() == null) return;
        if(txtMess.getText().equals("")) return;

        // txtMess -------> txtContent
        txtContent.append("[我 -> " + cmbUser.getSelectedItem() + "]:" + txtMess.getText() + "\n");

        // 将txtMess的内容存入聊天记录文件
        rcd.write("[我 -> " + cmbUser.getSelectedItem() + "]:" + txtMess.getText());

        // 发送信息到服务器
        net.send(cmbUser.getSelectedItem() + "%" + txtMess.getText());

        // 清除txtMess中的内容
        txtMess.setText("");
    }

    // 接收线程
    public void run() {
        try {
            while (true) {
                String message = net.receive();
                String type = message.split("%")[0];
                String user = message.split("%")[1];
                String mess = "";

                if (type.equals("add1")) {
                    cmbUser.addItem(user);
                    txtContent.append("[系统提示]:" + user + "上线啦。\n");
                }
                if (type.equals("add2")) {
                    cmbUser.addItem(user);
                }
                if (type.equals("exit")) {
                    cmbUser.removeItem(user);
                    txtContent.append("[系统提示]:" + user + "已下线。\n");
                }
                if (type.equals("mess")) {
                    mess = message.split("%")[2];
                    txtContent.append("[" + user + " -> 我]:" + mess + "\n");
                    System.out.println("[" + user + " -> 我]:" + mess + "\n");
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        // 发送退出的消息
        net.send("{exit}", true);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }
}
