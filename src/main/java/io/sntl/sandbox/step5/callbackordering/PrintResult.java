package io.sntl.sandbox.step5.callbackordering;

import akka.dispatch.OnSuccess;

public final class PrintResult<T> extends OnSuccess<T> {
	@Override
	public final void onSuccess(T t) {
		System.out.println("The result: " + t);
	}
}
