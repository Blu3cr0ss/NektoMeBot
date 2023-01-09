package bot.nektome.nektobot

import bot.nektome.nektobot.util.logger
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import kotlin.system.exitProcess

class DiscordBot: ListenerAdapter() {
    fun main(args: Array<String>){
        if (args.isEmpty()) {
            logger.info("You have to provide a token as first argument!");
            exitProcess(1);
        }

        val jda = JDABuilder.createLight(args[0], emptyList()).addEventListeners(DiscordBot()).setActivity(Activity.playing("NektoMe")).build()
        jda.awaitReady()
        jda.updateCommands().addCommands(Commands.slash("ping", "Calculate ping of the bot"))
    }
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent){
        if (event.name == "ping"){
            val time = System.currentTimeMillis()
            event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap { _ -> event.hook.editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)}
                .queue()
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        var author = event.author
        var message = event.message
        System.out.println("$author sent $message")
    }
}