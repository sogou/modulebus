package com.sogou.modulebus.routerbus;

public interface RouterCallback {

    void result(@ResultCode int resultCode, RouterBuild build);

}
