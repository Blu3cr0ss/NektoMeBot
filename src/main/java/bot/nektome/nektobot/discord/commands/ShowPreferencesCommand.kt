package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object ShowPreferencesCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("showpreferences")
                .description("Display all selected values")
                .build()
        ) { cmd, ev, cb ->
            return@create cb.content("Your sex: ${Settings.SearchParameters.mySex} \n" +
                    "Your age diapason: ${Settings.SearchParameters.myAge[0]} - ${Settings.SearchParameters.myAge[1]} \n" +
                    "Preferred sex: ${Settings.SearchParameters.wishSex} \n" +
                    "Preferred age diapason: ${Settings.SearchParameters.wishAge.get(0)[0]} - ${Settings.SearchParameters.wishAge.get(0)[1]} \n" +
                    "Use ```/search``` to start a chat with these parameters")
        }
    }
}