/*
 * Copyright (c) 2020 Sogou, Inc.
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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