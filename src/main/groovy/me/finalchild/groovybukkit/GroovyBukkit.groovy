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

package me.finalchild.groovybukkit

import groovy.time.BaseDuration
import groovy.time.TimeCategory
import me.finalchild.groovybukkit.command.ExeCommand
import me.finalchild.groovybukkit.script.Host
import me.finalchild.groovybukkit.script.gb.GBScriptLoader
import me.finalchild.groovybukkit.updater.LatestReleaseChecker
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

/**
 * A Bukkit plugin to support writing scripts and plugins using Groovy.
 * This class is singleton and the instance is created by Bukkit.
 * To get the instance, use {@link #instance}.
 */
class GroovyBukkit extends JavaPlugin implements Listener {

    private static GroovyBukkit instance

    private Host host

    static {
        Integer.mixin TimeCategory
        Date.mixin TimeCategory
        BaseDuration.mixin TimeCategory
    }

    /**
     * The instance is created by Bukkit. Do not create an instance yourself.
     */
    GroovyBukkit() {
        if (instance != null) {
            throw new IllegalStateException('An instance of GroovyBukkit is already present.')
        }
        instance = this

        host = new Host()
        host.addScriptLoader(new GBScriptLoader(), ['groovy', 'gvy', 'gy', 'gsh'].toSet())
    }

    /**
     * Returns the singleton instance.
     *
     * @return the singleton instance
     */
    static GroovyBukkit getInstance() {
        if (instance == null) {
            throw new IllegalStateException('GroovyBukkit is not yet initialized.')
        }
        return instance
    }

    @Override
    void onEnable() {
        LatestReleaseChecker.latestRelease.ifPresent {
            logger.info("A new release($it.name) of GroovyBukkit is found!")
            logger.info("Update now: $it.url")
        }

        getCommand('groovyexe').executor = new ExeCommand()

        host.initDependencyHandler()
        host.loadScripts(dataFolder.toPath())
        host.evalScripts()
    }

    @Override
    void onDisable() {
        host.onDisable()
    }

}
