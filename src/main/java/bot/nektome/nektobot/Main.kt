package bot.nektome.nektobot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.apache.log4j.BasicConfigurator
import java.net.URL

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        BasicConfigurator.configure()   //log4j
        //Bot.start()
        DiscordBot().main(arrayOf(Settings.botToken))
    }
}