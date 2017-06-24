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
import sun.plugin.dom.exception.InvalidStateException

import java.nio.file.Files
import java.nio.file.Path

import static com.google.common.io.Files.getFileExtension

class Host {

    private final Map<String, Script> pscripts = [:]
    private final Set<String> pscriptsBeingEvaled = new HashSet<>()
    private final Set<String> pevaledScripts = new HashSet<>()

    private final Map<String, ScriptLoader> pscriptLoaders = [:]

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
                    GroovyBukkit.instance.logger.severe("Failed to load a file as a script: $file.fileName")
                    e.printStackTrace()
                }
            }
        }
    }

    Script loadScript(Path file) {
        loadScript(getScriptLoader(file).orElseThrow { ->
            new UnsupportedOperationException("Could not find a ScriptLoader for the file: $file.fileName")
        }.loadScript(file, this))
    }

    Script loadScript(Script script) {
        if (isIdBeingUsed(script.id)) {
            throw new UnsupportedOperationException("Duplicate id: $script.id")
        }
        scripts.put(script.id, script)
        return script
    }

    void evalScripts() {
        scripts.forEach { id, loadedScript ->
            try {
                evalScript(id)
                loadedScript.eval()
            } catch (Exception e) {
                GroovyBukkit.instance.logger.severe("Failed to run a script: $id")
                e.printStackTrace()
            }
        }
    }

    void evalScript(String id) {
        if (!(id in scripts)) {
            throw new InvalidStateException('Tried to evaluate an unloaded script')
        }
        if (id in scriptsBeingEvaled) {
            throw new InvalidStateException('Tried to evaluate a script which was already being evaluated')
        }
        if (id in evaledScripts) {
            throw new InvalidStateException('Tried to evaluate an already evaluated script')
        }
        pscriptsBeingEvaled << id
        scripts[id].eval()
        this.pscriptsBeingEvaled.remove(id)
        this.pevaledScripts << id
    }

    Map<String, Script> getScripts() {
        Collections.unmodifiableMap(pscripts)
    }

    Optional<Script> getScript(String id) {
        Optional.ofNullable(scripts[id])
    }

    Set<String> getScriptsBeingEvaled() {
        Collections.unmodifiableSet(pscriptsBeingEvaled)
    }

    Set<String> getEvaledScripts() {
        Collections.unmodifiableSet(pevaledScripts)
    }

    void onDisable() {
        scripts.values().each {
            it.disable()
        }
    }

    Map<String, ScriptLoader> getScriptLoaders() {
        Collections.unmodifiableMap(scriptLoaders)
    }

    Optional<ScriptLoader> getScriptLoader(Path file) {
        getScriptLoader(getFileExtension(file.toString()))
    }

    Optional<ScriptLoader> getScriptLoader(String fileExtension) {
        Optional.ofNullable(scriptLoaders[fileExtension])
    }

    void addScriptLoader(ScriptLoader loader, Set<String> fileExtensions) {
        fileExtensions.each {
            this.pscriptLoaders.put(it, loader)
        }
    }

    boolean isIdBeingUsed(String id) {
        Bukkit.getPluginManager().getPlugin(id) != null || scripts.containsKey(id)
    }

    Optional<Object> getById(String id) {
        Plugin plugin = Bukkit.pluginManager.getPlugin(id)
        if (plugin != null) {
            return Optional.of(plugin)
        }

        Optional<Script> optionalScript = getScript(id)
        if (optionalScript.present) {
            return Optional.of(optionalScript.get())
        }

        Optional.empty()
    }

}
