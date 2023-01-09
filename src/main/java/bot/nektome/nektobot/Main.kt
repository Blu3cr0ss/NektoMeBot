package bot.nektome.nektobot

import org.apache.log4j.BasicConfigurator

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        BasicConfigurator.configure()   //log4j
        Bot.start()
    }
}