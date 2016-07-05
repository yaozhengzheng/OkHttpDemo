package com.yao.feicui.okhttpdemo;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private ArrayAdapter<Contributor> mAdapter;

    private OkHttpClient mClient;

    private HttpLoggingInterceptor mInterceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview= (ListView) findViewById(R.id.listView);
        mAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        listview.setAdapter(mAdapter);

        mInterceptor=new HttpLoggingInterceptor();
        mInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //添加一个日志连接器
        mClient=new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .build();
        String owner = "square"; // 公司
        String repo = "retrofit";// 产品
        //构建请求
        Request request=new Request.Builder()
                .url("https://api.github.com/repos/"+owner+"/"+repo+"/contributors")
                .build();
        //请求异步
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String boday=response.body().string();
                Gson gson=new Gson();

                TypeToken<List<Contributor>>typeToken=new TypeToken<List<Contributor>>(){};

                final List<Contributor>contributors=gson.fromJson(boday,typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addAll(contributors);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
   /* class GitHubTask extends AsyncTask<Void,Void,Response>{

        @Override
        protected Response doInBackground(Void... params) {
            //构建请求
            Request request=new Request.Builder()
                    .url("https://api.github.com")
                    .build();
            //请求同步
            try {
                Response response=mClient.newCall(request).execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.isSuccessful()){
                ResponseBody body=response.body();
            }
        }
    }*/
}
