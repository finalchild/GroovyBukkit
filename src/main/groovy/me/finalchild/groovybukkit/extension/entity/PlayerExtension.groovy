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

package me.finalchild.groovybukkit.extension.entity

import org.bukkit.Achievement
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Extends {@link Player}
 */
final class PlayerExtension {

    private PlayerExtension() {}

    static void leftShift(Player self, Achievement achievement) {
        self.awardAchievement(achievement)
    }

    static HashMap<Integer, ItemStack> leftShift(Player self, ItemStack... items) {
        self.inventory.addItem(items)
    }

    static HashMap<Integer, ItemStack> leftShift(Player self, Material... materials) {
        ItemStack[] items = new ItemStack[materials.length]
        for (int i = 0; i < materials.length; i++) {
            items[i] = new ItemStack(materials[i])
        }
        self.inventory.addItem(items)
    }

}
