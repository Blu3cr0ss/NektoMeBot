package bot.nektome.nektobot.event

import org.json.JSONObject

open class TypingEvent(val dialogId: Long, val data: JSONObject) {
    class Start(dialogId: Long, data: JSONObject) : TypingEvent(dialogId, data)
    class Stop(dialogId: Long, data: JSONObject) : TypingEvent(dialogId, data)
}