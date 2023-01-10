package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.util.logger
import discord4j.core.event.domain.interaction.ButtonInteractionEvent
import discord4j.core.`object`.command.ApplicationCommand
import discord4j.core.`object`.command.ApplicationCommandInteraction
import discord4j.core.`object`.command.Interaction
import discord4j.core.`object`.component.ActionRow
import discord4j.core.`object`.component.Button
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec
import discord4j.core.spec.MessageCreateSpec
import discord4j.discordjson.json.ApplicationCommandRequest
import java.util.UUID

object PingCommand : AbstractCommand() {
    override fun make() {
        create(
            ApplicationCommandRequest.builder()
                .name("ping")
                .description("Pinging bot")
                .build()
        ) { cmd, ev, callback ->
            return@create callback
                .content(
                    "Pong! `ping is: " + (System.currentTimeMillis() - ev.interaction.id.timestamp.toEpochMilli()) + "`"
                )
                .addComponent(
                    ActionRow.of(
                        Button.link("https://clck.ru/3vyXS", "Click me"),
                    )
                )
        }
    }

}