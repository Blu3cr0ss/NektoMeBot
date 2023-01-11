package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object ChangePrefixCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("setprefix")
                .description("Change the prefix using to send messages")
                .addOption(
                    ApplicationCommandOptionData.builder()
                        .name("newprefix")
                        .description("Any symbol")
                        .type(ApplicationCommandOption.Type.STRING.value)
                        .minValue(1.0)
                        .maxValue(99.0)
                        .required(true)
                        .build()
                )
                .build()
        ) { cmd, ev, cb ->
            val recievedArgument: String = ev.interaction.commandInteraction.get().options.getOrNull(0)?.value?.get()?.raw.toString()
            Settings.sendPrefix = recievedArgument
            cb.content("Prefix set to $recievedArgument")
        }
    }
}