package org.example.Managers;

import org.example.CollectionModel.HumanBeing;
import org.example.CommandSpace.DatabaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Класс менеджера коллекции, хранящий ее саму и информацию о ней
 */
public class CollectionManager {
    private final Stack<HumanBeing> collection = new Stack<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;

    static final Logger collectionManagerLogger = LoggerFactory.getLogger(CollectionManager.class);


    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
        this.lastSaveTime = null;
        try {
            collection.addAll(DatabaseHandler.getDatabaseManager().loadCollection());
        } catch (NullPointerException e) {
            collectionManagerLogger.error("Какое-то из полей в базе данных равно null");
        }
    }

    public static String formatTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        if (localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
            return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    public static String formatTime(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return formatTime(localDateTime);
    }

    public Stack<HumanBeing> getCollection() {
        synchronized (collection) {
            return collection;
        }
    }

    public String getLastInitTime() {
        synchronized (this) {
            return formatTime(lastInitTime);
        }
    }

    public String getLastSaveTime() {
        synchronized (this) {
            return formatTime(lastSaveTime);
        }
    }

    public String getCollectionType() {
        synchronized (collection) {
            return collection.getClass().getName();
        }
    }

    public int getSize() {
        synchronized (collection) {
            return collection.size();
        }
    }

    public void clear() {
        synchronized (collection) {
            collection.clear();
            lastInitTime = LocalDateTime.now();
            collectionManagerLogger.info("Коллекция очищена");
        }
    }

    public HumanBeing getLast() {
        synchronized (collection) {
            return collection.peek();
        }
    }

    public HumanBeing getById(int id) {
        synchronized (collection) {
            for (HumanBeing element : collection) {
                if (element.getId() == id) return element;
            }
            return null;
        }
    }

    public void addElement(HumanBeing humanBeing) {
        synchronized (collection) {
            this.lastSaveTime = LocalDateTime.now();
            collection.push(humanBeing);
            collectionManagerLogger.info("Добавлен новый элемент в коллекцию");
        }
    }

    public void addElement(Collection<HumanBeing> collection) {
        synchronized (this.collection) {
            if (collection == null) return;
            for (HumanBeing humanBeing : collection) {
                addElement(humanBeing);
            }
        }
    }

    public void removeLast() throws EmptyStackException {
        synchronized (collection) {
            if (collection.isEmpty()) throw new EmptyStackException();
            collection.pop();
            this.lastSaveTime = LocalDateTime.now();
        }
    }

    public void removeElement(HumanBeing humanBeing) {
        synchronized (collection) {
            collection.remove(humanBeing);
            this.lastSaveTime = LocalDateTime.now();
        }
    }

    public void removeElements(Collection<HumanBeing> collection) {
        synchronized (this.collection) {
            this.collection.removeAll(collection);
            this.lastSaveTime = LocalDateTime.now();
        }
    }

    public void removeElements(List<Long> deletedId) {
        synchronized (collection) {
            deletedId.forEach(id -> collection.remove(getById(id)));
            this.lastSaveTime = LocalDateTime.now();
        }
    }

    public boolean checkNonExistById(long id) {
        synchronized (collection) {
            return collection.stream().noneMatch(o -> o.getId() == id);
        }
    }

    public HumanBeing getById(long id) {
        synchronized (collection) {
            for (HumanBeing humanBeing : collection) {
                if (humanBeing.getId() == id) return humanBeing;
            }
            return null;
        }
    }

    public void editById(long id, HumanBeing newHumanBeing) {
        synchronized (collection) {
            HumanBeing oldHumanBeing = getById(id);
            String userLogin = oldHumanBeing.getUserLogin();
            removeElement(oldHumanBeing);
            newHumanBeing.setId(id);
            newHumanBeing.setUserLogin(userLogin);
            addElement(newHumanBeing);
            this.lastSaveTime = LocalDateTime.now();
            collectionManagerLogger.info("Изменен объект с id=" + id + ": " + newHumanBeing);
        }
    }

    public LocalDateTime getLastSaveTimeInDate() {
        synchronized (this) {
            return lastSaveTime;
        }
    }

    public void setLastInitTime(LocalDateTime lastInitTime) {
        synchronized (this) {
            this.lastInitTime = lastInitTime;
        }
    }

    public void setLastSaveTime(LocalDateTime lastSaveTime) {
        synchronized (this) {
            this.lastSaveTime = lastSaveTime;
        }
    }

    @Override
    public String toString() {
        synchronized (collection) {
            if (collection.isEmpty()) return "Коллекция пуста";
            Long last = getLast().getId();

            StringBuilder collectionInfo = new StringBuilder();
            for (HumanBeing humanBeing : collection) {
                collectionInfo.append(humanBeing);
                if (!humanBeing.getId().equals(last)) collectionInfo.append("\n");
            }
            return collectionInfo.toString();
        }
    }
}
