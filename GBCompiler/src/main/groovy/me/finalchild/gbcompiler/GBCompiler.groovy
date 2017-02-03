/*
 * This file is part of GroovyBukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Final Child
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.finalchild.gbcompiler

import joptsimple.OptionParser
import joptsimple.OptionSet
import me.finalchild.gbcompiler.gui.CompilerApp
import me.finalchild.groovybukkit.GBScript
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.codehaus.groovy.tools.Compiler
import org.yaml.snakeyaml.Yaml

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

class GBCompiler {

    Path file
    Path targetFile
    Path tempDir = com.google.common.io.Files.createTempDir().toPath()
    String id

    String name
    String version
    String description
    String author
    String[] authors
    String website

    void compile() {
        CompilerConfiguration config = new CompilerConfiguration()
        config.classpathList = ['lib/*']
        config.sourceEncoding = 'utf-8'
        config.setScriptBaseClass GBScript.name
        config.targetDirectory = tempDir.toFile()

        ImportCustomizer importCustomizer = new ImportCustomizer()
        customizer.addStarImports 'me.finalchild.groovybukkit.util.Enchants', 'org.bukkit.block', 'org.bukkit.entity', 'org.bukkit.generator', 'org.bukkit.util.noise', 'org.bukkit.material', 'org.bukkit.configuration.file', 'org.bukkit.plugin.java', 'org.bukkit.conversations', 'org.bukkit.plugin.messaging', 'org.bukkit', 'org.bukkit.inventory.meta', 'org.bukkit.scheduler', 'org.bukkit.configuration', 'org.bukkit.material.types', 'org.bukkit.projectiles', 'org.bukkit.util', 'org.bukkit.enchantments', 'org.bukkit.boss', 'org.bukkit.event.hanging', 'org.bukkit.permissions', 'org.bukkit.command.defaults', 'org.bukkit.potion', 'org.bukkit.util.permissions', 'org.bukkit.event.block', 'org.bukkit.event.inventory', 'org.bukkit.util.io', 'org.bukkit.event.player', 'org.bukkit.command', 'org.bukkit.inventory', 'org.bukkit.help', 'org.bukkit.plugin', 'org.bukkit.event.vehicle', 'org.bukkit.event', 'org.bukkit.attribute', 'org.bukkit.configuration.serialization', 'org.bukkit.scoreboard', 'org.bukkit.event.server', 'org.bukkit.map', 'org.bukkit.entity.minecart', 'org.bukkit.block.banner', 'org.bukkit.metadata', 'org.bukkit.event.entity', 'org.bukkit.event.weather', 'org.bukkit.event.world', 'org.bukkit.event.enchantment'
        customizer.addStaticStars 'me.finalchild.groovybukkit.extension.Util', 'me.finalchild.groovybukkit.util.Enchants', 'org.bukkit.Material'
        config.addCompilationCustomizers importCustomizer

        ASTTransformationCustomizer astCustomizer = new ASTTransformationCustomizer(new Transformer())
        config.addCompilationCustomizers astCustomizer

        Compiler compiler = new Compiler(config)
        compiler.compile(file.toFile())
    }

    void makeJar() {
        JarOutputStream target
        try {
            target = new JarOutputStream(targetFile.newOutputStream())
            add tempDir, target

            Map<String, Object> descriptor = [:]
            name ? descriptor.put('name', name) : descriptor.put('name', id)
            version ? descriptor.put('version', version) : descriptor.put('version', '1.0.0-SNAPSHOT')
            description ? descriptor.put('description', description) : null
            author ? descriptor.put('author', author) : null
            authors ? descriptor.put('authors', author) : null
            website ? descriptor.put('website', website) : null
            descriptor.put('main', id)
            descriptor.put('depend', ['GroovyBukkit'])

            JarEntry entry = new JarEntry('plugin.yml')
            entry.time = System.currentTimeMillis()
            target.putNextEntry(entry)
            Yaml yaml = new Yaml()
            yaml.dump(descriptor, target.newWriter('utf-8'))
            target.closeEntry()
        } finally {
            if (target != null) target.close()
        }
    }

    void add(Path source, JarOutputStream target) throws IOException {
        if (Files.isDirectory(source)) {
            String name = tempDir.relativize(source).toString().replace("\\", "/")
            if (!name.isEmpty()) {
                if (!name.endsWith("/")) {
                    name += "/"
                }
                JarEntry entry = new JarEntry(name)
                entry.time = Files.getLastModifiedTime(source).toMillis()
                target.putNextEntry entry
                target.closeEntry()
            }

            source.eachFile { Path path -> add path, target }
        } else {
            JarEntry entry = new JarEntry(tempDir.relativize(source).toString().replace("\\", "/"))
            entry.time = Files.getLastModifiedTime(source).toMillis()
            target.putNextEntry(entry)

            source.withInputStream {
                byte[] buffer = new byte[1024]
                while (true) {
                    int count = it.read(buffer)
                    if (count == -1)
                        break
                    target.write(buffer, 0, count)
                }
                target.closeEntry()
            }
        }
    }

    void clean() {
        if (Files.exists(tempDir)) {
            Files.walkFileTree(tempDir, new SimpleFileVisitor<Path>() {
                @Override
                FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file)
                    return FileVisitResult.CONTINUE
                }

                @Override
                FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir)
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }

    void run() {
        compile()
        makeJar()
        clean()
    }

    static void main(String[] args) {
        if (args.length == 0) {
            CompilerApp.launch(CompilerApp.class)
        } else {
            OptionParser parser = new OptionParser()
            parser.with {
                accepts 'name' withRequiredArg()
                accepts 'version' withRequiredArg() defaultsTo '1.0.0-SNAPSHOT'
                accepts 'description' withRequiredArg()
                accepts 'author' withRequiredArg()
                accepts 'authors' withRequiredArg()
                accepts 'website' withRequiredArg()
                nonOptions() ofType File
            }
            OptionSet options = parser.parse(args)
            Path file = options.nonOptionArguments()[0].toPath()
            Path target = options.nonOptionArguments()[1].toPath()

            new GBCompiler(
                    file: file,
                    targetFile: target,
                    id: com.google.common.io.Files.getNameWithoutExtension(file.toString()),
                    name: options.valueOf('name') ?: com.google.common.io.Files.getNameWithoutExtension(file.toString()),
                    version: options.valueOf('version'),
                    description: options.valueOf('description'),
                    author: options.valueOf('author'),
                    authors: ((String) options.valueOf('authors')).split(';'),
                    website: options.valueOf('website')
            ).run()
        }
    }

}
