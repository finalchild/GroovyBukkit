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
import me.finalchild.groovybukkit.GroovyBukkit
import me.finalchild.groovybukkit.script.Host
import me.finalchild.groovybukkit.script.Script as FScript
import me.finalchild.groovybukkit.script.ScriptLoader
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.nio.file.Path

final class GBScriptLoader implements ScriptLoader {

    GroovyScriptEngine engine = new GroovyScriptEngine(GroovyBukkit.instance.dataFolder.toURI().toURL())

    GBScriptLoader() {
        CompilerConfiguration config = new CompilerConfiguration()
        config.setScriptBaseClass GBScript.name

        ImportCustomizer customizer = new ImportCustomizer()
        customizer.addStarImports 'org.bukkit.block', 'org.bukkit.entity', 'org.bukkit.generator', 'org.bukkit.util.noise', 'org.bukkit.material', 'org.bukkit.configuration.file', 'org.bukkit.plugin.java', 'org.bukkit.conversations', 'org.bukkit.plugin.messaging', 'org.bukkit', 'org.bukkit.inventory.meta', 'org.bukkit.scheduler', 'org.bukkit.configuration', 'org.bukkit.material.types', 'org.bukkit.projectiles', 'org.bukkit.util', 'org.bukkit.enchantments', 'org.bukkit.boss', 'org.bukkit.event.hanging', 'org.bukkit.permissions', 'org.bukkit.command.defaults', 'org.bukkit.potion', 'org.bukkit.util.permissions', 'org.bukkit.event.block', 'org.bukkit.event.inventory', 'org.bukkit.util.io', 'org.bukkit.event.player', 'org.bukkit.command', 'org.bukkit.inventory', 'org.bukkit.help', 'org.bukkit.plugin', 'org.bukkit.event.vehicle', 'org.bukkit.event', 'org.bukkit.attribute', 'org.bukkit.configuration.serialization', 'org.bukkit.scoreboard', 'org.bukkit.event.server', 'org.bukkit.map', 'org.bukkit.entity.minecart', 'org.bukkit.block.banner', 'org.bukkit.metadata', 'org.bukkit.event.entity', 'org.bukkit.event.weather', 'org.bukkit.event.world', 'org.bukkit.event.enchantment'
        customizer.addStaticStars 'me.finalchild.groovybukkit.extension.Util'
        config.addCompilationCustomizers customizer

        engine.setConfig config
    }

    @Override
    FScript loadScript(Path path, Host host) {
        try {
            new GBScriptWrapper(path, host, engine)
        } catch (IOException e) {
            e.printStackTrace()
            null
        }
    }

}
