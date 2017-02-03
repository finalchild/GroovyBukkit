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

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Extends {@link ItemStack}.
 */
class ItemStackExtension {

    /**
     * Checks for existence of a display name.
     *
     * @param self
     * @return true if this has a display name
     */
    static boolean hasDisplayName(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.hasDisplayName()
        } else {
            false
        }
    }

    /**
     * Checks for existence of a display name.
     *
     * @param self
     * @return true if this has a display name
     */
    static boolean hasName(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.hasDisplayName()
        } else {
            false
        }
    }

    /**
     * Gets the display name that is set.
     * <p>
     * Plugins should check that hasDisplayName() returns <code>true</code>
     * before calling this method.
     *
     * @param self
     * @return the display name that is set
     */
    static String getDisplayName(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.displayName
        } else {
            null
        }
    }

    /**
     * Gets the display name that is set.
     * <p>
     * Plugins should check that hasDisplayName() returns <code>true</code>
     * before calling this method.
     *
     * @param self
     * @return the display name that is set
     */
    static String getName(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.displayName
        } else {
            null
        }
    }

    /**
     * Sets the display name.
     *
     * @param self
     * @param name the name to set
     */
    static void setDisplayName(ItemStack self, String name) {
        if (self.hasItemMeta()) {
            ItemMeta itemMeta = self.itemMeta
            itemMeta.displayName = name
            self.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
    }

    /**
     * Sets the display name.
     *
     * @param self
     * @param name the name to set
     */
    static void setName(ItemStack self, String name) {
        if (self.hasItemMeta()) {
            ItemMeta itemMeta = self.itemMeta
            itemMeta.displayName = name
            self.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
    }

    /**
     * Checks for existence of lore.
     *
     * @param self
     * @return true if this has lore
     */
    static boolean hasLore(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.hasLore()
        } else {
            false
        }
    }

    /**
     * Gets the lore that is set.
     * <p>
     * Plugins should check if hasLore() returns <code>true</code> before
     * calling this method.
     *
     * @param self
     * @return a list of lore that is set
     */
    static List<String> getLore(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.lore
        } else {
            Collections.emptyList()
        }
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param self
     * @param lore the lore that will be set
     */
    static void setLore(ItemStack self, List<String> lore) {
        if (self.hasItemMeta()) {
            ItemMeta itemMeta = self.itemMeta
            itemMeta.lore = lore
            self.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
    }

    /**
     * Checks for the existence of any enchantments.
     *
     * @param self
     * @return true if an enchantment exists on this meta
     */
    static boolean hasEnchants(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.hasEnchants()
        } else {
            false
        }
    }

    /**
     * Checks for existence of the specified enchantment.
     *
     * @param self
     * @param ench enchantment to check
     * @return true if this enchantment exists for this meta
     */
    static boolean hasEnchant(ItemStack self, Enchantment ench) {
        if (self.hasItemMeta()) {
            self.itemMeta.hasEnchant(ench)
        } else {
            false
        }
    }

    /**
     * Checks if the specified enchantment conflicts with any enchantments in
     * this ItemMeta.
     *
     * @param self
     * @param ench enchantment to test
     * @return true if the enchantment conflicts, false otherwise
     */
    static boolean hasConflictingEnchant(ItemStack self, Enchantment ench) {
        if (self.hasItemMeta()) {
            self.itemMeta.hasConflictingEnchant(ench)
        } else {
            false
        }
    }

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param self
     * @param itemFlags The hideflags which shouldn't be rendered
     */
    static void addItemFlags(ItemStack self, ItemFlag... itemFlags) {
        if (self.hasItemMeta()) {
            ItemMeta itemMeta = self.itemMeta
            itemMeta.addItemFlags(itemFlags)
            self.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
    }

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param self
     * @param itemFlags Hideflags which should be removed
     */
    static void removeItemFlags(ItemStack self, ItemFlag... itemFlags) {
        if (self.hasItemMeta()) {
            ItemMeta itemMeta = self.itemMeta
            itemMeta.removeItemFlags(itemFlags)
            self.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
    }

    /**
     * Get current set itemFlags. The collection returned is unmodifiable.
     *
     * @param self
     * @return A set of all itemFlags set
     */
    static Set<ItemFlag> getItemFlags(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.itemFlags
        } else {
            Collections.emptySet()
        }
    }

    /**
     * Check if the specified flag is present on this item.
     *
     * @param self
     * @param flag the flag to check
     * @return if it is present
     */
    static boolean hasItemFlag(ItemStack self, ItemFlag flag) {
        if (self.hasItemMeta()) {
            self.itemMeta.hasItemFlag(flag)
        } else {
            false
        }
    }

    /**
     * Return if the unbreakable tag is true. An unbreakable item will not lose
     * durability.
     *
     * @param self
     * @return true if the unbreakable tag is true
     */
    static boolean isUnbreakable(ItemStack self) {
        if (self.hasItemMeta()) {
            self.itemMeta.unbreakable
        } else {
            false
        }
    }

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param self
     * @param unbreakable true if set unbreakable
     */
    static void setUnbreakable(ItemStack self, boolean unbreakable) {
        if (self.hasItemMeta()) {
            ItemMeta itemMeta = self.itemMeta
            itemMeta.unbreakable = unbreakable
            self.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
    }

    static void leftShift(ItemStack self, Enchantment ench) {
        self.addUnsafeEnchantment(ench, 1)
    }

    static void leftShift(ItemStack self, Map.Entry<Enchantment, Integer> enchantment) {
        self.addUnsafeEnchantment(enchantment.key, enchantment.value)
    }

    static void leftShift(ItemStack self, ItemFlag flag) {
        if (self.hasItemMeta()) {
            ItemMeta itemMeta = self.itemMeta
            itemMeta.addItemFlags(flag)
            self.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
    }

    static ItemStack plus(ItemStack self, Enchantment ench) {
        ItemStack stack = self.clone()
        stack.addUnsafeEnchantment(ench, 1)
        stack
    }

    static ItemStack plus(ItemStack self, Map.Entry<Enchantment, Integer> enchantment) {
        ItemStack stack = self.clone()
        stack.addUnsafeEnchantment(enchantment.key, enchantment.value)
        stack
    }

    static ItemStack plus(ItemStack self, ItemFlag flag) {
        ItemStack stack = self.clone()
        if (stack.hasItemMeta()) {
            ItemMeta itemMeta = stack.itemMeta
            itemMeta.addItemFlags(flag)
            stack.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
        stack
    }

    static ItemStack minus(ItemStack self, Enchantment ench) {
        ItemStack stack = self.clone()
        stack.removeEnchantment(ench)
        stack
    }

    static ItemStack minus(ItemStack self, Map.Entry<Enchantment, Integer> enchantment) {
        ItemStack stack = self.clone()
        stack.removeEnchantment(enchantment.key)
        stack
    }

    static ItemStack minus(ItemStack self, ItemFlag flag) {
        ItemStack stack = self.clone()
        if (stack.hasItemMeta()) {
            ItemMeta itemMeta = stack.itemMeta
            itemMeta.removeItemFlags(flag)
            stack.itemMeta = itemMeta
        } else {
            throw new UnsupportedOperationException("No ItemMeta for the item ${self.toString()} found.")
        }
        stack
    }

}
