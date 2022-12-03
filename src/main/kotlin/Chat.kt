data class Chat(
    val id: Int = 0,
    val sendId: Int = 0,
    val getId: Int = 0,
    var messages: MutableList<Message> = mutableListOf<Message>()
)