package io.sntl.sandbox.step6.actorfutures;

import java.util.concurrent.Callable;

public class RandomPause implements Callable<Long> {

    private Long millisPause;

    public RandomPause() {
        millisPause = Math.round(Math.random() * 3000) + 1000; // 1,000 to 4,000
        System.out.println(this.toString() + " will pause for " + millisPause
                + " milliseconds");
    }

    public Long call() throws Exception {
        Thread.sleep(millisPause);
        System.out.println(this.toString() + " was paused for " + millisPause
                + " milliseconds");
        return millisPause;
    }
}
