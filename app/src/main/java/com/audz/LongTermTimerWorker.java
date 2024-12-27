package com.audz;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class LongTermTimerWorker extends Worker {

    public LongTermTimerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Your long-term task goes here
        // Example: Log a message or send a notification
        // System.out.println("Long-term timer triggered!");

        // MyAccessibilityService.isOkay = true;
        // Return success
        return Result.success();
    }

    public static void scheduleTimer(Context context, long delayInDays) {
        OneTimeWorkRequest timerRequest = new OneTimeWorkRequest.Builder(LongTermTimerWorker.class)
                .setInitialDelay(delayInDays, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(context).enqueue(timerRequest);
    }
}
