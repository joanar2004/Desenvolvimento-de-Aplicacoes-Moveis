package app

/**
 * Entry point for the demo application.
 *
 * `MyClassWrapper` is generated at compile-time by the annotation processor (module `processor`)
 * based on the `@Greeting` annotations declared on methods of [MyClass].
 */
fun main() {
    val myClass = MyClass()
    // Wrap the original instance to get the generated "print greeting then delegate" behavior.
    val wrappedMyClass = MyClassWrapper(myClass)
    wrappedMyClass.sayHello()
    wrappedMyClass.compute()
}
