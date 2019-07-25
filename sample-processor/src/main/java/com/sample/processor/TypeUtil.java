package com.sample.processor;

import com.squareup.javapoet.ClassName;

public class TypeUtil {
    public static final ClassName FINDER = ClassName.get("com.sample.api.finder", "Finder");
    public static final ClassName INJECTOR = ClassName.get("com.sample.api", "Injector");
}
