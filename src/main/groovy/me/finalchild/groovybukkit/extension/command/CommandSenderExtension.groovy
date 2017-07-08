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

package me.finalchild.groovybukkit.extension.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

/**
 * Extends {@link CommandSender}.
 */
final class CommandSenderExtension {

    private CommandSenderExtension() {}

    /**
     * Dispatches a command on this server, and executes it if found.
     *
     * @param self
     * @param commandLine the command + arguments. Example: test abc 123
     * @return returns false if no target is found
     */
    static boolean run(CommandSender self, String commandLine) {
        Bukkit.dispatchCommand(self, commandLine)
    }

    static void leftShift(CommandSender self, String message) {
        self.sendMessage(message)
    }

    static void leftShift(CommandSender self, String... messages) {
        self.sendMessage(messages)
    }

}
