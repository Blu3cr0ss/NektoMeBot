package bot.nektome.nektobot.util

import bot.nektome.nektobot.discord.DiscordBot

object D4J {
    fun deleteCommand(name: String) {
        val discordCommands =
            DiscordBot.gateway.restClient.getApplicationService().getGuildApplicationCommands(
                DiscordBot.gateway.applicationInfo.block().id.asLong(),
                DiscordBot.myGuild
            ).toTypedArray()
        discordCommands.filter {
            name == it.name()
        }.forEach {
            DiscordBot.gateway.restClient.applicationService.deleteGuildApplicationCommand(
                DiscordBot.gateway.applicationInfo.block().id.asLong(),
                DiscordBot.myGuild,
                it.id().asLong()
            ).block()
        }
    }

    fun deleteCommands(cmds: Array<String>) {
        val discordCommands =
            DiscordBot.gateway.restClient.getApplicationService().getGuildApplicationCommands(
                DiscordBot.gateway.applicationInfo.block().id.asLong(),
                DiscordBot.myGuild
            ).toTypedArray()
        discordCommands.filter {
            cmds.contains(it.name())
        }.forEach {
            DiscordBot.gateway.restClient.applicationService.deleteGuildApplicationCommand(
                DiscordBot.gateway.applicationInfo.block().id.asLong(),
                DiscordBot.myGuild,
                it.id().asLong()
            ).block()
        }
    }
}