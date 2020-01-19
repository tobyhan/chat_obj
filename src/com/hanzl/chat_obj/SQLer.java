package com.hanzl.chat_obj;

import java.io.*;
import java.sql.*;

public class SQLer {
    public boolean vali(String user, String pass) {
        boolean b = false;
        try {
            File f = new File("D:/SourceCode/JavaDemo/j2se/src/chat_obj/SQL.ini");

            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            String driver = br.readLine();
            String url = br.readLine();
            String u = br.readLine();
            String p = br.readLine();

            Class.forName(driver);
            Connection cn = DriverManager.getConnection(url, u, p);
            PreparedStatement ps = cn.prepareStatement("select * from user where username=? and password=?");
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            b = rs.next();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return b;
    }
}
