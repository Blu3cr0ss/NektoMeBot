package bot.nektome.nektobot.discord.commands

import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object HelpCommand : AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("help")
                .description("Information about bot")
                .build()
        ) { cmd, ev, cb ->
            return@create cb.content(
                "NektoMeBot is a Discord bot which brings Nekto.Me anonymous chat to discord. Usage is quite simple. \n" +
                        "First of all create a room for chatting by ```/createroom```" +
                        "Before chatting you should clarify information about you and preferred chat-mate" +
                        "There are 4 parameters, all of them are visible by ```/showpreferences```" +
                        "And may be changed by ```/setmysex``````/setmyage``````/setwishsex``````/setwishage```  " +
                        "So, when everything is ready, start searching with ```/startsearch``` searching can be canceled by ```/stopsearch```" +
                        "Every message sent by your chat-mate will be displayed in created channel, to send message yourself use ```>*your text*``` " +
                        "Once you want to end chat use ```/leave``` command"
            )
        }
    }
}