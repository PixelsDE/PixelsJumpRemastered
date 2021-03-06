/*
 * Copyright Notice for PixelsJumpRemastered
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 29.03.22, 16:45 by Carina The Latest changes made by Carina on 29.03.22, 16:45.
 *  All contents of "PlayerMovesInJumpNRun.kt" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package de.carina.pixelsjump.events

import de.carina.pixelsjump.PixelsJump
import de.carina.pixelsjump.util.BlockGenerator
import de.carina.pixelsjump.util.arena.Arena
import de.carina.pixelsjump.util.arena.ArenaHelper
import de.carina.pixelsjump.util.files.Configuration
import de.carina.pixelsjump.util.stats.Statistics
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMovesInJumpnRun : Listener {

    @EventHandler
    fun playerMovesInJumpNRun(event: PlayerMoveEvent) {
        if (!event.player.hasPermission("pixelsjump.jumpnrun")) return

        if (!ArenaHelper.playersInArenas.contains(event.player)) return
        val arena = ArenaHelper.arenas.find { it.players.contains(event.player) }!!

        if (!failChecker(arena, event)) return
        if (!blockRelated(event)) return
        if (!locationRelated(arena, event)) return


        if (arena.single == true) {
            if (event.player.location.block.getRelative(BlockFace.DOWN).type == BlockGenerator.playerBlock[event.player]!!.type && event.player.location.block.getRelative(BlockFace.DOWN).location.toCenterLocation() == BlockGenerator.playerBlock[event.player]!!.location.toCenterLocation()) {
                BlockGenerator.generateBlock(event.player)
                Statistics.addPoints(event.player, Configuration.pointsPerJump)
            }
        }

    }

    private fun blockRelated(event: PlayerMoveEvent): Boolean {
        if (event.player.location.block.getRelative(BlockFace.DOWN).type == BlockGenerator.checkPointMaterial && BlockGenerator.playerCheckpoints[event.player]!!.toCenterLocation() != event.player.location.block.getRelative(BlockFace.DOWN).location.toCenterLocation()) {
            BlockGenerator.playerCheckpoints[event.player] = event.player.location.block.getRelative(BlockFace.DOWN).location.toCenterLocation().add(0.0, 1.0, 0.0)
            event.player.sendMessage(PixelsJump.utility.messageConverter("arena-checkpoint-reached").replace("%arena%", ArenaHelper.arenas.find { it.players.contains(event.player) }!!.name))
            return false
        } else if (event.player.location.block.getRelative(BlockFace.DOWN).type == BlockGenerator.endPointFinish && BlockGenerator.playerCheckpoints[event.player]!!.toCenterLocation() != event.player.location.block.getRelative(BlockFace.DOWN).location.toCenterLocation()) {
            jumpWon(event)
            return false
        }
        return true
    }

    private fun jumpWon(event: PlayerMoveEvent): Boolean {
        Statistics.addWin(event.player)
        event.player.sendMessage(PixelsJump.utility.messageConverter("arena-goal-reached").replace("%arena%", ArenaHelper.arenas.find { it.players.contains(event.player) }!!.name))
        event.player.performCommand("pixelsjump leave")
        return false
    }

    private fun locationRelated(arena: Arena, event: PlayerMoveEvent): Boolean {
        if (arena.checkPoints.size == arena.checkPoints.indexOf(BlockGenerator.playerCheckpoints[event.player]!!) + 1) {
            jumpWon(event)
            return false
        } else if (event.player.location.block.getRelative(BlockFace.DOWN).location.toCenterLocation() == arena.checkPoints[arena.checkPoints.indexOf(BlockGenerator.playerCheckpoints[event.player]!!) + 1].toCenterLocation()) {
            BlockGenerator.playerCheckpoints[event.player] = arena.checkPoints[arena.checkPoints.indexOf(BlockGenerator.playerCheckpoints[event.player]!!) + 1]
            event.player.sendMessage(PixelsJump.utility.messageConverter("arena-checkpoint-reached").replace("%arena%", ArenaHelper.arenas.find { it.players.contains(event.player) }!!.name))
            return false
        }
        return true
    }

    private fun failChecker(arena: Arena, event: PlayerMoveEvent): Boolean {
        if (arena.single == true) {
            if (event.player.location.block.getRelative(BlockFace.DOWN).location.y < BlockGenerator.playerBlock[event.player]!!.location.y - 2) {
                event.player.sendMessage(PixelsJump.utility.messageConverter("arena-player-failed").replace("%arena%", ArenaHelper.arenas.find { it.players.contains(event.player) }!!.name))
                Statistics.addFail(event.player)
                event.player.performCommand("pixelsjump leave")
                return false
            }
        } else {
            if (event.player.location.block.getRelative(BlockFace.DOWN).location.y < BlockGenerator.playerCheckpoints[event.player]!!.y - 2) {
                event.player.sendMessage(PixelsJump.utility.messageConverter("arena-player-fell").replace("%arena%", ArenaHelper.arenas.find { it.players.contains(event.player) }!!.name))
                Statistics.addFail(event.player)
                event.player.teleport(BlockGenerator.playerCheckpoints[event.player]!!)
                return false
            }
        }
        return true
    }
}
