package com.falcon.movies.service.observers.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewMoviesListener {

    private final Logger log = LoggerFactory.getLogger(NewMoviesListener.class);

    private String notificationText;
    private final List<NotificationObserver> notificationObservers = new ArrayList<>();

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public void addObserver(NotificationObserver notificationObserver) {
        log.debug("Adding new observer for new movies listener: {}", notificationObserver);
        this.notificationObservers.add(notificationObserver);
    }

    public void removeObserver(NotificationObserver notificationObserver) {
        log.debug("Removing observer: {}", notificationObserver);
        this.notificationObservers.remove(notificationObserver);
    }

    public void notifyObservers() {
        log.debug("Notifying observers...");
        for (NotificationObserver notificationObserver : notificationObservers) {
            notificationObserver.sendNotification(this.notificationText);
        }
    }
}
