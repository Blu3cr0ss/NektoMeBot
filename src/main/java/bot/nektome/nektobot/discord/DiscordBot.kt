package bot.nektome.nektobot.discord

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.commands.*
import bot.nektome.nektobot.socketio.NektoBot
import bot.nektome.nektobot.util.logger
import bot.nektome.nektobot.util.toTypedArray
import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.discordjson.json.ApplicationCommandData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.random.Random


object DiscordBot {
    val client = DiscordClient.create(Settings.botToken);
    val gateway = client.login().block()!!;
    val myGuild = 972570408844947466
    val nektobot = NektoBot("146838d71244b62ff0c804ea346ea5a4c6f37e81af7e652b5f87ec41dc46c4e7").start()
    var inChannel: MessageChannel? = null
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
        HelpCommand
        SendCommand
        LeaveCommand
        StopSearchCommand
    }

    val typingCoroutineThread = newSingleThreadContext("TypingThread")
    fun setupNektoBot() {
        suspend fun humanizedTyping() {
            while (nektobot.isInDialog) {
                val secsToType = Random.nextLong(1, 5)
                val secsToStop = Random.nextLong(1, 5)
                nektobot.setTyping(true)
                delay(secsToType)
                nektobot.setTyping(false)
                delay(secsToStop)
            }
        }
        nektobot.events.DIALOG_STARTED.addListener {
            inChannel?.createMessage("Chat found! ${it.dialogId}")?.block()
            GlobalScope.launch(context = typingCoroutineThread) {
                humanizedTyping()
            }
        }
        nektobot.events.MESSAGE_RECEIVED.addListener {
            if (it.uuid != nektobot.lastMessageUUID) inChannel?.createMessage("_**==> ${it.data["message"]}**_")
                ?.block()
        }
        nektobot.events.DIALOG_ENDED.addListener {
            inChannel?.createMessage("Chat ended.")?.block()
        }
    }
}