package bot.nektome.nektobot

import bot.nektome.nektobot.util.logger
import discord4j.core.DiscordClient
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.spec.MessageCreateSpec

class DiscordBot {
    fun start(token: String) {
        val client = DiscordClient.create(token);
        val gateway = client.login().block();
        gateway.on(ReadyEvent::class.java).subscribe {
            logger.info("Logged in as " + it.self.username + "#" + it.self.discriminator)
        }
        gateway.on(MessageCreateEvent::class.java).subscribe {
            if (it.message.content == ".ping") {
                val ping = System.currentTimeMillis() - it.message.timestamp.toEpochMilli()
                logger.info(ping)
            }
        }
    }
}