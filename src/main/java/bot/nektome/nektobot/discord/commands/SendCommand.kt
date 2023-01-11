package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.socketio.NektoBot
import bot.nektome.nektobot.util.logger
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object SendCommand {
    init {
        DiscordBot.gateway.on(MessageCreateEvent::class.java).subscribe { ev ->
            if (ev.message.content.startsWith(">") &&
                ev.message.channel.block().id.asLong() == DiscordBot.inChannel?.id?.asLong()
            ) {
                val recievedArgument =
                    ev.message.content.split(">", limit = 2)[1]
                logger.info("received arg: $recievedArgument")
                if (DiscordBot.nektobot.isInDialog) {
                    DiscordBot.nektobot.sendMsg(recievedArgument)
//                    ev.message.channel.block().createMessage("You sent: $recievedArgument").block()
                } else
                    ev.message.channel.block().createMessage("You haven't started any dialog").block()
            }
        }
    }
}