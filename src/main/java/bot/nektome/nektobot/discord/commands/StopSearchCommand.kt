package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.discord.DiscordBot
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object StopSearchCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("stopsearch")
                .description("Stop search a chat-mate")
                .build()
        ) { cmd, ev, cb ->
            DiscordBot.nektobot.stopSearch()
            return@create cb.content("Stoping..")
        }
    }
}