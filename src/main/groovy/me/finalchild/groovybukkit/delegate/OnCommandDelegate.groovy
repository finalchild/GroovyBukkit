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

package me.finalchild.groovybukkit.delegate

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandSender

/**
 * Contains CommandSender, label, arguments for a command executor clojure.
 */
class OnCommandDelegate {

    /**
     * The CommandSender.
     */
    CommandSender sender

    /**
     * The command's label.
     */
    String label

    /**
     * Arguments used.
     */
    String[] args

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    void sendMessage(String message) {
        sender.sendMessage message
    }

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     */
    void sendMessage(String[] messages) {
        sender.sendMessage messages
    }

    /**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     */
    Server getServer() {
        sender.server
    }

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    String getName() {
        sender.name
    }

    /**
     * Dispatches a command on this server, and executes it if found.
     *
     * @param self
     * @param commandLine the command + arguments. Example: test abc 123
     * @return returns false if no target is found
     */
    boolean run(String commandLine) {
        Bukkit.dispatchCommand(sender, commandLine)
    }

}
