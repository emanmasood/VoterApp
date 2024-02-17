package com.example.polls.dto;


import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class StoreOtp {

    private static int otp;
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> scheduledTask;



    public static int getOtp() {
        return otp;
    }

    public static void setOtp(int otp) {

        StoreOtp.otp = otp;
        scheduleOtpDeletion();
    }

    private static void scheduleOtpDeletion() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        scheduledTask = executor.schedule(() -> {
            otp = 0;
        }, 3, TimeUnit.MINUTES);
    }

    @Scheduled(cron = "0 * * * * *")
    public static void checkForOtpDeletion() {
        if (scheduledTask != null && scheduledTask.getDelay(TimeUnit.MILLISECONDS) <= 0) {
            scheduledTask.cancel(false);
            otp = 0;
        }
    }

}
