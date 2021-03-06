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

import me.finalchild.groovybukkit.script.gb.GBScriptWrapper
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

import java.nio.file.Path

abstract class GBScript extends Script {

    abstract void onEnable()

    private GBScriptWrapper pwrapper
    private FileConfiguration pconfig

    @Override
    Object run() {
        onEnable()
        null
    }

    GBScriptWrapper getWrapper() {
        if (pwrapper == null) {
            pwrapper = binding.getVariable('wrapper') as GBScriptWrapper
        }
        pwrapper
    }

    Path getConfigPath() {
        wrapper.dataFolder.resolve('config.yml')
    }

    FileConfiguration getConfig() {
        if (pconfig == null) {
            reloadConfig()
        }
        pconfig
    }

    void reloadConfig() {
        pconfig = YamlConfiguration.loadConfiguration(configPath.toFile())
    }

    void saveConfig() {
        if (pconfig != null) pconfig.save(configPath.toFile())
    }

    Path getDataFolder() {
        wrapper.dataFolder
    }

    Object require(String id) {
        wrapper.host.require(id)
    }

    @Override
    String toString() {
        "GBScript: $wrapper.id"
    }

    static GroovyBukkit getPlugin() {
        GroovyBukkit.instance
    }

}
