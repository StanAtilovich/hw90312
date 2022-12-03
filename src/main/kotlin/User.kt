data class User(
    val idUser: Int = 0,
    val chats: MutableList<Chat> = mutableListOf<Chat>()
)