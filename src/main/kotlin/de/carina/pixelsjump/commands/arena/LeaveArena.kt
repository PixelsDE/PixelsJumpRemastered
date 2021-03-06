/*
 * Copyright Notice for PixelsJumpRemastered
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 29.03.22, 11:36 by Carina The Latest changes made by Carina on 29.03.22, 11:36.
 *  All contents of "LeaveArena.kt" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package de.carina.pixelsjump.commands.arena

import de.carina.pixelsjump.PixelsJump
import de.carina.pixelsjump.util.BlockGenerator
import de.carina.pixelsjump.util.arena.ArenaHelper
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LeaveArena(private val sender: CommandSender, private val command: Command, private val args: Array<out String>) {

    fun execute() {
        if (!PixelsJump.utility.preCommandStuff(sender, command, args, 1, "leave", "pixelsjump.leave")) return
        if (!ArenaHelper.playersInArenas.contains(sender)) {
            sender.sendMessage(PixelsJump.utility.messageConverter("no-jump"))
            return
        }

        Bukkit.getOnlinePlayers().forEach {
            if (it != sender) {
                it.showPlayer(PixelsJump.instance, sender as Player)
            }
        }
        (sender as Player).playerListName(Component.text(sender.name))
        ArenaHelper.playersInArenas.remove(sender)
        BlockGenerator.playerCheckpoints.remove(sender)
        BlockGenerator.playerBlock.remove(sender)
        BlockGenerator.playerJumpBlocks[sender]!!.forEach {
            it.type = Material.AIR
        }
        BlockGenerator.playerJumpBlocks.remove(sender)
        sender.teleport(ArenaHelper.arenas.find { it.players.contains(sender) }!!.locations[2] as Location)
        sender.inventory.clear()
        sender.sendMessage(PixelsJump.utility.messageConverter("arena-leave").replace("%arena%", ArenaHelper.arenas.find { it.players.contains(sender) }!!.name))
    }
}
