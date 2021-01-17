package com.example.myapplication.Tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.myapplication.DTO.MemberDTO;
import com.example.myapplication.HTTPClient;
import com.google.gson.Gson;

import java.util.Map;

public class DeviceTask extends AsyncTask<Map<String, String>, Integer, String> {

        public static String ip = "dodam9698.cafe24.com";
//    public static String ip = "172.30.1.13/Dodam";
//    public static String ip = "192.168.0.156/Dodam";

    private Handler handler;
    private Exception exception;

    public DeviceTask(Handler handler) {

        this.handler = handler;

    }

    @Override
    protected String doInBackground(Map<String, String>... maps) {
        try {
            HTTPClient.Builder http = new HTTPClient.Builder("POST", "http://" + ip + "/AndroidAddDevice");

            http.addAllParameters(maps[0]);

            HTTPClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            String body = post.getBody();

            if (body.equals("")) {
                throw new Exception();
            }

            return body;

        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {

        Message msg = handler.obtainMessage();
        if (exception != null) {

            msg.what = -1;
            msg.obj = exception;

        } else {

            msg.what = 0;

        }
        handler.sendMessage(msg);


    }
}
