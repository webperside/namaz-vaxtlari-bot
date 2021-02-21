package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.models.User;

public interface TaskService {

    void sendPrayTimes(User user);

    void storeUserData();

    void getPrayTimesFromAhlibeytAz();

    void clearPrayTimeData();
}
