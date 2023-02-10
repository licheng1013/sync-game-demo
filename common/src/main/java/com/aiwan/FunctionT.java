package com.aiwan;

/**
 * @author lc
 * @since 2023/2/10
 */
@FunctionalInterface
public interface FunctionT<T> {
    void apply(T t);
}
