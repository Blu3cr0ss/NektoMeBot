package bot.nektome.nektobot


import bot.nektome.nektobot.socketio.Connection
import bot.nektome.nektobot.util.logger
import kotlin.concurrent.thread

object Bot {
    fun start() {
        Connection.track()
        Connection.startSearch(
            Settings.Sex.MALE,
            Settings.Sex.FEMALE,
            Settings.Ages.UNDER17,
            Settings.Ages.UNDER17
        )
    }
}