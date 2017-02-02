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

package me.finalchild.groovybukkit.util

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandMap

import java.lang.reflect.Field

/**
 * Reflection-based CommandMap utility class.
 */
final class CommandUtil {

    private CommandUtil() {
    }

    private static CommandMap commandMap

    /**
     * Returns the CommandMap of the server.
     * @return the CommandMap of the server.
     */
    static CommandMap getCommandMap() {
        if (commandMap == null) {
            try {
                Server server = Bukkit.getServer()
                Field commandMapField
                commandMapField = server.getClass().getDeclaredField('commandMap')
                commandMapField.setAccessible(true)
                commandMap = (CommandMap) commandMapField.get(server)
            } catch (Throwable t) {
                throw new UnsupportedOperationException('commandMap reflection failed.', t)
            }
        }
        return commandMap
    }

    /**
     * Registers a command. Returns true on success; false if name is already
     * taken and fallback had to be used.
     * <p>
     * Caller can use:-
     * <ul>
     * <li>command.getName() to determine the label registered for this
     *     command
     * <li>command.getAliases() to determine the aliases which where
     *     registered
     * </ul>
     *
     * @param fallbackPrefix a prefix which is prepended to the command with a
     *     ':' one or more times to make the command unique
     * @param command the command to register, from which label is determined
     *     from the command name
     * @return true if command was registered with the passed in label, false
     *     otherwise, which indicates the fallbackPrefix was used one or more
     *     times
     */
    static boolean register(String fallbackPrefix, Command command) {
        getCommandMap().register(fallbackPrefix, command)
    }

}
