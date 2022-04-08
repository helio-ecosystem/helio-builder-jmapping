package helio.jmapping.builder;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
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
import helio.jmapping.processor.VelocityEvaluator;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;

public class SparqlFlowUnit implements TranslationUnit {

	Logger logger = LoggerFactory.getLogger(SparqlFlowUnit.class);
	private DataProvider provider;
	private Set<String> dataReferences = new HashSet<>();
	private DataHandler handler;
	private String id;
	private UnitType type;
	private Integer scheduleTime;

	private List<String> stream = new CopyOnWriteArrayList<String>();

	public SparqlFlowUnit(DataProvider asyncProvider) {
		this.provider = asyncProvider;
		if (this.provider instanceof AsyncDataProvider) {
			this.type = UnitType.Asyc;
		} else {
			this.type = UnitType.Sync;
		}
		this.id = UUID.randomUUID().toString();
	}

	public List<String> getTranslations() {
		return stream;
	}

	public void flush() {
		stream.clear();
	}

	public Runnable getTask() {
		return buildAsyncCallable2();
	}

	private Runnable buildAsyncCallable2() {
		return new Runnable() {
			public void run() {
				Flowable<String> source = Flowable.create(provider, BackpressureStrategy.BUFFER);
				source.subscribe(elem -> {
					stream.add(elem);
					// model.addLiteral(ResourceFactory.createResource("http://www.ex.es/resource/2000"),
					// RDFS.comment, ResourceFactory.createPlainLiteral(elem));
				}, e -> {
					System.out.println(e.toString());
				});

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
				// .map(column -> markForLinking(column))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		return v;
	}

	private Entry<String, List<String>> toMatrixColumn(String reference, String chunk) {
		List<String> cleanedValues = new ArrayList<>();

		try {
			cleanedValues = this.handler.filter(reference, chunk).parallelStream().filter(elem -> elem != null)
					.map(str -> str.toString().replaceAll("\"", "\\\\\"")).collect(Collectors.toList());
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

	public void configure(JsonObject configuration) {
		// TODO Auto-generated method stub

	}

	public Integer getScheduledTime() {
		return scheduleTime;
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

	@Override
	public void setScheduledTime(Integer ms) {
		this.scheduleTime = ms;
	}

}
