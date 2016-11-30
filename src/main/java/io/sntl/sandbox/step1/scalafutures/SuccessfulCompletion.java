package io.sntl.sandbox.step1.scalafutures;

import akka.dispatch.OnSuccess;

public final class SuccessfulCompletion<T> extends OnSuccess<T> {
    
    @Override
    public final void onSuccess(T t) {

        System.out.println("SuccessfulCompletion says: Total pause was for " + ((Long) t)
                + " milliseconds");
    }
}
