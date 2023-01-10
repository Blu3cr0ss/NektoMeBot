package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.socketio.NektoBot
import bot.nektome.nektobot.util.logger
import discord4j.common.util.Snowflake
import discord4j.core.`object`.PermissionOverwrite
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.core.spec.TextChannelCreateSpec
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest
import discord4j.rest.util.Permission
import discord4j.rest.util.PermissionSet

object SetMySexCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("setmysex")
                .description("Declare your sex. Can be NM")
                .addOption(
                    ApplicationCommandOptionData.builder()
                        .name("yoursex")
                        .description("Male, Female or NM")
                        .type(ApplicationCommandOption.Type.STRING.value)
                        .required(true)
                        .build()
                )
                .build()
        ) {cmd, ev, cb ->
            val recievedArgument: String? = ev.interaction.commandInteraction.get().options.getOrNull(0)?.value?.get()?.raw
            try {
                when (recievedArgument){
                "Male" -> {
                    Settings.SearchParameters.mySex = "M"
                    return@create  cb.content("Your sex set to Male")
                }
                "Female" -> {
                    Settings.SearchParameters.mySex= "F"
                    return@create cb.content("Your sex set to Female")
                }
                "NM" -> {
                    Settings.SearchParameters.mySex = null
                    return@create  cb.content("Your Sex is now not declared")
                }
                else -> {
                    return@create cb.content("Invalid argument. Please, use ```/help``` command")
                }
            }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return@create cb.content("Error: ${ex.stackTraceToString()}")
            }
        }
    }
}