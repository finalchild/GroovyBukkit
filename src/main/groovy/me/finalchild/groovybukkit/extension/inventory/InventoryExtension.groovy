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

package me.finalchild.groovybukkit.extension.inventory

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.codehaus.groovy.runtime.DefaultGroovyMethodsSupport
import org.codehaus.groovy.runtime.RangeInfo

/**
 * Extends {@link Inventory}.
 */
class InventoryExtension {

    static ItemStack getAt(Inventory self, int index) {
        self.getItem(index)
    }

    static List<ItemStack> getAt(Inventory self, Range range) {
        self.contents[range]
    }

    static List<ItemStack> getAt(Inventory self, Collection indices) {
        self.contents[indices]
    }

    static void putAt(Inventory self, int index, ItemStack item) {
        self.setItem(index, item)
    }

    static boolean isCase(Inventory self, ItemStack item) {
        self.containsAtLeast(item, item.amount)
    }

    static boolean isCase(Inventory self, Material material) {
        self.contains(material)
    }

    static HashMap<Integer, ItemStack> leftShift(Inventory self, ItemStack... items) {
        self.addItem(items)
    }

    static HashMap<Integer, ItemStack> leftShift(Inventory self, Material... materials) {
        ItemStack[] items = new ItemStack[materials.length]
        for (int i = 0; i < materials.length; i++) {
            items[i] = new ItemStack(materials[i])
        }
        self.addItem(items)
    }

}
