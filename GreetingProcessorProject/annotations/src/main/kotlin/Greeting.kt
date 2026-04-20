package annotations

/**
 * Marks a function to receive a greeting message generated/printed by a wrapper.
 *
 * This project uses an annotation processor to generate wrapper classes that print [message]
 * before delegating to the original function.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Greeting(val message: String)
