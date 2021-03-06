package io.sntl.sandbox.step4.composingfutures;

import java.util.concurrent.Callable;

public class PauseALittle implements Callable<Long> {

	@Override
	public Long call() throws Exception {
		System.out.println("Pausing A Little");
		long millisPause = 1500;
		Thread.sleep(millisPause);
		System.out.println(this.toString() + " was paused for " + millisPause
				+ " milliseconds");
		return millisPause;
	}

}
