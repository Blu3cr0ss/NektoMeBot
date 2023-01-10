package bot.nektome.nektobot

import bot.nektome.nektobot.discord.DiscordBot
import bot.nektome.nektobot.discord.listener.EventListener
import bot.nektome.nektobot.event.Events
import org.apache.log4j.BasicConfigurator
import kotlin.concurrent.thread

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        thread { Thread.currentThread().join() }
        BasicConfigurator.configure()   //log4j
        //Bot.start()
        DiscordBot.start()
        EventListener.start()
    }
}