package com.complaintsystem.listener;

import com.complaintsystem.job.EscalationJob;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.TimerTask;

public class EscalationScheduler implements ServletContextListener {

    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new Timer(true); // daemon thread
        long delay = 60_000L;        // first run after 1 minute
        long period = 10 * 60_000L;  // then every 10 minutes

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new EscalationJob().run();
            }
        }, delay, period);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer != null) {
            timer.cancel();
        }
    }
}

