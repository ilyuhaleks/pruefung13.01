package pruefung13jan;

import java.util.List;
import java.util.ArrayList;


// Інтерфейс повідом
interface Message {
    String getContent();
}

// Просте повідомлення
class BasicMessage implements Message {
    private String content;

    public BasicMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}

// Декоратор для повідомлення
abstract class MessageDecorator implements Message {
    protected Message message;

    public MessageDecorator(Message message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return message.getContent();
    }
}

// Декоратор для реклами
class AdvertisingMessage extends MessageDecorator {
    public AdvertisingMessage(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        return message.getContent() + " [Реклама: Текст реклами";
    }
}

// Декоратор для шифрування повідомлення
class EncryptedMessage extends MessageDecorator {
    public EncryptedMessage(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        return encrypt(message.getContent());
    }

    private String encrypt(String content) {
        return "Зашифровано(" + content + ")";
    }
}

// Підписник (Observer)
interface Subscriber {
    void update(String message);
}

// Конкретний підписник
class User implements Subscriber {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " отримала повідомлення: " + message);
    }
}

// Сповіщування (Subject)
class NotificationService {
    private static NotificationService instance;
    private List<Subscriber> subscribers = new ArrayList<>();

    private NotificationService() {}

    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers(String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(message);
        }
    }
}

// Стани повідомлень
interface MessageState {
    void handleState();
}

class NewState implements MessageState {
    @Override
    public void handleState() {
        System.out.println("Статус оновлюється");
    }
}

class SentState implements MessageState {
    @Override
    public void handleState() {
        System.out.println("Повідомлення надіслано");
    }
}

class DeliveredState implements MessageState {
    @Override
    public void handleState() {
        System.out.println("Повідомлення отримано");
    }
}

class ErrorState implements MessageState {
    @Override
    public void handleState() {
        System.out.println("Трапилася помилка");
    }
}

// Повідомлення зі станом
class StatefulMessage {
    private MessageState state;

    public void setState(MessageState state) {
        this.state = state;
    }

    public void processState() {
        if (state != null) {
            state.handleState();
        } else {
            System.out.println("Статус не встановлено");
        }
    }
}

// Головний клас
public class Main {
    public static void main(String[] args) {
        // Singleton
        NotificationService notificationService = NotificationService.getInstance();

        // Observer підписати користувачів
        User user1 = new User("Тарас");
        User user2 = new User("Одарка");
        notificationService.subscribe(user1);
        notificationService.subscribe(user2);

        // 3. Decorator Створити повідомлення
        Message message = new BasicMessage("Ласкаво просимо!");
        message = new AdvertisingMessage(message);
        message = new EncryptedMessage(message);

        notificationService.notifySubscribers(message.getContent());

        // 4. State стани повідомлення
        StatefulMessage statefulMessage = new StatefulMessage();
        statefulMessage.setState(new NewState());
        statefulMessage.processState();

        statefulMessage.setState(new SentState());
        statefulMessage.processState();

        statefulMessage.setState(new DeliveredState());
        statefulMessage.processState();

        statefulMessage.setState(new ErrorState());
        statefulMessage.processState();
    }
}
