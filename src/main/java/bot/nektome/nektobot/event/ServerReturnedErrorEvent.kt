package bot.nektome.nektobot.event

import org.json.JSONObject

class ServerReturnedErrorEvent(val id: Int, val desc: String,val data: JSONObject) {
}