package bot.nektome.nektobot.discord

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.commands.CreateRoomCommand
import bot.nektome.nektobot.discord.commands.DeleteLastRoomCommand
import bot.nektome.nektobot.discord.commands.PingCommand
import bot.nektome.nektobot.socketio.NektoBot
import bot.nektome.nektobot.util.logger
import discord4j.core.DiscordClient
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent

object DiscordBot {
    val client = DiscordClient.create(Settings.botToken);
    val gateway = client.login().block()!!;
    val myGuild = 972570408844947466
    val nektobot = NektoBot("146838d71244b62ff0c804ea346ea5a4c6f37e81af7e652b5f87ec41dc46c4e7").start()
    fun start() {
        setupNektoBot()
        // INIT COMMANDS
        PingCommand
        CreateRoomCommand
        DeleteLastRoomCommand
    }

    fun setupNektoBot() {

    }
}