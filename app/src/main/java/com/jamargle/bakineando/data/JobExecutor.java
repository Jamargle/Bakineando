package com.jamargle.bakineando.data;

import android.support.annotation.NonNull;
import com.jamargle.bakineando.domain.interactor.ThreadExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class JobExecutor implements ThreadExecutor {

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAXIMUM_POOL_SIZE = 5;
    private static final long KEEP_ALIVE_TIME = 10;

    private ThreadPoolExecutor threadPoolExecutor;

    public JobExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new JobThreadFactory());
    }

    @Override
    public void execute(@NonNull final Runnable runnable) {
        this.threadPoolExecutor.execute(runnable);
    }

    private static class JobThreadFactory implements ThreadFactory {

        private int counter = 0;

        @Override
        public Thread newThread(@NonNull final Runnable runnable) {
            return new Thread(runnable, "android_" + counter++);
        }

    }

}

