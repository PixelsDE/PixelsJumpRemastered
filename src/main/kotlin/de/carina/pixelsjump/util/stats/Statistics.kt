/*
 * Copyright Notice for PixelsJumpRemastered
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 28.03.22, 14:01 by Carina The Latest changes made by Carina on 28.03.22, 14:01.
 *  All contents of "Statistics.kt" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package de.carina.pixelsjump.util.stats

import de.carina.pixelsjump.PixelsJump
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

object Statistics {
    data class PlayerStats(var uuid: UUID, var games: Int = 0, var points: Int = 0, var fails: Int = 0, var wins: Int = 0)

    private const val path = "plugins/PixelsJumpRemastered/statistics.yml"
    private lateinit var file: File
    lateinit var ymlConfiguration: YamlConfiguration
    val statistics = mutableListOf<PlayerStats>()
    private fun loadPlayers() {
        for (player in ymlConfiguration.getKeys(false)) {
            statistics.add(PlayerStats(UUID.fromString(player), ymlConfiguration.getInt("$player.games"), ymlConfiguration.getInt("$player.points"), ymlConfiguration.getInt("$player.fails"), ymlConfiguration.getInt("$player.wins")))
            Bukkit.getConsoleSender().sendMessage(
                PixelsJump.utility.messageConverter("stats-loaded").replace("%player%", Bukkit.getOfflinePlayers().firstOrNull { it.uniqueId.equals(player) }?.name ?: "unknown")
            )
        }

    }

    fun loadStats() {
        PixelsJump.utility.sendMessage("loading-statistics-start")
        file = File(path)
        ymlConfiguration = YamlConfiguration.loadConfiguration(file)
        saveStatsFile()
        loadPlayers()
        PixelsJump.utility.sendMessage("loading-statistics-end")
    }


    fun addStats(player: Player) {
        val statsPlayer = getStatsPlayer(player.uniqueId)!!
        ymlConfiguration.set("${player.uniqueId}.games", statsPlayer.games)
        ymlConfiguration.set("${player.uniqueId}.points", statsPlayer.points)
        ymlConfiguration.set("${player.uniqueId}.fails", statsPlayer.fails)
        ymlConfiguration.set("${player.uniqueId}.wins", statsPlayer.wins)

        saveStatsFile()
    }

    private fun getStatsPlayer(uuid: UUID): PlayerStats? {
        statistics.forEach {
            if (it.uuid == uuid) {
                return it
            }
        }
        return null
    }

    private fun saveStatsFile() {
        ymlConfiguration.options().copyDefaults(true)
        ymlConfiguration.save(file)

    }

    fun joinArena(player: Player) {
        val statsPlayer = getStatsPlayer(player.uniqueId)!!
        statsPlayer.games++
        ymlConfiguration.set("${player.uniqueId}.games", statsPlayer.games)
        saveStatsFile()
    }

    fun addFail(player: Player) {
        val playerStats = getStatsPlayer(player.uniqueId)
        if (playerStats != null) {
            playerStats.fails++
            ymlConfiguration.set("${player.uniqueId}.fails", playerStats.fails)
            saveStatsFile()
        }
    }

    fun addPoints(player: Player, amount: Int) {
        val playerStats = getStatsPlayer(player.uniqueId)
        if (playerStats != null) {
            playerStats.points += amount
            ymlConfiguration.set("${player.uniqueId}.points", playerStats.points)
            saveStatsFile()
        }
    }

    fun addWin(player: Player) {
        val playerStats = getStatsPlayer(player.uniqueId)!!
        if (playerStats != null) {
            playerStats.wins++
            playerStats.points += 5
            ymlConfiguration.set("${player.uniqueId}.wins", playerStats.wins)
            ymlConfiguration.set("${player.uniqueId}.points", playerStats.points)
            saveStatsFile()
        }

    }
}
