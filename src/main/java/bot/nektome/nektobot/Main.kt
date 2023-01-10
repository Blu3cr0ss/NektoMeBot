package bot.nektome.nektobot

import bot.nektome.nektobot.discord.DiscordBot
import org.apache.log4j.BasicConfigurator
import kotlin.concurrent.thread

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        thread { Thread.currentThread().join() }
        BasicConfigurator.configure()   //log4j
        DiscordBot.start()
    }
}