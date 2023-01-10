package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.util.logger
import discord4j.common.util.Snowflake
import discord4j.core.`object`.PermissionOverwrite
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.core.spec.TextChannelCreateSpec
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest
import discord4j.rest.util.Permission
import discord4j.rest.util.PermissionSet

object CreateRoomCommand : AbstractCommand() {
    var rooms = 0
    val createdRoomsIDs = arrayListOf<Long>()
    
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("createroom")
                .description("Create room for talk")
                .addOption(
                    ApplicationCommandOptionData.builder()
                        .name("room_members")
                        .description("Users who will join this room (all users will join if not specified)")
                        .type(ApplicationCommandOption.Type.STRING.value)
                        .required(false)
                        .build()
                )
                .build()
        ) { cmd, ev, cb ->
            try {
                val users =
                    ev.interaction.commandInteraction.get().options.getOrNull(0)?.value?.get()?.raw
                        ?.replace(" ", "")
                        ?.replace("><@", "> <@")
                        ?.split(" ")
                        ?.distinct()
                        ?.map {
                            logger.info(it)
                            ev.interaction.guild.block().getMemberById(Snowflake.of(it.removeSurrounding("<@", ">")))
                                .block()
                        }

                val membersPerms: List<PermissionOverwrite>?
                if (users != null) {
                    membersPerms = users?.map {
                        PermissionOverwrite.forMember(
                            it.id,
                            PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES),
                            PermissionSet.none()
                        )
                    }
                } else {
                    membersPerms = listOf(
                        PermissionOverwrite.forRole(
                            ev.interaction.guild.block().id,
                            PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES),
                            PermissionSet.none()
                        )
                    )
                }


                val name = "nekto-me-room-" + rooms + "_" +
                        (1..5).map { (('a'..'z') + ('A'..'Z') + ('0'..'9')).random() }
                            .joinToString("")
                val room = DiscordBot.gateway.getGuildById(ev.interaction.guild.block().id).block()
                    .createTextChannel(
                        TextChannelCreateSpec.builder()
                            .name(name)
                            .addPermissionOverwrite(
                                PermissionOverwrite.forRole(
                                    ev.interaction.guild.block().id,
                                    PermissionSet.none(),
                                    PermissionSet.all(),
                                )
                            )
                            .addAllPermissionOverwrites(
                                membersPerms
                            )
                            .build()
                    ).block()
                rooms++;
                createdRoomsIDs.add(room.id.asLong())
                Settings.inChannel = room
                room.createMessage("Hello! This is room created by NektoMeBot. Type '/help' to get tutorial how to start chatting")
                    .block()

                return@create cb.content("Room <#" + room.id.asLong() + "> created")
            } catch (e: Exception) {
                return@create cb.content("Error! \n" + e.stackTraceToString())
            }
        }
    }
}