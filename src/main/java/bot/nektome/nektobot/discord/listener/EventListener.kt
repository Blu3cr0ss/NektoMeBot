package bot.nektome.nektobot.discord.listener

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.event.Events

object EventListener {
    fun start(){
        Events.DIALOG_STARTED.addListener{
            Settings.lastChannel!!.block()!!.createMessage("Chat found! ${it.dialogId}")
        }
        Events.MESSAGE_RECEIVED.addListener{
            Settings.lastChannel!!.block()!!.createMessage("Strangers sent: ${it.data}")
        }
    }
}