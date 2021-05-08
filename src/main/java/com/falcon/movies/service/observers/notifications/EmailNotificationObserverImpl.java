package com.falcon.movies.service.observers.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotificationObserverImpl implements NotificationObserver{

    private final Logger log = LoggerFactory.getLogger(EmailNotificationObserverImpl.class);

    // EMAIL SERVICE

    @Override
    public void sendNotification(String text) {
        log.debug("Email text message : {}", text);
    }
}
