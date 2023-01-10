package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import discord4j.common.util.Snowflake
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object DeleteLastRoomCommand : AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("deleteroom")
                .description("delete last N rooms (1 by default)")
                .addOption(
                    ApplicationCommandOptionData.builder()
                        .name("count")
                        .description("how much rooms i should delete")
                        .type(ApplicationCommandOption.Type.INTEGER.value)
                        .required(false)
                        .build()
                ).build()
        )
        { cmd, ev, cb ->
            var count = ev.interaction.commandInteraction.get().options.getOrNull(0)?.let {
                it.value.get().asLong().toInt()
            }
            if (count == null) count = 1
            if (count > CreateRoomCommand.rooms) count = CreateRoomCommand.rooms
            CreateRoomCommand.createdRoomsIDs.takeLast(count).forEach {
                ev.interaction.guild.block().getChannelById(Snowflake.of(it)).block().delete().block()
                CreateRoomCommand.rooms--
            }
            Settings.inChannel = null
            return@create cb.content("Deleted " + count + " rooms")
        }
    }


}