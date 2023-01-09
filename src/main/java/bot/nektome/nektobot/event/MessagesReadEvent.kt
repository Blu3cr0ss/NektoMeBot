package bot.nektome.nektobot.event

import org.json.JSONObject

class MessagesReadEvent(val msgs:Array<Long>,val data: JSONObject) {
}