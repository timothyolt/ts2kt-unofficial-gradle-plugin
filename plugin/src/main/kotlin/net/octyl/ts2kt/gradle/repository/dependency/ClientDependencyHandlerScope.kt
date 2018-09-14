/*
 * This file is part of plugin, licensed under the MIT License (MIT).
 *
 * Copyright (c) Kenzie Togami <https://octyl.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.octyl.ts2kt.gradle.repository.dependency

import net.octyl.ts2kt.gradle.Ts2ktUnofficialExtension

class ClientDependencyHandlerScope(private val extension: Ts2ktUnofficialExtension) {
    operator fun String.invoke(dependencyNotation: Any): ExternalClientDependency {
        return when (dependencyNotation) {
            is Map<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                val asMap = dependencyNotation as Map<String, String>
                val name = asMap["name"] ?: throw IllegalArgumentException("No name specified.")

                this(asMap["group"], name, asMap["version"])
            }
            is String -> {
                val parts = dependencyNotation.split(':')
                if (parts.size < 2) {
                    throw IllegalArgumentException("Illegal string notation." +
                            " Must be at least <group>:<name>. Use :<name> if you have no group.")
                }
                val (group, name) = parts
                val version = parts.getOrNull(2)

                this(group, name, version)
            }
            else -> throw IllegalArgumentException("Illegal type for dependency notation: " +
                    dependencyNotation.javaClass.name)
        }
    }

    operator fun String.invoke(group: String?,
                               name: String,
                               version: String? = null): ExternalClientDependency {
        val groupFixed = when {
            group.isNullOrBlank() -> null
            else -> group
        }
        return ExternalClientDependency(groupFixed, name, version)
                .also { extension.getClientConfiguration(this).dependencies.add(it) }
    }
}
