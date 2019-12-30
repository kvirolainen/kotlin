// KOTLIN_CONFIGURATION_FLAGS: +JVM.EMIT_JVM_TYPE_ANNOTATIONS
// TARGET_BACKEND: JVM
// IGNORE_BACKEND_FIR: JVM_IR
// IGNORE_BACKEND: JVM_IR
// JVM_TARGET: 1.8
// WITH_RUNTIME
// WITH_REFLECT
// FULL_JDK
package foo

import java.lang.reflect.AnnotatedType
import kotlin.reflect.jvm.javaConstructor
import kotlin.test.fail

@Target(AnnotationTarget.TYPE)
annotation class TypeAnn

enum class Kotlin (s: @TypeAnn String) {}

}

fun box(): String {

    checkTypeAnnotation(
        ::Kotlin.javaConstructor!!.annotatedParameterTypes.last(),
        "class java.lang.String",
        "@foo.TypeAnn()",
        "enum constructor"
    )

    return "OK"
}

fun checkTypeAnnotation(
    annotatedType: AnnotatedType,
    type: String,
    annotations: String,
    message: String
) {
    if (annotatedType.annotation() != annotations) fail("check $message (1): ${annotatedType.annotation()} != $annotations")

    if (annotatedType.type.toString() != type) fail("check $message (2): ${annotatedType.type} != $type")
}


fun AnnotatedType.annotation() = annotations.joinToString()
