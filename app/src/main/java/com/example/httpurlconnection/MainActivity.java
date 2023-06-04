package com.example.httpurlconnection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView tv_source;
    private EditText et_url;
    private String path;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_url = (EditText) findViewById(R.id.et_url);
        tv_source = (TextView) findViewById(R.id.tv_source);
    }

    private boolean check_input() {
        path = et_url.getText().toString().trim();
        if (path.isEmpty()) {
            Toast.makeText(MainActivity.this, "网址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    /**
     * GET
     *
     * @param view
     */
    public void GET(View view) {
        boolean is_check_input = this.check_input();
        if (!is_check_input) {
            return;
        }
        // 请求网络,子线程进行
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5 * 1000);
                    httpURLConnection.setReadTimeout(5 * 1000);
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        // 主线程更新ui
                        updateUi(sb.toString());
                    } else {
                        Log.d(TAG, "responseCode: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * POST
     *
     * @param view
     */
    public void POST(View view) {
        boolean is_check_input = this.check_input();
        if (!is_check_input) {
            return;
        }
        // 请求网络,子线程进行
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    // post请求
                    httpURLConnection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                    out.writeBytes("username=admin&password=123456");
                    httpURLConnection.setConnectTimeout(5 * 1000);
                    httpURLConnection.setReadTimeout(5 * 1000);
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        // 主线程更新ui
                        updateUi(sb.toString());
                    } else {
                        Log.d(TAG, "responseCode: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 主线程更新ui
     *
     * @param result 网络源码
     */
    private void updateUi(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_source.setText(result);
            }
        });
    }
}
