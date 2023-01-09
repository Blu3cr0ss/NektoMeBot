package bot.nektome.nektobot

import bot.nektome.nektobot.socketio.Connection
import bot.nektome.nektobot.util.logger
import org.apache.log4j.BasicConfigurator
import kotlin.concurrent.thread

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        BasicConfigurator.configure()   //log4j
        Connection.start("146838d71244b62ff0c804ea346ea5a4c6f37e81af7e652b5f87ec41dc46c4e7")
        Bot.start()
    }
}