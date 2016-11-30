package io.sntl.sandbox.step5.callbackordering;

import java.util.concurrent.Callable;

public class PauseALot implements Callable<Long> {

	@Override
	public Long call() throws Exception {
		System.out.println("Pausing A Lot");
		long millisPause = 3500;
		Thread.sleep(millisPause);
		System.out.println(this.toString() + " was paused for " + millisPause
				+ " milliseconds");
		return millisPause;
	}
}
