package bot.nektome.nektobot

object Settings {

    val botToken = "OTI2NjQ0NzgzNjA1MjQ4MDUy.GK_zDq.n0qxFZ0W3_BTt195miUx9uJB4PmDMcoL9Lc-Rk"

    object Sex {
        val MALE = ("M")
        val FEMALE = ("F")
    }

    object Ages {
        val UNDER17 = (arrayOf(17, 17))
        val `18-21` = (arrayOf(18, 21))
        val `22-25` = (arrayOf(22, 25))
        val `26-35` = (arrayOf(26, 35))
        val OVER36 = (arrayOf(36, 36))
    }
}