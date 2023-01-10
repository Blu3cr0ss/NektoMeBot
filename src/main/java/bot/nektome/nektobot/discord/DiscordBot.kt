package bot.nektome.nektobot.discord

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.commands.*
import bot.nektome.nektobot.socketio.NektoBot
import discord4j.core.DiscordClient

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
        SetMySexCommand
        SetWishSexCommand
        SetMyAgeCommand
        SetWishAgeCommand
        ShowPreferencesCommand
        StartSearchCommand
    }

    fun setupNektoBot() {
        nektobot.events.DIALOG_STARTED.addListener {
            Settings.inChannel?.createMessage("Chat found! ${it.dialogId}")?.block()
        }
        nektobot.events.MESSAGE_RECEIVED.addListener {
            Settings.inChannel?.createMessage("Strangers sent: ${it.data}")?.block()
        }
    }
}