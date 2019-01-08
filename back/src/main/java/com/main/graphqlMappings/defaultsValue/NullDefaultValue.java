package com.main.graphqlMappings.defaultsValue;

import java.util.function.Supplier;

/**
 * null value generator
 */
public class NullDefaultValue implements Supplier<Object> {
    @Override
    public Object get() {
        return null;
    }
}
