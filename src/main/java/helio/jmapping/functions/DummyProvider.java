package helio.jmapping.functions;

import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;

import helio.blueprints.AsyncDataProvider;
import helio.blueprints.DataProvider;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableEmitter;

public class DummyProvider implements AsyncDataProvider {

	@Override
	public void configure(JsonObject configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribe(@NonNull FlowableEmitter<@NonNull String> emitter) throws Throwable {
		
		while(true){
			String msg = "Test dummy "+String.valueOf(Math.random());
			System.out.println("(EVENT: "+msg+")");
			emitter.onNext(msg);
			try{
				Thread.sleep(5000);
			}catch(Exception e) {
				Thread.currentThread().interrupt(); 
				emitter.onComplete();
				this.finalize();
				Thread.currentThread().stop();
			}
		}
		
		//;
		

	}

}
