package helio.jmapping.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import helio.blueprints.components.components.AsyncDataProvider;
import helio.blueprints.components.components.TranslationTaskBuilder;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.jmapping.builder.JMappingBuilder;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Consumer;

public class Main {

	public static void main(String[] args) throws InterruptedException, IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException, TimeoutException {
		// TODO Auto-generated method stub
		ExecutorService executorService = Executors.newFixedThreadPool(30);
		
		executorService.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
		    	AsyncDataProvider provider = new FileWatcherProvider();
				Flowable<String> source = Flowable.create(provider, BackpressureStrategy.BUFFER);
				source.map(elem -> elem.toLowerCase()).subscribe(elem -> System.out.println(elem));
				return null;
		    }
		});
		
		Future<String> value2 =  executorService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
		    	StringBuilder result = new StringBuilder();
		    	AsyncDataProvider provider = new DummyProvider();
				Flowable<String> source = Flowable.create(provider, BackpressureStrategy.BUFFER);
				source.map(elem -> elem.toLowerCase()).subscribe(elem ->  {
					System.out.println(result);
					result.append(elem);
				});
				return result.toString();
		    }
		});

		Future<String> value = executorService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
		    	StringBuilder result = new StringBuilder();
		    	Flowable<String> source2 = Flowable.generate(emitter ->  {emitter.onNext("prueba"); emitter.onComplete(); });
				return source2.map(elem -> elem.toLowerCase()).blockingFirst();
				
		    }

			
		});

	
		try {
			
			System.out.println("*****"+value.get(500, TimeUnit.MILLISECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
		System.out.println("ready");
		Thread.sleep(5000);
		
		try {
			if(value2.isDone()) {
				System.out.println("*****"+value2.get(500, TimeUnit.MILLISECONDS));
			}else {
				System.out.println("not done");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		TranslationTaskBuilder builder = new JMappingBuilder();
//		Set<Callable<? extends Object>> results = builder.build("");
//		
//		List<Future<?>> futures = new ArrayList<>();
//		results.forEach(task -> futures.add(executorService.submit(task)));
//		futures.stream().filter(f -> f.isDone() ).forEach(f -> {
//			try {
//				System.out.println(f.get());
//			} catch (InterruptedException | ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
		System.out.println("done!");
		// Lo malo de esto es que es en primer plano, se pdoria resolver asi?
		// Definir acciones como resultado: write file, return in memory, store in sparql, etc
	}

}
