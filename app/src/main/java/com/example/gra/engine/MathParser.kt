package com.example.gra.engine

import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.*

object MathParser {
    fun eval(expression: String, x: Double): Double? {
        return try {
            val e = ExpressionBuilder(expression)
                .variable("x")
                .build()
                .setVariable("x", x)
            val result = e.evaluate()
            if (result.isNaN() || result.isInfinite()) null else result
        } catch (e: Exception) {
            null
        }
    }
}
