package processor

import annotations.Extract
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

// Regista automaticamente este processador no sistema de anotações
@AutoService(Processor::class)
// Define que este processador suporta até ao Java 21
@SupportedSourceVersion(SourceVersion.RELEASE_21)
// Define qual a anotação que este processador vai tratar
@SupportedAnnotationTypes("annotations.Extract")
class RegexProcessor : AbstractProcessor() {

    // Método principal do processador - é chamado automaticamente pelo compilador
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {

        // Mapa que associa cada classe aos seus métodos anotados com @Extract
        val classMethodMap = mutableMapOf<TypeElement, MutableList<ExecutableElement>>()

        // Percorre todos os métodos anotados com @Extract
        for (element in roundEnv.getElementsAnnotatedWith(Extract::class.java)) {
            if (element is ExecutableElement) {
                // Obtém a classe "pai" que contém o método anotado
                val enclosingClass = element.enclosingElement as TypeElement
                // Adiciona o método à lista da respetiva classe
                classMethodMap
                    .computeIfAbsent(enclosingClass) { mutableListOf() }
                    .add(element)
            }
        }

        // Para cada classe com métodos anotados, gera uma classe extratora
        for ((classElement, methods) in classMethodMap) {
            generateExtractorClass(classElement, methods)
        }

        // Retorna true para indicar que as anotações foram processadas
        return true
    }

    // Gera a classe extratora que implementa os métodos abstratos
    private fun generateExtractorClass(
        classElement: TypeElement,
        methods: List<ExecutableElement>
    ) {
        // Obtém o package da classe original (ex: "app")
        val packageName = processingEnv.elementUtils
            .getPackageOf(classElement).toString()

        // Obtém o nome simples da classe original (ex: "DataProcessor")
        val originalClassName = classElement.simpleName.toString()

        // Define o nome da classe gerada (ex: "DataProcessorExtractor")
        val extractorClassName = "${originalClassName}Extractor"

        // Começa a construir a classe gerada com KotlinPoet
        // A classe gerada estende a classe abstrata original
        val classBuilder = TypeSpec.classBuilder(extractorClassName)
            .superclass(ClassName(packageName, originalClassName))
            // Adiciona o construtor que recebe a string de input
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("input", String::class)
                    .build()
            )
            // Passa o input para o construtor da classe pai (DataProcessor)
            .addSuperclassConstructorParameter("input")

        // Para cada método anotado, gera a implementação correspondente
        for (method in methods) {
            // Obtém o nome do método (ex: "getName")
            val methodName = method.simpleName.toString()

            // Obtém o padrão regex definido na anotação @Extract
            val regex = method.getAnnotation(Extract::class.java)?.regex ?: ""

            // Constrói o método override com a lógica de extração
            val methodBuilder = FunSpec.builder(methodName)
                .addModifiers(KModifier.OVERRIDE)
                // O método retorna String? (pode ser null se não encontrar)
                .returns(String::class.asTypeName().copy(nullable = true))
                // Aplica a regex ao input e guarda o resultado
                .addStatement("val match = Regex(%S).find(input)", regex)
                // Retorna o primeiro grupo capturado, ou null se não houver correspondência
                .addStatement("return match?.groupValues?.get(1)")

            classBuilder.addFunction(methodBuilder.build())
        }

        // Constrói o ficheiro Kotlin final com a classe gerada
        val file = FileSpec.builder(packageName, extractorClassName)
            .addImport("kotlin.text", "Regex")
            .addType(classBuilder.build())
            .build()

        // Escreve o ficheiro gerado na diretoria de output do kapt
        try {
            val kaptKotlinGeneratedDir =
                processingEnv.options["kapt.kotlin.generated"]
            if (kaptKotlinGeneratedDir != null) {
                // Escreve o ficheiro na pasta correta
                file.writeTo(File(kaptKotlinGeneratedDir))
            } else {
                // Erro caso a pasta de output não seja encontrada
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "kapt.kotlin.generated não encontrado"
                )
            }
        } catch (e: Exception) {
            // Erro caso a escrita do ficheiro falhe
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Erro ao gerar ficheiro: ${e.message}"
            )
        }
    }
}