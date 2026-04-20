package annotations

/**
 * Marks a function as "extractable" using the provided [regex].
 *
 * Retention is [AnnotationRetention.SOURCE] because this annotation is meant to be consumed by a
 * compile-time tool (e.g., an annotation processor / KSP) rather than at runtime via reflection.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Extract(val regex: String)
