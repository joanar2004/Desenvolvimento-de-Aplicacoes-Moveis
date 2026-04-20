package processor

import annotations.Greeting
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * Annotation processor for [Greeting].
 *
 * For each class that contains one or more functions annotated with [Greeting], this processor
 * generates a `<OriginalClassName>Wrapper` class in the same package. Each generated wrapper
 * prints the greeting message and then forwards the call to the original instance.
 *
 * The generated sources are written into KAPT's output directory (`kapt.kotlin.generated`).
 */
@AutoService(Processor::class)
@SupportedAnnotationTypes("annotations.Greeting")
class GreetingProcessor : AbstractProcessor() {

    /** Use the latest source version supported by the current toolchain. */
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    /**
     * Called by the annotation processing infrastructure for each processing round.
     *
     * We group annotated methods by their enclosing class so we can generate a single wrapper per
     * original class.
     */
    override fun process(annotations: MutableSet<out TypeElement>,
                         roundEnv: RoundEnvironment): Boolean {
        val classMethodMap = mutableMapOf<TypeElement, MutableList<ExecutableElement>>()

        // Collect all methods annotated with @Greeting, grouping them by their enclosing class.
        for (element in roundEnv.getElementsAnnotatedWith(Greeting::class.java)) {
            if (element is ExecutableElement) {
                val enclosingClass = element.enclosingElement as TypeElement
                classMethodMap.computeIfAbsent(enclosingClass) { mutableListOf() }.add(element)
            }
        }

        // Generate one wrapper class per original class.
        for ((classElement, methods) in classMethodMap) {
            generateKotlinWrapperClass(classElement, methods)
        }
        return true
    }

    /**
     * Generates a KotlinPoet [FileSpec] for the wrapper class and writes it to the KAPT output dir.
     *
     * The wrapper has a single constructor parameter `original` which holds the instance being
     * wrapped. Each annotated method is generated with the same signature and delegates the call
     * after printing the configured greeting message.
     */
    private fun generateKotlinWrapperClass(classElement: TypeElement,
                                           methods: List<ExecutableElement>) {
        val packageName = processingEnv.elementUtils.getPackageOf(classElement).toString()
        val originalClassName = classElement.simpleName.toString()
        val wrapperClassName = "${originalClassName}Wrapper"

        val classBuilder = TypeSpec.classBuilder(wrapperClassName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("original", ClassName(packageName, originalClassName))
                    .build()
            )
            .addProperty(
                PropertySpec.builder("original", ClassName(packageName, originalClassName))
                    .initializer("original")
                    .build()
            )
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)

        for (method in methods) {
            val methodName = method.simpleName.toString()
            val parameters = method.parameters.map { param ->
                ParameterSpec.builder(param.simpleName.toString(),
                    param.asType().asTypeName()).build()
            }
            val arguments = method.parameters.joinToString(", ") { it.simpleName.toString() }
            val greetingMessage = method.getAnnotation(Greeting::class.java)?.message ?: "Hello!"

            // Generated method: prints a greeting, then forwards the call to the wrapped instance.
            val methodBuilder = FunSpec.builder(methodName)
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
                .addParameters(parameters)
                .addStatement("println(%S)", greetingMessage)
                .addStatement("original.$methodName($arguments)")

            classBuilder.addFunction(methodBuilder.build())
        }

        val file = FileSpec.builder(packageName, wrapperClassName)
            .addType(classBuilder.build())
            .build()

        try {
            // KAPT provides the destination folder via this option.
            val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
            if (kaptKotlinGeneratedDir != null) {
                file.writeTo(File(kaptKotlinGeneratedDir))
            } else {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                    "kapt.kotlin.generated not found")
            }
        } catch (e: Exception) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "Error generating Kotlin file: ${e.message}")
        }
    }
}
