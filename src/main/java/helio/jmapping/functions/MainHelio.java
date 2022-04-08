package helio.jmapping.functions;

import java.io.IOException;
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
import helio.jmapping.functions.helio.Helio;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Consumer;

public class MainHelio {

	public static void main(String[] args) throws InterruptedException, IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException, TimeoutException, ExecutionException {
		// TODO Auto-generated method stub
		Helio helio = new Helio();
		
		SparqlFlowUnit unit1 = new SparqlFlowUnit(new DummyProvider());
		SparqlFlowUnit unit2 = new SparqlFlowUnit(new FileWatcherProvider());
		SparqlFlowUnit unit3 = new SparqlFlowUnit(new FileProvider());
		
		SparqlFlowUnit unit4 = new SparqlFlowUnit(new FileProvider());
		unit4.setUnitType(UnitType.Scheduled);
		unit4.setScheduledTime(100);
		helio.add(unit1);
		helio.add(unit2);
		helio.add(unit3);
		helio.add(unit4);

		
		

		int i = 0;
		while(i < 19){
			System.out.println("-----i: "+i);
			i++; 
			System.out.println("ASYNC1: "+helio.readAndFlush(unit1.getId()));
			if(i%2 == 0) {
				System.out.println("SYNC: "+helio.readAndFlush(unit3.getId()));
			}else {
				System.out.println("SYNC: ");
			}
			System.out.println("ASYNC2: "+helio.readAndFlush(unit2.getId()));
			System.out.println("SCH1: "+helio.readAndFlush(unit4.getId()));

			
			
		
			
			
			System.out.println("-----");
			Thread.sleep(1000);
			
			
		
				
		}
		helio.stopAll();
		System.out.println("reached");
	}

}
