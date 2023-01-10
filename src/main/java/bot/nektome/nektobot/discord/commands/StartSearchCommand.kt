package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.socketio.NektoBot
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object StartSearchCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("startsearch")
                .description("Start search a chat-mate")
                .build()
        ) { cmd, ev, cb ->
            return@create cb.content("Bebra")
        }
    }
}