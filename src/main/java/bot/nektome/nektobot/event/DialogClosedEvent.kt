package bot.nektome.nektobot.event

import org.json.JSONObject

class DialogClosedEvent(val dialogId:Long,val data:JSONObject) {
}