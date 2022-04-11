package helio.jmapping.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class RdfSubscriber implements Supplier<String>{

	public OutputStream stream = new ByteArrayOutputStream();

	@Override
	public String get() {
		// TODO Auto-generated method stub
		return null;
	}
	

	


}
