package com.sogou.modulebus.routerbus;

import java.util.Map;

public interface RouterFactory {

    void putDynamicRouter(Map<String, String> routerTable);
}
