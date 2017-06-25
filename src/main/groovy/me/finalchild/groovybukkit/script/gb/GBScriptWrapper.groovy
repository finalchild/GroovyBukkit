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

package me.finalchild.groovybukkit.script.gb

import me.finalchild.groovybukkit.GBScript
import me.finalchild.groovybukkit.script.Host
import me.finalchild.groovybukkit.script.Script as FScript

import java.nio.file.Files
import java.nio.file.Path

import static com.google.common.io.Files.getNameWithoutExtension

final class GBScriptWrapper implements FScript {

    private final Script handle

    private final Host host
    private final String id

    private final Path dataFolder

    GBScriptWrapper(Path file, Host host, GroovyScriptEngine engine) throws IOException {
        Binding binding = new Binding()

        binding.setVariable("wrapper", this)

        handle = engine.createScript(file.fileName.toString(), binding)

        this.host = host
        id = getNameWithoutExtension(file.toAbsolutePath().toString())

        dataFolder = file.parent.resolve(id)
        if (Files.exists(dataFolder)) {
            if (!Files.isDirectory(dataFolder)) {
                throw new IOException("Data folder for the script $id is not a directory!")
            }
        } else {
            Files.createDirectory(dataFolder)
        }
    }

    @Override
    void eval() {
        handle.run()
    }

    @Override
    void disable() {
        if (handle instanceof GBScript) {
            (handle as GBScript).saveConfig()
        }
    }

    @Override
    Host getHost() {
        return host
    }

    @Override
    String getId() {
        return id
    }

    Path getDataFolder() {
        return dataFolder
    }

}
