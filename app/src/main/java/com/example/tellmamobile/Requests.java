package com.example.tellmamobile;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Requests {             //Singleton RequestQueue
    private static Requests instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private Requests(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

    }

    public static synchronized Requests getInstance(Context context) {
        if (instance == null) {
            instance = new Requests(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {

            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}

