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

import org.bukkit.enchantments.Enchantment

import static org.bukkit.enchantments.Enchantment.*

/**
 * Redirects to {@link Enchantment}s by its displayed name.
 */
class Enchants {

    private Enchants() {}

    static final Enchantment PROTECTION = PROTECTION_ENVIRONMENTAL
    static final Enchantment FIRE_PROTECTION = PROTECTION_FIRE
    static final Enchantment FEATHER_FALLING = PROTECTION_FALL
    static final Enchantment BLAST_PROTECTION = PROTECTION_EXPLOSIONS
    static final Enchantment PROJECTILE_PROTECTION = PROTECTION_PROJECTILE
    static final Enchantment RESPIRATION = OXYGEN
    static final Enchantment AQUA_AFFINITY = WATER_WORKER
    static final Enchantment THORNS = THORNS
    static final Enchantment DEPTH_STRIDER = DEPTH_STRIDER
    static final Enchantment FROST_WALKER = FROST_WALKER
    static final Enchantment CURSE_OF_BINDING = BINDING_CURSE

    static final Enchantment SHARPNESS = DAMAGE_ALL
    static final Enchantment SMITE = DAMAGE_UNDEAD
    static final Enchantment BANE_OF_ARTHROPODS = DAMAGE_ARTHROPODS
    static final Enchantment KNOCKBACK = KNOCKBACK
    static final Enchantment FIRE_ASPECT = FIRE_ASPECT
    static final Enchantment LOOTING = LOOT_BONUS_MOBS
    static final Enchantment SWEEPING_EDGE = SWEEPING_EDGE

    static final Enchantment EFFICIENCY = DIG_SPEED
    static final Enchantment SILK_TOUCH = SILK_TOUCH
    static final Enchantment UNBREAKING = DURABILITY
    static final Enchantment FORTUNE = LOOT_BONUS_BLOCKS

    static final Enchantment POWER = ARROW_DAMAGE
    static final Enchantment PUNCH = ARROW_KNOCKBACK
    static final Enchantment FLAME = ARROW_FIRE
    static final Enchantment INFINITY = ARROW_INFINITE

    static final Enchantment LUCK_OF_THE_SEA = LUCK
    static final Enchantment LURE = LURE

    static final Enchantment MENDING = MENDING
    static final Enchantment CURSE_OF_VANISHING = VANISHING_CURSE

}
