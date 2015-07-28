package com.github.asufana;

import javax.lang.model.element.*;

import org.apache.commons.lang3.*;

import com.github.asufana.vo.*;
import com.squareup.javapoet.*;
import com.squareup.javapoet.AnnotationSpec.Builder;

public class VOGenerator {
    //http://qiita.com/opengl-8080/items/87f850c6b2467ad4da32
    public static JavaFile generate(final String packageName, final VODefinition def) {
        final TypeSpec typeSpec = TypeSpec.classBuilder(def.className())
                                          .superclass(com.github.asufana.ddd.vo.AbstractValueObject.class)
                                          .addJavadoc(def.title())
                                          .addAnnotation(javax.persistence.Embeddable.class)
                                          .addAnnotation(lombok.Getter.class)
                                          .addAnnotation(AnnotationSpec.builder(lombok.experimental.Accessors.class)
                                                                       .addMember("fluent",
                                                                                  "$L",
                                                                                  "true")
                                                                       .build())
                                          .addAnnotation(AnnotationSpec.builder(javax.annotation.Generated.class)
                                                                       .addMember("value",
                                                                                  "{$S}",
                                                                                  "com.github.asufana.modelbuilder")
                                                                       .build())
                                          .addModifiers(Modifier.PUBLIC)
                                          .addField(buildField(def))
                                          .addMethod(buildConstructor())
                                          .build();
        return JavaFile.builder(packageName, typeSpec).build();
    }
    
    private static MethodSpec buildConstructor() {
        return MethodSpec.constructorBuilder()
                         .addModifiers(Modifier.PUBLIC)
                         .addParameter(String.class, "value", Modifier.FINAL)
                         .addStatement("this.$N = $N", "value", "value")
                         .addStatement("validate()")
                         .build();
    }
    
    private static FieldSpec buildField(final VODefinition def) {
        return FieldSpec.builder(String.class, "value")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .addAnnotation(buildColumnAnnotation(def))
                        .build();
    }
    
    private static AnnotationSpec buildColumnAnnotation(final VODefinition def) {
        final Builder builder = AnnotationSpec.builder(javax.persistence.Column.class)
                                              .addMember("name", "$S", columnName(def));
        if (StringUtils.isNotEmpty(columnNullable(def))) {
            builder.addMember("nullable", columnNullable(def));
        }
        if (StringUtils.isNotEmpty(columnLength(def))) {
            builder.addMember("length", columnLength(def));
        }
        return builder.build();
    }
    
    private static String columnName(final VODefinition def) {
        return StringUtils.isNotEmpty(def.columnName())
                ? def.columnName()
                : toSnakeName(def.className());
    }
    
    private static String toSnakeName(final String className) {
        return className.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                        .replaceAll("([a-z])([A-Z])", "$1_$2")
                        .toLowerCase();
    }
    
    private static String columnNullable(final VODefinition def) {
        if (def.type().toLowerCase().equals("string") == false) {
            return "";
        }
        return def.nullable()
                ? "true"
                : "false";
    }
    
    private static String columnLength(final VODefinition def) {
        if (def.type().toLowerCase().equals("string") == false) {
            return "";
        }
        return def.length().toString();
    }
    
}
