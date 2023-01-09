package bot.nektome.nektobot


import bot.nektome.nektobot.event.Events
import bot.nektome.nektobot.event.TypingEvent
import bot.nektome.nektobot.socketio.NektoBot
import bot.nektome.nektobot.util.logger

object Bot {
    fun start() {   //little example
        val bot =
            NektoBot("146838d71244b62ff0c804ea346ea5a4c6f37e81af7e652b5f87ec41dc46c4e7").start() //create & start bot
        bot.startSearch(    //search for chat
            Settings.Sex.MALE,
            Settings.Ages.UNDER17,
            Settings.Ages.UNDER17
        )
        Events.DIALOG_STARTED.addListener {//say hello when chat is found
            bot.sendMsg("Привет")
        }
        Thread.sleep(5000L) //wait 5 seconds
        bot.leaveChat() //leave chat
    }
}