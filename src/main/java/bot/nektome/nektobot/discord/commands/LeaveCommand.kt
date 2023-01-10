package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.discord.DiscordBot
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object LeaveCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("leave")
                .description("Finish the dialog")
                .build()
        ) { cmd, ev, cb ->
            DiscordBot.nektobot.leaveChat()
            return@create cb.content("You finished chat")
        }
    }
}