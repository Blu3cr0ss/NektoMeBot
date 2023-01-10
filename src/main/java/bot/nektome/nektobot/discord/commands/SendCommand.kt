package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.socketio.NektoBot
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object SendCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("send")
                .description("Send a message to opened dialog")
                .addOption(
                    ApplicationCommandOptionData.builder()
                        .name("text")
                        .description("Text to be sent")
                        .type(ApplicationCommandOption.Type.STRING.value)
                        .required(true)
                        .build()
                )
                .build()
        ) { cmd, ev, cb ->
            val recievedArgument: String? =
                ev.interaction.commandInteraction.get().options.getOrNull(0)?.value?.get()?.raw
            if (Settings.isDialogOpened) {
                DiscordBot.nektobot.sendMsg(recievedArgument!!)
                return@create cb.content("You sent: $recievedArgument")
            }
            else return@create cb.content("You haven't started any dialog")
            return@create cb.content("")
        }
    }
}