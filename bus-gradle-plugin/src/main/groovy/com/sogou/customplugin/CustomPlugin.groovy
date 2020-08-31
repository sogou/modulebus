package com.sogou.customplugin

import com.android.build.gradle.AppExtension
import com.sogou.customplugin.extension.DefaultRegistryHelper
import com.sogou.customplugin.extension.PluginConfig
import com.sogou.customplugin.transform.SogouTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin implements Plugin<Project>{

    public static final String EXT_NAME = 'moduleBusConfig'

    @Override
    void apply(Project project) {

        //处理导入
        def isApp = ProjectModuleManager.manageModule(project)

        //处理依赖及编译时参数注入
        ProjectManager.managerDependencies(project)

        if (isApp){
            project.extensions.create(EXT_NAME, PluginConfig)
            AppExtension appExtension = project.extensions.getByType(AppExtension)
            SogouTransform sogouTransform = new SogouTransform(project)
            appExtension.registerTransform(sogouTransform)
            project.afterEvaluate {
                PluginConfig config = project.extensions.findByName(EXT_NAME) as PluginConfig
                config.init()
                DefaultRegistryHelper.addDefaultRegistry(config.infoList)
                sogouTransform.infoList = config.infoList
            }
        }
    }
}