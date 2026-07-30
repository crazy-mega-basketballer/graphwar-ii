package com.example.gra.engine

import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.*

object MathParser {
    fun eval(expression: String, x: Double): Double? {
        return try {
            // Validate and prepare expression
            if (expression.isBlank()) return null

            // Replace π symbol with pi constant for exp4j
            val processedExpression = expression
                .replace("π", "pi")
                .trim()

            val e = ExpressionBuilder(processedExpression)
                .variable("x")
                .build()
                .setVariable("x", x)

            val result = e.evaluate()

            // Strict validation of result
            when {
                result.isNaN() -> null
                result.isInfinite() -> null
                result.absoluteValue > 1000000.0 -> null // Prevent extreme values
                else -> result
            }
        } catch (e: Exception) {
            // Silently handle parsing errors
            null
        }
    }
}
