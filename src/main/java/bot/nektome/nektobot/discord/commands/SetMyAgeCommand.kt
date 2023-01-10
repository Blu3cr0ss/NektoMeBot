package bot.nektome.nektobot.discord.commands

import bot.nektome.nektobot.Settings
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

object SetMyAgeCommand: AbstractCommand() {
    override fun make() {
        create(
            ImmutableApplicationCommandRequest.builder()
                .name("setmyage")
                .description("Declare your age")
                .addOption(
                    ApplicationCommandOptionData.builder()
                        .name("age")
                        .description("Number between 0 and 99")
                        .type(ApplicationCommandOption.Type.INTEGER.value)
                        .minValue(1.0)
                        .maxValue(99.0)
                        .required(true)
                        .build()
                )
                .build()
        ) { cmd, ev, cb ->
            val recievedArgument: Int = ev.interaction.commandInteraction.get().options.getOrNull(0)?.value?.get()?.asLong()!!.toInt()
                when {
                    (recievedArgument <= 17) -> {
                        Settings.SearchParameters.myAge = Settings.Ages.UNDER17
                        return@create cb.content("Your age is set to <17")
                    }

                    (18 <= recievedArgument && recievedArgument <= 21) -> {
                        Settings.SearchParameters.myAge = Settings.Ages.`18-21`
                        return@create cb.content("Your age is set to 18-21")
                    }

                    (22 <= recievedArgument && recievedArgument <= 25) -> {
                        Settings.SearchParameters.myAge = Settings.Ages.`22-25`
                        return@create cb.content("Your age is set to 22-25")
                    }

                    (26 <= recievedArgument && recievedArgument <= 35) -> {
                        Settings.SearchParameters.myAge = Settings.Ages.`26-35`
                        return@create cb.content("Your age is set to 18-21")
                    }

                    (recievedArgument >= 36) -> {
                        Settings.SearchParameters.myAge = Settings.Ages.OVER36
                        return@create cb.content("Your age is set to 36+")
                    }

                    else -> {
                        return@create cb.content("Invalid argument. Please, use ```/help``` command")
                    }
                }
            }
        }
    }
