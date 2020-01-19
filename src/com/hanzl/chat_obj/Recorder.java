package com.hanzl.chat_obj;

import java.io.*;

public class Recorder {
    private File f;

    public Recorder() {
        f = new File("D:/SourceCode/IdeaProjects/chat_obj/src/com/hanzl/chat_obj/History.log");
    }
    public Recorder(String url) {
        f = new File(url);
    }

    public void write(String message) {
        try {
            FileWriter fw = new FileWriter(f, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(message);

            pw.close();
            fw.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public String read() {
        String mess = "";
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            while(br.ready()) {
                mess += br.readLine()+"/n";
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return mess;
    }
}
