package bot.nektome.nektobot.discord.commands
import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.util.logger
import discord4j.core.event.domain.message.MessageCreateEvent

object SendCommand {
    init {
        DiscordBot.gateway.on(MessageCreateEvent::class.java).subscribe { ev ->
            if (ev.message.content.startsWith(Settings.sendPrefix) &&
                ev.message.channel.block().id.asLong() == DiscordBot.inChannel?.id?.asLong()
            ) {
                try {
                    val recievedArgument =
                        ev.message.content.split(">", limit = 2)[1]
                    logger.info("received arg: $recievedArgument")
                    if (DiscordBot.nektobot.isInDialog) {
                        DiscordBot.nektobot.sendMsg(recievedArgument)
//                    ev.message.channel.block().createMessage("You sent: $recievedArgument").block()
                    } else
                        ev.message.channel.block().createMessage("You haven't started any dialog").block()
                } catch (ex: Exception){
                    ev.message.channel.block().createMessage("Error - invalid message")
                    throw (ex)
                }
            }
        }
    }
}