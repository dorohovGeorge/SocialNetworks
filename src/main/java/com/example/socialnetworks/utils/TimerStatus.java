package com.example.socialnetworks.utils;

import com.example.socialnetworks.model.User;
import com.example.socialnetworks.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Component
public class TimerStatus {

    private TaskScheduler scheduler;
    private Long id;
    @Autowired
    private UserRepo userRepo;
    private ScheduledFuture<?> scheduledFuture;
    private final List<TimeUser> timeUsers = new ArrayList<>();
    private final int COUNT_OF_MINUTES = 5;
    private Instant lastTimeActive;
    private Instant currentTimeActive;
    private ScheduledExecutorService localExecutor;
    Runnable exampleRunnable = new Runnable() {
        @Override
        public void run() {
            User user = userRepo.findById(id).orElse(null);
            assert user != null;
            System.out.println(currentTimeActive);
            System.out.println(lastTimeActive);
            if (user.getLastStatus().equals("Online")
                    && currentTimeActive.toEpochMilli() != lastTimeActive.toEpochMilli()) {
                scheduledFuture.cancel(true);
                localExecutor.shutdownNow();
                lastTimeActive = currentTimeActive;
                return;
            }

            user.setLastStatus("Online");
            user.setCurrentStatus("Away");
            userRepo.save(user);
            localExecutor.shutdownNow();
        }
    };

    @Async
    public void executeTaskT(Long userId) {
        localExecutor = Executors.newScheduledThreadPool(0);
        id = userId;
        Instant t1 = Instant.now();
        boolean flag = false;
        for (int i = 0; i < timeUsers.size(); i++) {
            if (timeUsers.get(i) != null && timeUsers.get(i).getUserId().equals(userId)) {
                flag = true;
                t1 = Instant.now();
                timeUsers.set(i, new TimeUser(t1));
                currentTimeActive = t1;
            }
        }
        if (!flag) {
            t1 = Instant.now();
            timeUsers.add(new TimeUser(t1));
            lastTimeActive = t1;
            currentTimeActive = lastTimeActive;
        }
        scheduler = new ConcurrentTaskScheduler(localExecutor);
        Instant t2 = t1.plus(COUNT_OF_MINUTES, ChronoUnit.MINUTES);
        scheduledFuture = scheduler.schedule(exampleRunnable, t2);
    }

    private class TimeUser {
        private Long userId;
        private Instant timeStatus;

        public TimeUser(Instant timeStatus) {
            this.userId = id;
            this.timeStatus = timeStatus;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Instant getTimeStatus() {
            return timeStatus;
        }

        public void setTimeStatus(Instant timeStatus) {
            this.timeStatus = timeStatus;
        }
    }
}
