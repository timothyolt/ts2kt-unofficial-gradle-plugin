/*
 * This file is part of ts2kt-unofficial-gradle-plugin-functional-tests, licensed under the MIT License (MIT).
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
package net.octyl.ts2kt.gradle

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.util.stream.Collectors.toList

object TestKitAssist {
    val files: List<File> by lazy {
        val fileList = javaClass.classLoader.getResource("plugin-classpath.txt")
                ?: throw IllegalStateException("Missing plugin classpath, please run Gradle build!")

        fileList.openStream().bufferedReader().lines()
                .map(::File)
                .collect(toList())
    }

}

internal fun File.writeGradleSetupAnd(moreText: String) {
    writeText("""
            buildscript {
                repositories {
                    jcenter()
                }
                dependencies {
                    "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}embeddedKotlinVersion")
                }
            }
            plugins {
                id("kotlin2js")
                id("net.octyl.ts2kt-unofficial")
            }

            repositories {
                jcenter()
            }

            dependencies {
                "compile"(kotlin("stdlib-js"))
            }
        """.trimIndent() + "\n" + moreText)
}

fun GradleRunner.withTs2ktPluginClasspath(): GradleRunner {
    withPluginClasspath(TestKitAssist.files)
    return this
}
