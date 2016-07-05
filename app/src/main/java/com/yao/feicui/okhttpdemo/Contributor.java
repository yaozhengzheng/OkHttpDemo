package com.yao.feicui.okhttpdemo;

/**
 * Created by 16245 on 2016/07/05.
 */
public class Contributor {
    String login;
    int contributions;

    @Override
    public String toString() {
        return login+","+contributions;
    }
}
