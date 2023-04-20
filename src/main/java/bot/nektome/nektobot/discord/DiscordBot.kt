package bot.nektome.nektobot.discord

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.commands.*
import bot.nektome.nektobot.event.TypingEvent
import bot.nektome.nektobot.socketio.NektoBot
import bot.nektome.nektobot.util.logger
import discord4j.core.DiscordClient
import discord4j.core.event.domain.channel.TypingStartEvent
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.entity.channel.MessageChannel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
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
        ChangePrefixCommand
    }

    fun makeDiscordBotTyping(channel: MessageChannel?, shouldType: Boolean) {
        var isTyping = shouldType
        channel?.typeUntil(
            Mono.fromRunnable<Void> { isTyping = false }
        )?.subscribe()
    }

    fun makeNektoTyping(event: TypingStartEvent, channel: MessageChannel?, users: Array<User>? = null) {
        if (!nektobot.isInDialog) return
        val channelId = channel?.id?.asLong() ?: return
        if (event.channelId.asLong() != channelId) return
        if (users != null && event.userId.asLong() !in users.map { it.id.asLong() }) return
        if (event.userId.asLong() == client.self.block().id().asLong()) return

        nektobot.setTyping(true)

        val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
        scheduler.schedule({
            nektobot.setTyping(false)
            scheduler.shutdown()
        }, 10, TimeUnit.SECONDS)
    }

    fun setupNektoBot() {
        nektobot.events.DIALOG_STARTED.addListener {
            inChannel?.createMessage("Chat found! ${it.dialogId}")?.block()
        }
        nektobot.events.MESSAGE_RECEIVED.addListener {
            if (it.uuid != nektobot.lastMessageUUID) inChannel?.createMessage("_**==> ${it.data["message"]}**_")
                ?.block()
        }
        nektobot.events.DIALOG_ENDED.addListener {
            inChannel?.createMessage("Chat ended.")?.block()
        }
        nektobot.events.TYPING.addListener {
            if (it is TypingEvent.Start) {
                makeDiscordBotTyping(inChannel, true)
            } else {
                makeDiscordBotTyping(inChannel, false)
            }
        }
        gateway.on(TypingStartEvent::class.java).subscribe {
            makeNektoTyping(it, inChannel)
        }
    }
}