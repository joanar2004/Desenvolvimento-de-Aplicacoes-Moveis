package app

import annotations.Greeting

/**
 * Simple class used to demonstrate the annotation processor.
 *
 * Methods annotated with [Greeting] will have corresponding methods generated in `MyClassWrapper`
 * that print a message before delegating to the original implementation.
 */
open class MyClass {

    /** Prints a log line; a wrapper will print the greeting message before calling this. */
    @Greeting("Hello from MyClass!")
    open fun sayHello() {
        println("Executing sayHello method")
    }

    /** Prints a log line; a wrapper will print the greeting message before calling this. */
    @Greeting("Welcome to the compute function!")
    open fun compute() {
        println("Computing something important...")
    }
}
