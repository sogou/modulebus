package com.sogou.customplugin.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.sogou.customplugin.autoexport.Helpers
import com.sogou.customplugin.autoexport.InjectProcess
import com.sogou.customplugin.autoexport.ScanProcess
import com.sogou.customplugin.extension.ExportInfo
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class SogouTransform extends Transform{

    Project project

    List<ExportInfo> infoList = new ArrayList<>()

    SogouTransform(Project project){
        this.project = project
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation)

        long start = System.currentTimeMillis()
        boolean leftSlash = File.separator == '/'

        Helpers.reset()
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs()
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()

        for (TransformInput input : inputs){

            project.logger.error("Scan JarInput")
            for (JarInput jarInput : input.getJarInputs()){
//                project.logger.error("jarInput-"+jarInput)
                File src = jarInput.getFile()
                def jarPath = src.absolutePath
                File dest = outputProvider.getContentLocation(jarInput.name,
                        jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR)
                if (!leftSlash) {
                    jarPath = jarPath.replaceAll("\\\\", "/")
                }
                if (Helpers.shouldScan(jarPath)){
//                    project.logger.error("jarPath-"+jarPath)
//                    project.logger.error("destJarPath-"+dest.absolutePath)
                    ScanProcess.scanJarFile(src, dest)
                }else{
//                    project.logger.error("ignore:" + jarPath)
                }

                FileUtils.copyFile(src, dest)
            }


            project.logger.error("Scan DirectoryInput")
            for (DirectoryInput directoryInput : input.getDirectoryInputs()){
                File dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY)

                String root = directoryInput.file.absolutePath
                if (!root.endsWith(File.separator)){
                    root += File.separator
                }
                directoryInput.file.eachFileRecurse {

                    def path = it.absolutePath.replace(root, '')
                    if (it.isFile()) {
                        def entryName = path
                        if (!leftSlash) {
                            entryName = entryName.replaceAll("\\\\", "/")
                        }

//                        project.logger.error("directoryPath:" + it.absolutePath)
//                        project.logger.error("entryName:" + entryName)
                        ScanProcess.scanClassFile(entryName, it, new File(dest.absolutePath + File.separator + path))

                    }
                }

                FileUtils.copyDirectory(directoryInput.file, dest)
            }

        }

        project.logger.error("Inject Code")
        /*if (Helpers.destClassFileList.size() <= 0){
            project.logger.error("未找到编辑的class文件")
        }else{
            InjectProcess.injectCode()
        }*/

        Helpers.infoList.each {
            info ->
                if (info.destClassFiles.size() <= 0){
                    project.logger.error("未找到编辑的class文件:"+info.destClass)
                }else{
                    project.logger.error(info.toString())
                    InjectProcess.injectCode(info)
                }
        }

        project.logger.error("SogouTransform cost " + (System.currentTimeMillis() - start) +"ms")

    }


    /**
     * Returns the unique name of the transform.
     *
     * <p>This is associated with the type of work that the transform does. It does not have to be
     * unique per variant.
     */
    @Override
    String getName() {
        return "ModuleBusTransform"
    }

    /**
     * Returns the type(s) of data that is consumed by the Transform. This may be more than
     * one type.
     *
     * <strong>This must be of type {@link QualifiedContent.DefaultContentType}</strong>
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * Returns the scope(s) of the Transform. This indicates which scopes the transform consumes.
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * Returns whether the Transform can perform incremental work.
     *
     * <p>If it does, then the TransformInput may contain a list of changed/removed/added files, unless
     * something else triggers a non incremental run.
     */
    @Override
    boolean isIncremental() {
        return false
    }
}
