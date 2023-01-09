package bot.nektome.nektobot

object Settings {

    val botToken: String
        get() = "OTk2ODM0MTA4OTA5MDQzNzkz.GXeVX_.1UPBnxYin-Y8LiZgwFH7RZ5wMMzuKnUcHgH3p0"

    object Sex {
        val MALE = ("M")
        val FEMALE = ("F")
    }

    object Ages {
        val UNDER17 = (arrayOf(17,17))
        val `18-21` = (arrayOf(18, 21))
        val `22-25` = (arrayOf(22, 25))
        val `26-35` = (arrayOf(26, 35))
        val OVER36 = (arrayOf(36,36))
    }
}