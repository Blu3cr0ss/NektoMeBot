package bot.nektome.nektobot

import org.apache.log4j.BasicConfigurator
import java.net.URL
import kotlin.concurrent.thread

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        thread { Thread.currentThread().join() }
        BasicConfigurator.configure()   //log4j
        //Bot.start()
        DiscordBot().start(Settings.botToken)
    }
}