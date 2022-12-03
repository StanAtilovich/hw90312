fun main() {
    val message = Message(text = "привет 000")
    val message1 = Message(text = "салам 111")
    val message2 = Message(text = " привет 222")
    val message3 = Message(text = " салам 333")
    val message4 = Message(text = "привет4")
    val message5 = Message(text = "недам телефон")
    val message6 = Message(text = "и не проси степа")
    val message7 = Message(text = "а я не разрешаю")

    ChatService.createMessage(message)
    ChatService.createMessage(message1)
    ChatService.createMessage(message2)
    ChatService.createMessage(message3)
    ChatService.createMessage(message4)
    ChatService.createMessage(message5)
    ChatService.createMessage(message6)
    ChatService.createMessage(message7)

    ChatService.chats.forEach { println(it) }
    println()

    println(ChatService.getMessages(0, 2, 3))
}