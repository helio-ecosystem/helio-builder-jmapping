package helio.jmapping.builder;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import helio.blueprints.AsyncDataProvider;
import helio.blueprints.DataHandler;
import helio.blueprints.DataProvider;
import helio.blueprints.TranslationUnit;
import helio.blueprints.UnitType;
import helio.jmapping.Datasource;
import helio.jmapping.TripleMapping;
import helio.providers.RdfHandler;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;

public class MemoryFlowUnit implements TranslationUnit {

	Logger logger = LoggerFactory.getLogger(MemoryFlowUnit.class);

	private Set<String> dataReferences = new HashSet<>();
	private Datasource datasource;
	private String id;
	private UnitType type;

	private List<String> stream = new CopyOnWriteArrayList<String>();

	public MemoryFlowUnit(TripleMapping mapping) {
		this.datasource = mapping.getDatasource();

		try {

			if (datasource.getDataProvider() instanceof AsyncDataProvider) {
				this.type = UnitType.Async;
			} else {
				this.type = UnitType.Sync;
			}

			if (!(datasource.getDataHandler() instanceof RdfHandler))
				this.dataReferences.addAll(mapping.getDataReferences());
		} catch (Exception e) {
			logger.error(e.toString());
		}

		this.id = String.valueOf(this.hashCode()).replace("-", "A");
		if (mapping.getTemplate() != null) 
			VelocityEvaluator.registerVelocityTemplate(this.id, mapping.getTemplate());
			
		
	}

	public List<String> getTranslations() {
		return stream;
	}

	public void flush() {
		stream.clear();
	}

	public Callable<Void> getTask() {
		return new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				
					DataProvider provider = datasource.getDataProvider();
					Flowable<String> source = Flowable.create(provider, BackpressureStrategy.BUFFER);
					source.subscribe(data -> {
						if(datasource.getDataHandler() instanceof RdfHandler) {
							stream.add(data);
						}else {
							datasource.getDataHandler().iterator(data).parallelStream()
							.map(chunk -> toTranslationMatrix(chunk)).map(matrix -> solveMatrix(matrix))
							.forEach(nt -> stream.add(nt));
						}
						
					}, e -> {
						System.out.println(e.toString());
					});
				
				return null;
			}
		};
	}
	
	/**
	 * The matrix has has column header the dataReference, and as cell a list of
	 * values extracted from the raw data
	 *
	 * @param chunk
	 * @return
	 */
	private Map<String, List<String>> toTranslationMatrix(String chunk) {
		Map<String, List<String>> v = this.dataReferences.parallelStream()
				.map(reference -> toMatrixColumn(reference, chunk))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		return v;
	}

	private Entry<String, List<String>> toMatrixColumn(String reference, String chunk) {
		List<String> cleanedValues = new ArrayList<>();

		try {
			cleanedValues = datasource.getDataHandler().filter(reference, chunk).parallelStream()
					.filter(elem -> elem != null).map(str -> str.toString().replaceAll("\"", "\\\\\""))
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(e.toString());
			logger.error(reference);
			logger.error(chunk);
		}
		return new AbstractMap.SimpleEntry<>(reference, cleanedValues);
	}

	private String solveMatrix(Map<String, List<String>> matrix) {
		return (VelocityEvaluator.evaluateTemplate(this.id, matrix)).toString();
	}

	// getters & setters

	public Integer getScheduledTime() {
		return datasource.getRefresh();
	}

	public UnitType getUnitType() {
		return type;
	}

	public void setUnitType(UnitType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}


	public void configure(JsonObject configuration) {
		// empty
	}

}
