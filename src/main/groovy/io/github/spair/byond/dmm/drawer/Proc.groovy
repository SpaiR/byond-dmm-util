package io.github.spair.byond.dmm.drawer

@FunctionalInterface
interface Proc<T, R> {
    R call(T o)
}
