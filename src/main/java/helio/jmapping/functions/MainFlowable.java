package helio.jmapping.functions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import helio.blueprints.DataHandler;
import helio.blueprints.DataProvider;
import helio.blueprints.UnitType;
import helio.blueprints.components.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.jmapping.builder.SparqlFlowUnit;
import helio.jmapping.builder.SparqlFlowUnit;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Consumer;

public class MainFlowable {

	public static void main(String[] args) throws InterruptedException, IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException, TimeoutException, ExecutionException, IOException {
		// TODO Auto-generated method stub
		ExecutorService executorService = Executors.newFixedThreadPool(30);
		
		SparqlFlowUnit unit1 = new SparqlFlowUnit(new DummyProvider());
		SparqlFlowUnit unit2 = new SparqlFlowUnit(new FileWatcherProvider());
		SparqlFlowUnit unit3 = new SparqlFlowUnit(new FileProvider());
		
		Future<?> f1 = executorService.submit(unit1.getTask());
		Future<?> f2 = executorService.submit(unit2.getTask());
		//Future<?> f3 =  executorService.submit(unit3.getTask());
		
		
		
		Thread.sleep(6000);

		executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
		System.out.println("ready");
		int i = 0;
		while(i<20){
			System.out.println("-----i: "+i);
			System.out.println("ASYNC:"+unit1.getTranslations());
			unit1.flush();
			
			
			
			if(i%2 ==0) {
				executorService.submit(unit3.getTask()).get();
				System.out.println("SYNC: "+unit3.getTranslations());
				unit3.flush();
			}else {
				System.out.println("SYNC: ");
			}
	
			System.out.println("ASYNC1: "+unit2.getTranslations());
			unit2.flush();
		
			
				
			
			Thread.sleep(3000);
			i++;
				
		}
		f1.cancel(true);
		f2.cancel(true);
		//f3.cancel(true);
		executorService.shutdownNow();
	}

}
