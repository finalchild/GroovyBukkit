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

package me.finalchild.groovybukkit.script

import me.finalchild.groovybukkit.GroovyBukkit
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Supplier

class Host {

    private final Map<String, Script> loadedScripts = new HashMap<>()

    private final Map<String, ScriptLoader> scriptLoaders = new HashMap<>()

    void loadScripts(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectory(directory)
            } catch (IOException e) {
                e.printStackTrace()
                return
            }
        }

        directory.eachFile { Path file ->
            if (!Files.isDirectory(file)) {
                try {
                    loadScript(file)
                } catch (Exception e) {
                    GroovyBukkit.instance.logger.severe('Failed to load a file as a script: ' + file.fileName)
                    e.printStackTrace()
                }
            }
        }
    }

    Script loadScript(Path file) {
        loadScript(getScriptLoader(file).orElseThrow { ->
                new UnsupportedOperationException('Could not find a ScriptLoader for the file: ' + file.fileName.toString())
            }.loadScript(file, this))
    }

    Script loadScript(Script script) {
        if (isIdBeingUsed(script.id)) {
            throw new UnsupportedOperationException('Duplicate id: ' + script.id)
        }
        loadedScripts.put(script.id, script)
        return script
    }

    void evalScripts() {
        for (Script loadedScript : loadedScripts.values()) {
            try {
                loadedScript.eval()
            } catch (Exception e) {
                GroovyBukkit.instance.logger.severe('Failed to run a script: ' + loadedScript.id)
                e.printStackTrace()
            }
        }
    }

    Map<String, Script> getScripts() {
        Collections.unmodifiableMap(loadedScripts)
    }

    Optional<Script> getScript(String id) {
        Optional.ofNullable(loadedScripts[id])
    }

    void onDisable() {
        loadedScripts.values().each {
            it.disable()
        }
    }

    Map<String, ScriptLoader> getScriptLoaders() {
        Collections.unmodifiableMap(scriptLoaders)
    }

    Optional<ScriptLoader> getScriptLoader(Path file) {
        getScriptLoader(com.google.common.io.Files.getFileExtension(file.toString()))
    }

    Optional<ScriptLoader> getScriptLoader(String fileExtension) {
        Optional.ofNullable(scriptLoaders[fileExtension])
    }

    void addScriptLoader(ScriptLoader loader, Set<String> fileExtensions) {
        fileExtensions.each {
            this.@scriptLoaders.put(it, loader)
        }
    }

    boolean isIdBeingUsed(String id) {
        Bukkit.getPluginManager().getPlugin(id) != null || scripts.containsKey(id)
    }

    Optional<Object> getById(String id) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(id)
        if (plugin != null) {
            return Optional.of(plugin)
        }

        Optional<Script> optionalScript = getScript(id)
        if (optionalScript.isPresent()) {
            return Optional.of(optionalScript.get())
        }

        Optional.empty()
    }
}
