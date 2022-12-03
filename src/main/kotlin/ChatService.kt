import java.lang.RuntimeException

class MessageNotFoundException(message: String) : RuntimeException(message)
class ChatNotFoundException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)

object ChatService {
    var chatId = 0
    var messageId = 0
    var messageChatId = 0
    var chats = mutableListOf<Chat>()
    var messages = mutableListOf<Message>()

    fun createMessage(message: Message): Int {
        val newMessage = message.copy(idMessage = messageId++)
        messages.add(newMessage)
        val target = chats.find { chat -> chat.messages.any { it.toId == newMessage.toId && it.fromId == newMessage.fromId } }
        if (target == null) {
            chats.add(Chat(id = chatId++, getId = newMessage.fromId, sendId = newMessage.toId, messages = mutableListOf(newMessage.copy(chatId = messageChatId++))))
        } else {
            target.messages.add(newMessage.copy(chatId = target.id))
        }
        return messages.last().idMessage
    }

    fun editMessage(message: Message): Boolean {
        val indexOfOld = messages.indexOfFirst { it.idMessage == message.idMessage }
        val old = messages.getOrElse(indexOfOld) { throw MessageNotFoundException("Сообщение с указанным id '${message.idMessage}' не найден") }
        messages[indexOfOld] = message.copy(idMessage = old.idMessage, toId = old.toId, fromId = old.fromId)
        return true
    }

    fun deleteMessage(message: Message): Boolean {
        val targetMessage = messages.find { it.idMessage == message.idMessage }
        return if (targetMessage == null) {
            throw MessageNotFoundException("Сообщение с указанным id '${message.idMessage}' не найден")
        } else {
            messages.remove(targetMessage)
            val targetChat = chats.last { it.id == message.chatId }
            targetChat.messages.remove(message)
            chats.removeIf { it.messages.isEmpty() }
        }
    }

    fun deleteChat(chat: Chat): Boolean {
        val target = chats.find { it.id == chat.id }
        if (target == null) {
            throw ChatNotFoundException("Чат с указанным id '${chat.id}' не найдена")
        } else {
            chats.remove(target)
            messages.removeAll { it.chatId == target.id }
        }
        return true
    }

    fun getUnreadChartsCount(id: Int): Any {
        val target = chats.find { it.id == id }
        if (target == null) {
            throw UserNotFoundException("Пользователь с указанным id '$id' не найден")
        } else {
            val filteredChats = chats.filter { it.sendId == id }
            if (filteredChats.isNotEmpty()) {
                val count = filteredChats.filter { chat -> chat.messages.any { !it.read } }
                return count.size
            }
            return 0
        }
    }

    fun getChats(id: Int): Any {
        val target = chats.find { it.sendId == id || it.getId == id }
        if (target == null) {
            throw UserNotFoundException("Пользователь с указанным id '$id' не найден")
        } else {
            val filteredChats = chats.filter { it.sendId == id || it.getId == id }
            return if (filteredChats.isNotEmpty()) filteredChats.filter { it.messages.isNotEmpty() } else "Сообщений нет"
        }
    }

    fun getMessages(chatId: Int, messageId: Int, count: Int): List<Message> {
        val targetChat = chats.find { it.id == chatId }
        if (targetChat == null) {
            throw ChatNotFoundException("Чат с указанным id '$chatId' не найден")
        } else {
            return targetChat.messages.asSequence()
                .filter { it.idMessage >= messageId }
                .take(count)
                .map { it.copy(read = true) }
                .toList()
        }
    }
}