package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object SetWishSexCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("setwishsex")
                .description("Set preferred sex of chat-mate. Can be NM")
                .addOption(
                    ApplicationCommandOptionData.builder()
                        .name("wishsex")
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
                        Settings.SearchParameters.wishSex = "M"
                        return@create  cb.content("Wished sex set to Male")
                    }
                    "Female" -> {
                        Settings.SearchParameters.wishSex= "F"
                        return@create cb.content("Wished sex set to Female")
                    }
                    "NM" -> {
                        Settings.SearchParameters.wishSex = null
                        return@create  cb.content("Wished sex is now not declared")
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