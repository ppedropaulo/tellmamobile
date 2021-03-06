package com.example.tellmamobile;

import android.content.Context;

public class UserSession { //Singleton para acesso ao usuário

    private static UserSession instance;
    private String username;
    private String id;
    private static Context ctx;


    private UserSession(Context context){
        ctx = context;
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public static synchronized UserSession setInstance(Context context,String name, String id) {
        if (instance == null) {
            instance = new UserSession(context);
            instance.id= id;
            instance.username= name;
        }
        return instance;
    }

    public static synchronized void eraseInstance(Context context){
        instance = null;
    }

    public String getUsername(){
        return instance.username;
    }

    public String getId(){
        return instance.id;
    }
}





