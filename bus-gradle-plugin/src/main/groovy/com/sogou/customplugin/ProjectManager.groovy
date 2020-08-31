package com.sogou.customplugin

import org.gradle.api.Project

class ProjectManager {

    static final String APT_OPTION_NAME = "moduleName"

    static boolean managerDependencies(Project project){

        def android = project.extensions.findByName("android")
        if (android) {
            android.defaultConfig.javaCompileOptions.annotationProcessorOptions.includeCompileClasspath = true
            android.defaultConfig.javaCompileOptions.annotationProcessorOptions.argument(APT_OPTION_NAME, project.name)
            android.productFlavors.all {
                it.javaCompileOptions.annotationProcessorOptions.argument(APT_OPTION_NAME, project.name)
            }
        }
    }
}