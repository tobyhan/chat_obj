package com.hanzl.chat_obj;

import java.util.*;

public class NetMap<K, V> extends HashMap<String, Neter> {
    public void send(String message) {
        try {
            for(Neter n : this.values()) {
                n.send(message);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void send(String user, String message) {
        try {
            Neter n = this.get(user);
            n.send(message);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
