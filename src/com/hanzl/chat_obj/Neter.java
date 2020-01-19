package com.hanzl.chat_obj;

import java.io.*;
import java.net.*;

public class Neter {
    private PrintWriter pw;
    private BufferedReader br;

    public Neter(Socket s) {
        try {
            InputStream is = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            OutputStream os = s.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            pw = new PrintWriter(osw, true);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void send(String message) {
        try {
            pw.println(message);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void send(String message, boolean flg) {
        try {
            pw.println(message);

            // 正常退出
            System.exit(0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public String receive() {
        String message = "";
        try {
            message = br.readLine();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return message;
    }
}
