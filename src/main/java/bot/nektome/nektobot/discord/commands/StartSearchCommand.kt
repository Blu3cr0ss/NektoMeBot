package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings.SearchParameters.wishAge
import bot.nektome.nektobot.Settings.SearchParameters.wishSex
import bot.nektome.nektobot.Settings.SearchParameters.myAge
import bot.nektome.nektobot.Settings.SearchParameters.mySex
import bot.nektome.nektobot.discord.DiscordBot.nektobot
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object StartSearchCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("startsearch")
                .description("Start search a chat-mate")
                .build()
        ) { cmd, ev, cb ->

            if (!mySex.isNullOrEmpty() && !wishSex.isNullOrEmpty()){
                nektobot.startSearch(mySex!!, wishSex!!, myAge, wishAge)
            } else if (!mySex.isNullOrEmpty() && wishSex.isNullOrEmpty()){
                nektobot.startSearch(mySex!!, myAge, wishAge)
            } else {
                return@create cb.content("Problem with search arguments")
            }

            return@create cb.content("Searching for chat-mate...")
        }
    }
}