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
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.Plugin

import java.nio.file.Files
import java.nio.file.Path

import static com.google.common.io.Files.getFileExtension
import static me.finalchild.groovybukkit.extension.Util.on

class Host {

    void initDependencyHandler() {
        on(PluginEnableEvent) {
            if (requireMap.containsKey(event.plugin.name)) {
                Set<String> require = requireMap.remove(event.plugin.name)
                require.forEach { id ->
                    evalScript(id)
                }
            }
        }
    }

    private final Map<String, Script> pscripts = [:]
    private final Map<String, ScriptStatus> pscriptStatuses = [:]

    private final Map<String, ScriptLoader> pscriptLoaders = [:]

    private final Map<String, Set<String>> requireMap = [:]

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
                } catch (ScriptLoaderNotFoundException ignored) {
                } catch (Throwable e) {
                    GroovyBukkit.instance.logger.severe("Failed to load a file as a script: $file.fileName")
                    e.printStackTrace()
                }
            }
        }
    }

    Script loadScript(Path file) {
        loadScript(getScriptLoader(file).orElseThrow { ->
            new ScriptLoaderNotFoundException(file)
        }.loadScript(file, this))
    }

    Script loadScript(Script script) {
        if (isIdBeingUsed(script.id)) {
            throw new UnsupportedOperationException("Duplicate id: $script.id")
        }
        pscripts.put(script.id, script)
        pscriptStatuses.put(script.id, ScriptStatus.LOADED)
        return script
    }

    void evalScripts() {
        scripts.forEach { id, loadedScript ->
            evalScript(id)
        }
    }

    Script evalScript(String id) {
        try {
            return evalScriptRaw(id)
        } catch (DependencyNotEnabledException e) {
            GroovyBukkit.instance.logger.info("DependencyNotEnabledException was caught: $id")

            Set require = requireMap[e.dependencyId]

            if (require == null) {
                requireMap[e.dependencyId] = Collections.singleton(id)
            } else {
                require = new HashSet<String>(require)
                require.add(id)
                requireMap[e.dependencyId] = require
            }

            return scripts[id]
        } catch (Exception e) {
            GroovyBukkit.instance.logger.severe("Failed to run a script: $id")
            e.printStackTrace()
            return scripts[id]
        }
    }

    Script evalScriptRaw(String id) {
        if (!(id in scripts)) {
            throw new IllegalStateException('Tried to evaluate an unloaded script')
        }
        if (scriptStatuses[id] == ScriptStatus.EVALING) {
            throw new IllegalStateException('Tried to evaluate a script which is already being evaluated')
        }
        if (scriptStatuses[id] == ScriptStatus.EVALED) {
            return scripts[id]
        }


        Script script = scripts[id]

        pscriptStatuses[id] = ScriptStatus.EVALING
        try {
            script.eval()
        } catch (Exception e) {
            pscriptStatuses[id] = ScriptStatus.LOADED
            throw e
        }
        pscriptStatuses[id] = ScriptStatus.EVALED

        if (requireMap.containsKey(id)) {
            Set<String> require = requireMap.remove(id)
            require.forEach { dependingScript ->
                evalScript(dependingScript)
            }
        }

        return script
    }

    Object require(String id) {
        getScriptStatus(id).ifPresent { status ->
            switch (status) {
                case ScriptStatus.LOADED:
                    Script script = evalScriptRaw(id)
                    if (getScriptStatus(id).get() == ScriptStatus.EVALED) {
                        return script
                    } else {
                        throw new DependencyNotEnabledException(id)
                    }
                    break
                case ScriptStatus.EVALING:
                    throw new IllegalStateException('Circular dependency found!')
                case ScriptStatus.EVALED:
                    return scripts[id]
            }
        }

        if (Bukkit.pluginManager.isPluginEnabled(id)) {
            return Bukkit.pluginManager.getPlugin(id)
        } else {
            throw new DependencyNotEnabledException(id)
        }
    }

    Map<String, Script> getScripts() {
        Collections.unmodifiableMap(pscripts)
    }

    Optional<Script> getScript(String id) {
        Optional.ofNullable(scripts[id])
    }

    Map<String, ScriptStatus> getScriptStatuses() {
        Collections.unmodifiableMap(pscriptStatuses)
    }

    Optional<ScriptStatus> getScriptStatus(String id) {
        Optional.ofNullable(scriptStatuses[id])
    }

    Map<String, ScriptLoader> getScriptLoaders() {
        Collections.unmodifiableMap(pscriptLoaders)
    }

    Optional<ScriptLoader> getScriptLoader(Path file) {
        getScriptLoader(getFileExtension(file.toAbsolutePath().toString()))
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
        Bukkit.pluginManager.getPlugin(id) != null || scripts.containsKey(id)
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

    void onDisable() {
        scripts.values().each {
            it.disable()
        }
    }

}
