/*
 * Copyright Notice for PixelsJumpRemastered
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 28.03.22, 16:48 by Carina The Latest changes made by Carina on 28.03.22, 16:48.
 *  All contents of "Inventories.kt" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package de.carina.pixelsjump.util.inventory

import de.carina.pixelsjump.util.ArenaHelper
import org.bukkit.inventory.Inventory

object Inventories {
    fun starterInventory(): Inventory {
        return InventoryBuilder("Arena Builder", 9).addItem(Items.startLocationItem(), 1).addItem(Items.endLocationItem(), 3).addItem(Items.finishArenaBuildItem(), 8).fillInventory(Items.paneFillerItem()).buildInventory()
    }

    fun arenaInventory(): Inventory {
        var builder = InventoryBuilder("Arenas", (ArenaHelper.arenas.size / 9 + 1) * 9)
        for ((index, arena) in ArenaHelper.arenas.withIndex()) {
            println(arena.name + " name   dfsdfsdss ")
            builder.addItem(Items.arenaItem(arena.name), index).fillInventory(Items.paneFillerItem())
        }

        return builder.buildInventory()
    }
}