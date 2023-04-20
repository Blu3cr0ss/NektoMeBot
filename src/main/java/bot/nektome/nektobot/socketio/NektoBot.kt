package bot.nektome.nektobot.socketio

import bot.nektome.nektobot.event.*
import bot.nektome.nektobot.util.logger
import com.pploder.events.SimpleEvent
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONString
import java.util.*


class NektoBot(val token: String) {
    class Settings {
        var AUTO_READ = true
        var LOG_UNKNOWN = true
    }

    val events = Events
    var isInDialog = false
    var lastMessageUUID = ""

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

    var settings = Settings()
    lateinit var socket: Socket
    var dialogId: Long = -1
    var messageCache = hashMapOf<Long, String>()
    var onlineCount = 0
    fun start(): NektoBot {
        logger.info("Connecting to https://im.nekto.me ...")
        val options = IO.Options()

        val socket = IO.socket(
            "https://im.nekto.me",
            options
        )
        socket.connect()
        logger.info("Connected!")
        this.socket = socket

        val authToken = JSONObject(
            mapOf(
                "action" to "auth.sendToken",
                "token" to token
            )
        )
        socket.on("connect") {
            socket.emit(
                "action", authToken
            )
        }
        // listeners by default
        socket.on("notice") {
            val res = JSONObject(JSONString { it[0].toString() }.toJSONString())
            val data = res["data"] as JSONObject
            when (res["notice"]) {
                "auth.successToken" -> {
                    logger.info("Successfully logged in!")
                    Events.LOGIN.trigger()
                }

                "search.success" -> {
                    logger.warn("Searching chat...")
                    Events.SEARCH_STARTED.trigger()
                }

                "dialog.opened" -> {
                    dialogId = data["id"].toString().toLong()
                    logger.warn("Found chat! DialogID is $dialogId")
                    isInDialog = true
                    Events.DIALOG_STARTED.trigger(FoundDialogEvent(dialogId, data))
                }

                "dialog.typing" -> {
                    if (data["typing"].toString().toBoolean()) {
                        Events.TYPING.trigger(TypingEvent.Start(data["dialogId"].toString().toLong(), data))
                    } else {
                        Events.TYPING.trigger(TypingEvent.Stop(data["dialogId"].toString().toLong(), data))
                    }
                }

                "dialog.info" -> {
                    if (shouldRefresh) {
                        val msgList = (data["messages"] as JSONArray)
                        for (i in 0 until msgList.length() - 1) {
                            val current = (msgList[i] as JSONObject)
                            messageCache[current["id"].toString().toLong()] = current["message"].toString()
                        }
                        shouldRefresh = false
                    }
                }

                "dialog.closed" -> {
                    logger.info("Dialog closed!")
                    isInDialog = false
                    Events.DIALOG_ENDED.trigger(DialogClosedEvent(dialogId, data))
                    dialogId = -1
                }

                "messages.new" -> {
                    val msg = data["message"].toString()
                    messageCache[data["id"].toString().toLong()] = msg

                    // auto read message
                    if (settings.AUTO_READ) {
                        socket.emit(
                            "action", JSONObject(
                                mapOf(
                                    "action" to "anon.readMessages",
                                    "dialogId" to dialogId,
                                    "lastMessageId" to data["id"].toString().toLong()
                                )
                            )
                        )
                    }
                    Events.MESSAGE_RECEIVED.trigger(MessageReceivedEvent(msg, data, data["randomId"].toString()))
                }

                "error.code" -> {
                    Events.ERROR.trigger(
                        ServerReturnedErrorEvent(
                            data["id"].toString().toInt(),
                            data["description"].toString(), data
                        )
                    )
                }

                "search.out" -> {
                    logger.info("Exited search!")
                }

                "messages.reads" -> {
                    val msgs = (data["reads"] as JSONArray)
                    Events.MESSAGES_READ.trigger(MessagesReadEvent(msgs.convertJSONArrayToSimpleArray(), data))
                }

                "online.count" -> {
                    onlineCount = (data["inChats"].toString().toInt())
                    logger.info("Online is: $onlineCount")
                }

                else -> {
                    if (settings.LOG_UNKNOWN) logger.warn(res.toString(2))
                }
            }
        }
        return this
    }

    private fun JSONArray.convertJSONArrayToSimpleArray(): Array<Long> {
        val arr = arrayListOf<Long>()
        for (i in 0 until this.length()) {
            arr.add(this[i].toString().toLong())
        }
        return arr.toTypedArray()
    }

    fun track() {
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "online.track",
                    "on" to true
                )
            )
        )
    }

    fun sendMsg(msg: String) {
        val tmp = UUID.randomUUID()
        lastMessageUUID = tmp.toString()
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "anon.message",
                    "dialogId" to dialogId,
                    "message" to msg,
                    "randomId" to tmp
                )
            )
        )
    }

    fun untrack() {
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "online.track",
                    "on" to false
                )
            )
        )
    }

    fun startSearch(mySex: String, wishSex: String, myAge: Array<Int>, wishAge: Array<Array<Int>>) {
        val req = JSONObject(
            mapOf(
                "action" to "search.run",
                "myAge" to myAge,
                "mySex" to mySex,
                "wishAge" to wishAge,
                "wishSex" to wishSex,
            )
        )
        socket.emit(
            "action", req
        )
    }

    fun startSearch(mySex: String, wishSex: String, myAge: Array<Int>, wishAge: Array<Int>) {
        val req = JSONObject(
            mapOf(
                "action" to "search.run",
                "myAge" to myAge,
                "mySex" to mySex,
                "wishAge" to arrayOf(wishAge),
                "wishSex" to wishSex,
            )
        )
        socket.emit(
            "action", req
        )
    }

    public fun startSearch(mySex: String, myAge: Array<Int>, wishAge: Array<Array<Int>>) {
        val req = JSONObject(
            mapOf(
                "action" to "search.run",
                "myAge" to myAge,
                "mySex" to mySex,
                "wishAge" to wishAge
            )
        )
        socket.emit(
            "action", req
        )
    }

    fun startSearch(mySex: String, myAge: Array<Int>, wishAge: Array<Int>) {
        val req = JSONObject(
            mapOf(
                "action" to "search.run",
                "myAge" to myAge,
                "mySex" to mySex,
                "wishAge" to arrayOf(wishAge)
            )
        )
        socket.emit(
            "action", req
        )
    }

    private fun refreshMsgCache() {
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "dialog.info",
                    "dialogId" to dialogId
                )
            )
        )
    }

    fun leaveChat() {
        if (dialogId == -1L) {
            logger.info("Dialog is -1")
            return
        }
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "anon.leaveDialog",
                    "dialogId" to dialogId
                )
            )
        )
    }

    fun stopSearch() {
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "search.sendOut"
                )
            )
        )
    }

    fun setTyping(typing: Boolean) {
        if (typing)
            logger.info("typing")
        else logger.info("not typing")
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "dialog.setTyping",
                    "typing" to typing
                )
            )
        )
    }

    var shouldRefresh = false
    fun refreshMessagesCache() {
        shouldRefresh = true
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "dialog.info",
                    "dialogId" to dialogId
                )
            )
        )
    }

    fun getMessageById(id: Long): String? {
        return messageCache[id]
    }

    fun getIdByMessage(msg: String): Long {
        return messageCache.entries.first {
            (it.value == msg)
        }.key
    }

}