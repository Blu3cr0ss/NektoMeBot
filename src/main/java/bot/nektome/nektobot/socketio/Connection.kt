package bot.nektome.nektobot.socketio

import bot.nektome.nektobot.Settings
import bot.nektome.nektobot.util.logger
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONString
import java.util.*


object Connection {
    lateinit var socket: Socket
    var dialogId: Long = -1
    var messageCache = hashMapOf<Long, String>()
    fun start(token: String) {
        logger.info("Connecting to https://im.nekto.me ...")
        val options = IO.Options()

//        setOptionsTrustingAllSsls(options)
        val socket = IO.socket(
            "https://im.nekto.me",
            options
        )
        socket.connect()
        logger.info("Connected!")
        Connection.socket = socket

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
                "search.success" -> {
                    logger.warn("Searching chat...")
                }

                "dialog.opened" -> {
                    dialogId = (res["data"] as JSONObject)["id"].toString().toLong()
                    logger.warn("Found chat! DialogID is $dialogId")
                    sendMsg("привет")
                }

                "dialog.typing" -> {
                    if ((res["data"] as JSONObject)["typing"].toString().toBoolean()) {
                        logger.warn("Stranger started typing!")
                    } else {
                        logger.warn("Stranger stopped typing!")
                    }
                }

                "dialog.closed" -> {
                    logger.info("Dialog closed!")
                }

                "messages.new" -> {
                    val msg = data["message"].toString()
                    logger.info(msg)
                    // auto read message
                    socket.emit(
                        "action", JSONObject(
                            mapOf(
                                "action" to "anon.readMessages",
                                "dialogId" to dialogId,
                                "lastMessageId" to data["id"].toString().toLong()
                            )
                        )
                    )

                    if (msg.startsWith(
                            "."
                        ) && data["senderId"].toString().toInt() == 32306740    //mine id
                    ) when (msg.removePrefix(".")) {
                        "disconnect" -> {
                            leaveChat()
                        }

                        "info" -> {
                            refreshMsgCache()
                        }

                        else -> {
                            logger.error("Command not found")
                        }
                    }
                }

                "auth.successToken" -> {
                    logger.info("Successfully logged in!")
                }

                "error.code" -> {
                    logger.error(data["id"].toString() + ": " + data["description"].toString())
                }

                "search.out" -> {
                    logger.info("Exited search!")
                }

                "online.count" -> {
                    logger.info("We on track!")
                }

                "dialog.info" -> {
                    val msgList = (data["messages"] as JSONArray)
                    for (i in 0 until msgList.length() - 1) {
                        val current = (msgList[i] as JSONObject)
                        messageCache[current["id"].toString().toLong()] = current["message"].toString()
                    }
                }

                "messages.reads" -> {
                    val msgs = (data["reads"] as JSONArray)
                    logger.info("Stranger have read following msgs: " + msgs)
                }

                else -> {
                    logger.warn(res.toString(2))
                }
            }
        }
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
        logger.warn("Im online now!")
    }

    fun sendMsg(msg: String) {
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "anon.message",
                    "dialogId" to dialogId,
                    "message" to msg,
                    "randomId" to UUID.randomUUID().toString()
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
        logger.warn("Im offline now!")
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

    fun startSearch(mySex: String, myAge: Array<Int>, wishAge: Array<Array<Int>>) {
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
        dialogId = -1
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
        socket.emit(
            "action", JSONObject(
                mapOf(
                    "action" to "dialog.setTyping",
                    "typing" to typing
                )
            )
        )
    }

}