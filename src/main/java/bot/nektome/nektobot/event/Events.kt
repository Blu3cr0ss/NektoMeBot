package bot.nektome.nektobot.event

import com.pploder.events.SimpleEvent

object Events {
    val LOGIN = SimpleEvent<LoginedEvent>()
    val DIALOG_STARTED = SimpleEvent<FoundDialogEvent>()
    val TYPING = SimpleEvent<TypingEvent>()
    val SEARCH_STARTED = SimpleEvent<StartSearchForDialogEvent>()
    val DIALOG_ENDED = SimpleEvent<DialogClosedEvent>()
    val ERROR = SimpleEvent<ServerReturnedErrorEvent>()
    val MESSAGES_READ = SimpleEvent<MessagesReadEvent>()
    val MESSAGE_RECEIVED = SimpleEvent<MessageReceivedEvent>()
}