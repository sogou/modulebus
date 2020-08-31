package com.sogou.customplugin.extension

import com.sogou.customplugin.autoexport.Helpers

class PluginConfig{

    public List<Map<String, Object>> exportInfoList = []

    List<ExportInfo> infoList = new ArrayList<>()

    void init(){
        exportInfoList.each {
            map ->
                ExportInfo exportInfo = new ExportInfo()
                exportInfo.scanInterface = map.get("scanInterface")
                exportInfo.destClass = map.get("destClass")

                String destMethod = map.get("destMethod")
                if (destMethod){
                    exportInfo.destMethod = destMethod
                }
                exportInfo.methodCall = map.get("methodCall")
                exportInfo.format()
                Helpers.log(exportInfo.toString())
                if (exportInfo.isValidate()){
                    infoList.add(exportInfo)
                }
        }

        Helpers.infoList = infoList
    }
}