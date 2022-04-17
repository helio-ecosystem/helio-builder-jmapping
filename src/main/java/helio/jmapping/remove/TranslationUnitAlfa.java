package helio.jmapping.remove;
//package helio.jmapping.processor;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.AbstractMap;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Objects;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.ModelFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.gson.JsonObject;
//
//import helio.blueprints.TranslationUnit;
//import helio.blueprints.components.components.AsyncDataProvider;
//import helio.blueprints.components.components.UnitType;
//import helio.jmapping.Datasource;
//import helio.jmapping.RDFHandler;
//import helio.jmapping.RdfHandler;
//import helio.jmapping.TripleMapping;
//
//public class TranslationUnitAlfa implements TranslationUnit {
//
//	Logger logger = LoggerFactory.getLogger(TranslationUnitAlfa.class);
//
//	// -- Attributes
//	private String id;
//	private Datasource datasource;
//	private UnitType type;
//	private Set<String> dataReferences = new HashSet<>();
//
//	// -- Constructor
//
//	public TranslationUnitAlfa(TripleMapping mapping) {
//		this.datasource = mapping.getDatasource();
//		instantiateUnitType();
//
//		try {
//			if (!(datasource.getDataHandler() instanceof RdfHandler))
//				this.dataReferences.addAll(mapping.getDataReferences());
//		} catch (Exception e) {
//			logger.error(e.toString());
//		}
//
//		this.id = String.valueOf(this.hashCode()).replace("-", "A");
//		if (mapping.getTemplate() != null)
//			VelocityEvaluator.registerVelocityTemplate(this.id, mapping.getTemplate());
//
//	}
//
//	private void instantiateUnitType() {
//		try {
//			if (datasource.getDataProvider() instanceof AsyncDataProvider) {
//				((AsyncDataProvider) datasource.getDataProvider()).setTranslationUnit(this);
//				type = UnitType.Asyc;
//			} else if (datasource.getRefresh() != null && datasource.getRefresh() > 0) {
//				type = UnitType.Scheduled;
//			} else {
//				type = UnitType.Sync;
//			}
//		} catch (Exception e) {
//			logger.error(e.toString());
//		}
//	}
//
//	// -- Getters and Setters
//
//	@Override
//	public UnitType getUnitType() {
//		return type;
//	}
//
//	@Override
//	public Model translate() {
//		Model model = ModelFactory.createDefaultModel();
//		long startTime = System.currentTimeMillis();
//		try {
//
//			if (datasource.getDataHandler() instanceof RDFHandler) {
//				model.read(datasource.getDataProvider().getData(), null, "NT");
//			} else {
//				datasource.getDataHandler().splitData(datasource.getDataProvider().getData()).parallelStream()
//						.map(chunk -> toTranslationMatrix(chunk)).map(matrix -> solveMatrix(matrix))
//						.forEach(nt -> model.read(processNt(nt), null, "TURTLE"));
//
//			}
//		} catch (Exception e) {
//			logger.error(e.toString());
//		}
//		long endTime = (System.currentTimeMillis() - startTime);
//		logger.debug("translation " + (endTime - startTime) + " milliseconds");
//
//		return model;
//	}
//
//	private ByteArrayInputStream processNt(String nt) {
//		return new ByteArrayInputStream(nt.getBytes());
//	}
//
//	/**
//	 * The matrix has has column header the dataReference, and as cell a list of
//	 * values extracted from the raw data
//	 *
//	 * @param chunk
//	 * @return
//	 */
//	private Map<String, List<String>> toTranslationMatrix(String chunk) {
//		Map<String, List<String>> v = this.dataReferences.parallelStream()
//				.map(reference -> toMatrixColumn(reference, chunk))
//				// .map(column -> markForLinking(column))
//				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
//		return v;
//	}
//
//	private Entry<String, List<String>> toMatrixColumn(String reference, String chunk) {
//		List<String> cleanedValues = new ArrayList<>();
//
//		try {
//			cleanedValues = this.datasource.getDataHandler().filter(reference, chunk).parallelStream()
//					.filter(elem -> elem != null).map(str -> str.toString().replaceAll("\"", "\\\\\""))
//					.collect(Collectors.toList());
//		} catch (Exception e) {
//			logger.error(e.toString());
//			logger.error(reference);
//			logger.error(chunk);
//		}
//		return new AbstractMap.SimpleEntry<>(reference, cleanedValues);
//	}
//
//	private String solveMatrix(Map<String, List<String>> matrix) {
//		return (VelocityEvaluator.evaluateTemplate(this.id, matrix)).toString();
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(dataReferences, datasource, type);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if ((obj == null) || (getClass() != obj.getClass()))
//			return false;
//		TranslationUnitAlfa other = (TranslationUnitAlfa) obj;
//		return Objects.equals(dataReferences, other.dataReferences) && Objects.equals(datasource, other.datasource)
//				&& type == other.type;
//	}
//
//	@Override
//	public Integer getScheduledTime() {
//		return null;
//	}
//
//	@Override
//	public void translate(InputStream stream) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public Model getRDF() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void clearRDF() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void configure(JsonObject configuration) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public Object call() throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setUnitType(UnitType type) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
