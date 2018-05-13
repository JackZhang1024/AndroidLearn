package com.lucky.simplebutterknife_annotations;

import android.support.annotation.IdRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// RetentionPolicy.class 表示BindView这个注解只在编译时使用
// ElementType.FIELD 表示BindView这个注解使用在属性元素上

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface SimpleBindView {
    @IdRes int value();
}
