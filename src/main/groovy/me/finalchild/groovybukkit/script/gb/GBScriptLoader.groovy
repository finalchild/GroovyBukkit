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
        config.scriptBaseClass = GBScript.name
        config.sourceEncoding = 'UTF-8'

        ImportCustomizer customizer = new ImportCustomizer()
        customizer.addStarImports 'me.finalchild.groovybukkit.util.Enchants', 'org.bukkit', 'org.bukkit.advancement', 'org.bukkit.attribute', 'org.bukkit.block', 'org.bukkit.block.banner', 'org.bukkit.boss', 'org.bukkit.command', 'org.bukkit.command.defaults', 'org.bukkit.configuration', 'org.bukkit.configuration.file', 'org.bukkit.configuration.serialization', 'org.bukkit.conversations', 'org.bukkit.enchantments', 'org.bukkit.entity', 'org.bukkit.entity.minecart', 'org.bukkit.event', 'org.bukkit.event.block', 'org.bukkit.event.enchantment', 'org.bukkit.event.entity', 'org.bukkit.event.hanging', 'org.bukkit.event.inventory', 'org.bukkit.event.painting', 'org.bukkit.event.player', 'org.bukkit.event.server', 'org.bukkit.event.vehicle', 'org.bukkit.event.weather', 'org.bukkit.event.world', 'org.bukkit.generator', 'org.bukkit.help', 'org.bukkit.inventory', 'org.bukkit.inventory.meta', 'org.bukkit.map', 'org.bukkit.material', 'org.bukkit.material.types', 'org.bukkit.metadata', 'org.bukkit.permissions', 'org.bukkit.plugin', 'org.bukkit.plugin.java', 'org.bukkit.plugin.messaging', 'org.bukkit.potion', 'org.bukkit.projectiles', 'org.bukkit.scheduler', 'org.bukkit.scoreboard', 'org.bukkit.util', 'org.bukkit.util.io', 'org.bukkit.util.noise', 'org.bukkit.util.permissions'
        customizer.addStaticStars 'me.finalchild.groovybukkit.extension.Util', 'me.finalchild.groovybukkit.util.Enchants', 'org.bukkit.Material'
        config.addCompilationCustomizers customizer

        engine.config = config
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
