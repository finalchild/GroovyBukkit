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

package me.finalchild.groovybukkit.extension

import groovy.time.Duration
import me.finalchild.groovybukkit.GroovyBukkit
import me.finalchild.groovybukkit.delegate.OnCommandDelegate
import me.finalchild.groovybukkit.util.CommandUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Event
import org.bukkit.event.EventException
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.EventExecutor
import org.bukkit.scheduler.BukkitTask

import java.util.Map.Entry

/**
 * Utility methods. Automatically imported in GroovyBukkit scripts.
 */
class Util {

    private Util() {}

    /**
     * Registers the specified executor to the given event class
     *
     * @param event Event type to register
     * @param priority Priority to register this event at
     * @param closure executor closure to register
     */
    static <T extends Event> void on(
            @DelegatesTo.Target Class<T> event, EventPriority priority,
            @DelegatesTo(genericTypeIndex = 0) Closure closure) {
        Bukkit.getPluginManager().registerEvent(event, GroovyBukkit.instance, priority, new EventExecutor() {
            @Override
            void execute(Listener listener, Event eventObj) throws EventException {
                closure.setDelegate(eventObj)
                closure()
            }
        }, GroovyBukkit.instance)
    }

    /**
     * Registers the specified executor to the given event class
     *
     * @param event Event type to register
     * @param closure executor closure to register
     */
    static <T extends Event> void on(
            @DelegatesTo.Target Class<T> event, @DelegatesTo(genericTypeIndex = 0) Closure closure) {
        on(event, EventPriority.NORMAL, closure)
    }

    /**
     * {@code on <Event> do <Closure>} syntax.
     *
     * @param event Event type to register
     * @return syntax handler.
     */
    static <T extends Event> OnHandler<T> on(Class<T> event) {
        return new OnHandler<T>(event)
    }

    static class OnHandler<T extends Event> {

        private final Class<T> event

        OnHandler(Class<T> event) {
            this.event = event
        }

        void run(@DelegatesTo(type = 'T') Closure closure) {
            on(event, closure)
        }

    }

    /**
     * Registers the specified executor to the given event class
     *
     * @param self Event type to register
     * @param closure executor closure to register
     */
    static <T extends Event> void rightShift(Class<T> self, @DelegatesTo(genericTypeIndex = 0) Closure closure) {
        on(self, closure)
    }

    /**
     * Registers a command. Returns true on success; false if name is already
     * taken.
     *
     * @param label the label of the command, without the '/'-prefix.
     * @param closure executor closure to register
     * @return true if command was registered with the passed in label, false
     *     otherwise.
     */
    static boolean onCommand(String label, @DelegatesTo(OnCommandDelegate) Closure closure) {
        CommandUtil.register('gb', new Command(label) {
            @Override
            boolean execute(CommandSender sender, String commandLabel, String[] args) {
                closure.delegate = new OnCommandDelegate(sender: sender, label: commandLabel, args: args)
                closure()
            }
        })
    }

    /**
     * Registers a command. Returns true on success; false if name is already
     * taken.
     *
     * @param self the label of the command, without the '/'-prefix.
     * @param closure executor closure to register
     * @return true if command was registered with the passed in label, false
     *     otherwise.
     */
    static boolean rightShift(String self, @DelegatesTo(OnCommandDelegate) Closure closure) {
        onCommand(self, closure)
    }

    /**
     * Returns a task that will run on the next server tick.
     *
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask runTask(Closure<Void> task) {
        Bukkit.getScheduler().runTask(GroovyBukkit.instance, task)
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously.
     *
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask async(Closure<Void> task) {
        Bukkit.getScheduler().runTaskAsynchronously(GroovyBukkit.instance, task)
    }

    /**
     * Returns a task that will run after the specified number of server
     * ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask later(long delay, Closure<Void> task) {
        Bukkit.getScheduler().runTaskLater(GroovyBukkit.instance, task, delay)
    }

    /**
     * Returns a task that will run after the specified number of server
     * ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask later(Duration delay, Closure<Void> task) {
        later(delay.toMilliseconds() / 50 as long, task)
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously after the specified number
     * of server ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask laterAsync(long delay, Closure<Void> task) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(GroovyBukkit.instance, task, delay)
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously after the specified number
     * of server ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask laterAsync(Duration delay, Closure<Void> task) {
        laterAsync(delay.toMilliseconds() / 50 as long, task)
    }

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask timer(long delay, long period, Closure<Void> task) {
        Bukkit.getScheduler().runTaskTimer(GroovyBukkit.instance, task, delay, period)
    }

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask timer(Duration delay, Duration period, Closure<Void> task) {
        timer(delay.toMilliseconds() / 50 as long, period.toMilliseconds() / 50 as long, task)
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask timerAsync(long delay, long period, Closure<Void> task) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(GroovyBukkit.instance, task, delay, period)
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    static BukkitTask timerAsync(Duration delay, Duration period, Closure<Void> task) {
        timerAsync(delay.toMilliseconds() / 50 as long, period.toMilliseconds() / 50 as long, task)
    }

    /**
     * Returns self
     * @param self
     * @return self
     */
    static <T extends Event> T getEvent(T self) {
        self
    }

    /**
     * {@link ItemStack} builder
     * @param closure
     * @return the ItemStack
     */
    static ItemStack itemStack(@DelegatesTo(ItemStackBuilder) Closure closure) {
        ItemStackBuilder builder = new ItemStackBuilder()
        closure.delegate = builder
        closure()
        builder.build()
    }

    static class ItemStackBuilder {

        Material type
        int amount
        short durability

        String name
        List<String> lore
        Map<Enchantment, Integer> enchantments
        List<ItemFlag> itemFlags
        boolean unbreakable

        void type(Material type) {
            this.type = type
        }

        void amount(int amount) {
            this.amount = amount
        }

        void durability(short durability) {
            this.durability = durability
        }

        void name(String name) {
            this.name = name
        }

        void lore(List<String> lore) {
            this.lore = lore
        }

        void enchantments(Map<Enchantment, Integer> enchantments) {
            this.enchantments = enchantments
        }

        void enchantments(List<Entry<Enchantment, Integer>> enchantments) {
            this.enchantments = enchantments.collectEntries {
                [it.key, it.value]
            }
        }

        void enchantments(@DelegatesTo(EnchantmentsBuilder) Closure closure) {
            EnchantmentsBuilder builder = new EnchantmentsBuilder()
            closure.delegate = builder
            closure()
            enchantments = builder.build()
        }

        void itemFlags(List<ItemFlag> itemFlags) {
            this.itemFlags = itemFlags
        }

        void unbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable
        }

        ItemStack build() {
            ItemStack stack = new ItemStack(type, amount)
            stack.durability = durability

            if (stack.hasItemMeta()) {
                stack.addUnsafeEnchantments enchantments
                ItemMeta meta = stack.itemMeta
                meta.displayName = name
                meta.lore = lore
                meta.addItemFlags itemFlags.toArray() as ItemFlag[]
                stack.itemMeta = meta
            }

            stack
        }

        static class EnchantmentsBuilder {

            Map<Enchantment, Integer> enchantments

            void enchantment(Entry<Enchantment, Integer> enchantment) {
                enchantments[enchantment.key] = enchantment.value
            }

            Map<Enchantment, Integer> build() {
                enchantments
            }

        }

    }
}
