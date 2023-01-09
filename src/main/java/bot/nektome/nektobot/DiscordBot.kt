package bot.nektome.nektobot

import bot.nektome.nektobot.util.logger
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import kotlin.system.exitProcess

class DiscordBot: ListenerAdapter() {
    public fun main(args: Array<String>){
        if (args.isEmpty()) {
            logger.info("You have to provide a token as first argument!");
            exitProcess(1);
        }

        val jda = JDABuilder.createLight(args[0], emptyList()).addEventListeners(DiscordBot()).setActivity(Activity.playing("NektoMe")).build()
        jda.updateCommands().addCommands(Commands.slash("ping", "Calculate ping of the bot"))


        @Override
        fun onSlashCommandInteraction(event: SlashCommandInteractionEvent){
            if (event.name == "ping"){
                val time = System.currentTimeMillis()
                event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                    .flatMap { v -> event.hook.editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)}
                    .queue()
            }
        }
    }
}