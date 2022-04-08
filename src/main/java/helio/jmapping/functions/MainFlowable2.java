package helio.jmapping.functions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
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

public class MainFlowable2 {

	public static void main(String[] args) throws InterruptedException, IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException, TimeoutException, ExecutionException, IOException {
		// TODO Auto-generated method stub
		ExecutorService executorService = Executors.newFixedThreadPool(30);
		
		SparqlFlowUnit unit3 = new SparqlFlowUnit(new FileProvider());
		
		executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
		System.out.println("ready");
		
		int i = 2;
		
		while(i < 17){
			System.out.println("-----i: "+i);
			
			
			if(i%2 == 0) {
				// NECESITAMOS EL GET PARA LA SINCRONIA: espera a que haya datos en el flujo
				executorService.submit(unit3.getTask()).get();
				System.out.println(unit3.getTranslations());
				unit3.flush();
				System.out.println("--**---");
				//System.out.println(unit3.subscription.stream);
				//unit3.getDataStream().flush();
			}else {
				System.out.println("--**---");
			}
			
			
			
			i++;
			
				//executorService.submit(unit3.translate());
			
		}
		executorService.shutdownNow();
		
	}

}
