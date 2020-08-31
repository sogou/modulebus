package com.sogou.annotationprocessor;

import com.google.auto.service.AutoService;
import com.sogou.annotation.RouterSchema;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedOptions("moduleName")
public class AnnotationProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager messager;
    private Map<String, String> options;
    private String mModuleName;

    /**
     * @param processingEnvironment
     * 初始化注解环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        options = processingEnvironment.getOptions();
        mModuleName = options.get("moduleName");
        messager.printMessage(Diagnostic.Kind.NOTE, "option "+options.size());
        note();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> types = new HashSet<>();
        types.add(RouterSchema.class.getCanonicalName());

        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(RouterSchema.class);
        Map<String, String> entity = new HashMap<>();

        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            String[] annotationValues = typeElement.getAnnotation(RouterSchema.class).value();
            String qualifiedName = typeElement.getQualifiedName().toString();
            for (String annotationValue : annotationValues) {
                entity.put(annotationValue, qualifiedName);
                messager.printMessage(Diagnostic.Kind.NOTE, String.format("router >>> %1s-%2s", annotationValue, qualifiedName));
            }
        }

        if (entity.size() > 0){
            String fileName = "RouterRegistry_"+mModuleName;
            Writer writer = null;
            try {
                JavaFileObject sourceFile = mFiler.createSourceFile("com.sogou.modulebus.routerbus." + fileName);
                writer = sourceFile.openWriter();
                writer.write("package com.sogou.modulebus.routerbus;\n" +
                        "\n" +
                        "class " + fileName + " implements IRouterRegistry {\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void register(){\n");
                Iterator<String> iterator = entity.keySet().iterator();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    String value = entity.get(key);
                    writer.write("      RouterGlobalSetting.getInstance().putItem(\""+ key +"\", \""+ value +"\");\n");
                }

                writer.write("  }\n}");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (writer != null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private void note(){
        Set<String> keySet = options.keySet();
        for (String key : keySet) {
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("option-%1s:%2s", key, options.get(key)));
        }
    }
}
