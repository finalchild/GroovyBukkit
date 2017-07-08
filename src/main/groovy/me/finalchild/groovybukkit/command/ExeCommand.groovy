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

package me.finalchild.groovybukkit.command

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.player.PlayerQuitEvent

import static me.finalchild.groovybukkit.extension.Util.on
import static me.finalchild.groovybukkit.gshell.GShell.shell

class ExeCommand implements CommandExecutor {

    private Map<CommandSender, List<String>> codesMap = [:];

    {
        on(PlayerQuitEvent) {
            codesMap.remove(player)
        }
    }

    private void addCode(CommandSender sender, String code) {
        List<String> codes = codesMap[sender]
        if (codes == null) {
            codesMap[sender] = [code]
        } else {
            codes << code
        }
    }

    private String addLastCodeAndReturnJoined(CommandSender sender, String lastCode) {
        List<String> existingCodes = codesMap.remove(sender)
        if (existingCodes == null) {
            return lastCode
        } else {
            StringBuilder builder = new StringBuilder()
            existingCodes.forEach {
                builder.append('it').append('\n')
            }
            builder.append(lastCode)
            return builder.toString()
        }
    }

    @Override
    boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false
        }

        String code = args.join(' ')
        if (code.endsWith('\\')) {
            addCode(sender, code.substring(0, code.length() - 1))
        } else {
            Object existingSender = shell.getVariable('sender')
            shell.setVariable('sender', sender)
            try {
                sender.sendMessage("${ChatColor.GOLD}Evaluation Result: ${ChatColor.WHITE}${shell.evaluate(addLastCodeAndReturnJoined(sender, code)).toString()}")
            } catch (Throwable t) {
                sender.sendMessage("${ChatColor.DARK_RED}Something went wrong!")
                sender.sendMessage("${ChatColor.DARK_RED}${t}")
            }
            shell.setVariable('sender', existingSender)
        }
        return true
    }

}
