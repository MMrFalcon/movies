package com.falcon.movies.service.observers.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsNotificationObserverImpl implements NotificationObserver{

    private final Logger log = LoggerFactory.getLogger(SmsNotificationObserverImpl.class);

    // SMS SERVICE

    @Override
    public void sendNotification(String text) {
        log.debug("SMS notification: {}", text);
    }
}
