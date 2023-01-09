package bot.nektome.nektobot.event

abstract class SimpleEvent() {
    val lambdas = arrayListOf<() -> Unit>()

    open fun fire() {
        lambdas.forEach {
            it.invoke()
        }
    }

    open fun subscribe(lambda: () -> Unit) {
        lambdas.add(lambda)
    }
}