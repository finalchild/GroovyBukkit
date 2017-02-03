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

/**
 * Redirects to {@link Enchantment}s by its displayed name.
 */
class Enchants {

    private Enchants() {}

    static final Enchantment PROTECTION = Enchantment.PROTECTION_ENVIRONMENTAL
    static final Enchantment FIRE_PROTECTION = Enchantment.PROTECTION_FIRE
    static final Enchantment FEATHER_FALLING = Enchantment.PROTECTION_FALL
    static final Enchantment BLAST_PROTECTION = Enchantment.PROTECTION_EXPLOSIONS
    static final Enchantment PROJECTILE_PROTECTION = Enchantment.PROTECTION_PROJECTILE
    static final Enchantment RESPIRATION = Enchantment.OXYGEN
    static final Enchantment AQUA_AFFINITY = Enchantment.WATER_WORKER
    static final Enchantment THORNS = Enchantment.THORNS
    static final Enchantment DEPTH_STRIDER = Enchantment.DEPTH_STRIDER
    static final Enchantment FROST_WALKER = Enchantment.FROST_WALKER
    static final Enchantment CURSE_OF_BINDING = Enchantment.BINDING_CURSE

    static final Enchantment SHARPNESS = Enchantment.DAMAGE_ALL
    static final Enchantment SMITE = Enchantment.DAMAGE_UNDEAD
    static final Enchantment BANE_OF_ARTHROPODS = Enchantment.DAMAGE_ARTHROPODS
    static final Enchantment KNOCKBACK = Enchantment.KNOCKBACK
    static final Enchantment FIRE_ASPECT = Enchantment.FIRE_ASPECT
    static final Enchantment LOOTING = Enchantment.LOOT_BONUS_MOBS
    static final Enchantment SWEEPING_EDGE = Enchantment.SWEEPING_EDGE

    static final Enchantment EFFICIENCY = Enchantment.DIG_SPEED
    static final Enchantment SILK_TOUCH = Enchantment.SILK_TOUCH
    static final Enchantment UNBREAKING = Enchantment.DURABILITY
    static final Enchantment FORTUNE = Enchantment.LOOT_BONUS_BLOCKS

    static final Enchantment POWER = Enchantment.ARROW_DAMAGE
    static final Enchantment PUNCH = Enchantment.ARROW_KNOCKBACK
    static final Enchantment FLAME = Enchantment.ARROW_FIRE
    static final Enchantment INFINITY = Enchantment.ARROW_INFINITE

    static final Enchantment LUCK_OF_THE_SEA = Enchantment.LUCK
    static final Enchantment LURE = Enchantment.LURE

    static final Enchantment MENDING = Enchantment.MENDING
    static final Enchantment CURSE_OF_VANISHING = Enchantment.VANISHING_CURSE

}
