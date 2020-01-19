package com.hanzl.chat_obj;

/*
 * QQServer，服务器端代码
 */
import java.net.*;
//import java.util.*;

public class QQServer {
    public static void main(String[] args) {
        try {
            // 声明存放所有人的Socket的集合
            NetMap<String, Neter> hm = new NetMap<String, Neter>();
            // 服务器在8000端口监听
            ServerSocket ss = new ServerSocket(8000);

            while(true) {
                System.out.println("服务器正在8000端口监听......");
                Socket s = ss.accept();
                Neter net = new Neter(s);

                MyService t = new MyService();
                t.setNeter(net);
                t.setHashMap(hm);
                t.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

class MyService extends Thread {
    private Neter net;
    // 接收HashMap的引用
    private NetMap<String, Neter> hm;

    public void setHashMap(NetMap hm) {
        this.hm = hm;
    }
    public void setNeter(Neter net) {
        this.net = net;
    }

    public void run() {
        try {
            // 接收用户名和密码
            String uandp = net.receive();

            // 拆分用户名和密码
            String u = uandp.split("%")[0];
            String p = uandp.split("%")[1];

            // 防止同一用户重复登录
            if(hm.containsKey(u)) {
                net.send("REPEAT");
                return;
            }

            // 到数据库中验证用户身份
            SQLer sql = new SQLer();
            if(sql.vali(u, p)) {
                // 发送正确信息到客户端
                net.send("OK");

                // 将本人的名字发送给其他用户
                hm.send("add1%" + u);

                // 将其他人的名字发送给自己
                for(String tu : hm.keySet()) {
                    net.send("add2%" + tu);
                }

                // 将本人的用户名和Socket存入HashMap
                hm.put(u, net);

                // 不断接收客户端发送过来的信息
                while(true) {
                    String message = net.receive();

                    if(message.equals("{exit}")) {
                        // 将该用户从HashMap中删除
                        hm.remove(u);
                        // 通知所有人，本用户退出
                        hm.send("exit%" + u);
                        // 打印最新的在线用户列表
                        System.out.println("-------- 最新在线用户列表 开始 --------");
                        for (String tu : hm.keySet()) {
                            System.out.println(tu);
                        }
                        return ;
                    }

                    // 转发信息
                    String to = message.split("%")[0];
                    String mess = message.split("%")[1];
                    hm.send(to, "mess%" + u + "%" + mess);
                }
            } else {
                // 发送错误信息到客户端
                net.send("ERROR");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
