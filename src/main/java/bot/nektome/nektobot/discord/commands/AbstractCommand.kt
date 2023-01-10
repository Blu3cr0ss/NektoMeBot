package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.util.logger
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent
import discord4j.core.`object`.command.ApplicationCommandInteraction
import discord4j.core.`object`.entity.Message
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec
import discord4j.core.spec.MessageCreateSpec
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

abstract class AbstractCommand {
    companion object {
        @JvmStatic
        val listCommands = arrayListOf<AbstractCommand>()
    }

    lateinit var cmd: ImmutableApplicationCommandRequest
    lateinit var lambda: (ImmutableApplicationCommandRequest, ApplicationCommandInteractionEvent, InteractionApplicationCommandCallbackSpec.Builder) -> InteractionApplicationCommandCallbackSpec.Builder


    init {
        listCommands.add(this)
        make()
        DiscordBot.gateway.restClient.applicationService
            .createGuildApplicationCommand(
                DiscordBot.gateway.restClient.applicationId.block(),
                DiscordBot.myGuild,
                cmd
            )
            .subscribe();
    }

    protected fun create(
        cmd: ImmutableApplicationCommandRequest,
        lambda: (ImmutableApplicationCommandRequest, ApplicationCommandInteractionEvent, InteractionApplicationCommandCallbackSpec.Builder) -> InteractionApplicationCommandCallbackSpec.Builder

    ) {
        this.cmd = cmd
        this.lambda = lambda

        DiscordBot.gateway.on(ApplicationCommandInteractionEvent::class.java).subscribe {
            if (it.commandName == cmd.name()) {
                it.reply(lambda.invoke(cmd, it, InteractionApplicationCommandCallbackSpec.builder()).build()).block()
            }
        }
    }

    abstract fun make()
}