package com.lucky.simplebutterknife.compiler;


import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.lucky.simplebutterknife.annotations.SimpleBindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

// 对注解的元素进行分析和生成源码(就是Java文件)
// @AutoService(Processor.class) 注解是利用了 Google 的 AutoService 为注解处理器自动生成 metadata 文件并将注解处理器jar文件加入构建路径，
// 这样也就不需要再手动创建并更新 META-INF/services/javax.annotation.processing.Processor 文件了。
// 覆写 getSupportedSourceVersion() 方法指定可以支持最新的 Java 版本，
// 覆写 getSupportedAnnotationTypes() 方法指定该注解处理器用于处理哪些注解（我们这里只处理 @BindView 注解）。
// 而检索注解元素并生成代码的是 process 方法的实现:
@AutoService(Processor.class)
public class SimpleButterKnifeProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(SimpleBindView.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "处理程序>>>>>>>>>>>>>>>>>>>>>>>");
        Map<TypeElement, BindingSet> bindingMap = new LinkedHashMap<>();
        // 查找所有被@BindView元素注解的程序元素（Element）
        for (Element element : roundEnv.getElementsAnnotatedWith(SimpleBindView.class)) {
            // 注解元素的外侧元素， 即View的所在Activity类
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            // 注解的value值，即View的id
            int id = element.getAnnotation(SimpleBindView.class).value();
            // 注解元素的名字，即View的变量名
            Name simpleName = element.getSimpleName();
            String name = simpleName.toString();
            // 注解元素的类型，即View的类型
            TypeMirror elementType = element.asType();
            TypeName type = TypeName.get(elementType);
            // 将这些信息存到Activity对应的View绑定中
            BindingSet bindingSet = bindingMap.get(enclosingElement);
            if (bindingSet == null) {
                bindingSet = new BindingSet();
                TypeMirror typeMirror = enclosingElement.asType();
                // Activity对应的名称
                TypeName targetType = TypeName.get(typeMirror);
                String packageName = MoreElements.getPackage(enclosingElement)
                        .getQualifiedName().toString();
                String className = enclosingElement.getQualifiedName().toString()
                        .substring(packageName.length()+1)
                        .replace(".", "$");
                ClassName bindingClassName = ClassName.get(packageName,
                        className+"_ViewBinding");
                bindingSet.targetTypeName = targetType;
                bindingSet.bindingClassName = bindingClassName;
                bindingMap.put(enclosingElement, bindingSet);
            }
            if (bindingSet.viewBindings == null){
                bindingSet.viewBindings = new ArrayList<>();
            }
            ViewBinding viewBinding = new ViewBinding();
            viewBinding.type = type;
            viewBinding.id = id;
            viewBinding.name = name;
            bindingSet.viewBindings.add(viewBinding);
        }
        // 利用JavaPoet来生成Java文件
        for (Map.Entry<TypeElement, BindingSet> entry: bindingMap.entrySet()){
             TypeElement typeElement = entry.getKey();
             BindingSet binding = entry.getValue();

             TypeName targetTypeName = binding.targetTypeName;
             ClassName bindingClassName = binding.bindingClassName;
             List<ViewBinding> viewBindings = binding.viewBindings;

             //binding类
            TypeSpec.Builder viewBindingBuilder = TypeSpec
                    .classBuilder(bindingClassName.simpleName())
                    .addModifiers(Modifier.PUBLIC);
            //public的target字段用来保存Activity引用
            viewBindingBuilder.addField(targetTypeName, "target",
                    Modifier.PUBLIC);
            //构造器
            MethodSpec.Builder activityViewBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(targetTypeName, "target");
            activityViewBuilder.addStatement("this(target, target.getWindow().getDecorView())");
            viewBindingBuilder.addMethod(activityViewBuilder.build());

            //第二个构造器
            MethodSpec.Builder viewBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(targetTypeName, "target")
                    .addParameter(ClassName.get("android.view", "View"), "source");
            viewBuilder.addStatement("this.target = target");
            viewBuilder.addCode("\n");
            for (ViewBinding viewBinding: viewBindings){
                CodeBlock.Builder builder = CodeBlock.builder()
                        .add("target.$L=", viewBinding.name);
                builder.add("($T)", viewBinding.type);
                builder.add("source.findViewById($L)", CodeBlock.of("$L", viewBinding.id));
                viewBuilder.addStatement("$L", builder.build());
            }

            viewBindingBuilder.addMethod(viewBuilder.build());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "packageName "+bindingClassName.packageName());
            // 输出Java文件
            JavaFile javaFile = JavaFile.builder(bindingClassName.packageName(),
                    viewBindingBuilder.build()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            }catch (IOException e){
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        return true;
    }


    class BindingSet {
        TypeName targetTypeName;
        ClassName bindingClassName;
        List<ViewBinding> viewBindings;
    }

    class ViewBinding {
        TypeName type;
        int id;
        String name;
    }

}
