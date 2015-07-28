package com.github.asufana;

import java.io.*;

import org.junit.*;

import com.squareup.javapoet.*;

public class VOGeneratorTest {
    
    @Test
    public void testGenerate() throws Exception {
        final JavaFile javaFile = VOGenerator.generate("sample.vo", T.voDef);
        javaFile.writeTo(new File("dist"));
    }
    
}
