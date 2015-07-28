package com.github.asufana.vo;

import lombok.*;
import lombok.experimental.*;

@Getter
@Accessors(fluent = true)
public class VODefinition {
    private final String title;
    private final String className;
    private final String columnName;
    private final String type;
    private final boolean nullable;
    private final Integer length;
    
    public VODefinition(final String title, final String className, final String type) {
        this(title, className, null, type, false, 255);
    }
    
    public VODefinition(final String title,
            final String className,
            final String columnName,
            final String type,
            final boolean nullable,
            final Integer length) {
        this.title = title;
        this.className = className;
        this.columnName = columnName;
        this.type = type;
        this.nullable = nullable;
        this.length = length;
    }
}
