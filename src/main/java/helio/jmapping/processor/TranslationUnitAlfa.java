package helio.jmapping.processor;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import helio.blueprints.components.AsyncDataProvider;
import helio.blueprints.components.TranslationUnit;
import helio.blueprints.components.UnitType;
import helio.jmapping.Datasource;
import helio.jmapping.RDFHandler;
import helio.jmapping.TripleMapping;

public class TranslationUnitAlfa implements TranslationUnit{

	Logger logger = LoggerFactory.getLogger(TranslationUnitAlfa.class);

	// -- Attributes
	private String id;
	private Datasource datasource;
	private UnitType type;
	private Set<String> dataReferences = new HashSet<>();
	private Model model;
	
	// -- Constructor
	
	public TranslationUnitAlfa(TripleMapping mapping) {
		this.datasource = mapping.getDatasource();
		instantiateUnitType();
		
		try {
			if(!(datasource.getDataHandler() instanceof RDFHandler))
				this.dataReferences.addAll(mapping.getDataReferences());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		this.id = String.valueOf(this.hashCode()).replace("-", "A");
		if(mapping.getTemplate()!=null)
			VelocityEvaluator.registerVelocityTemplate(this.id, mapping.getTemplate());

	}
	
	public Model getModel() {
		return this.model;
	}
	private void instantiateUnitType() {
		try {
			if(datasource.getDataProvider() instanceof AsyncDataProvider) {
				((AsyncDataProvider) datasource.getDataProvider()).setTranslationUnit(this);
				type = UnitType.Asyc;
			}else if(datasource.getRefresh()!=null && datasource.getRefresh()>0) {
				type = UnitType.Scheduled;
			}else {
				type = UnitType.Sync;
			}
		}catch(Exception e) {
			logger.error(e.toString());
		}
	}

	// -- Getters and Setters
	


	@Override
	public UnitType getUnitType() {
		return type;
	}

	@Override
	public Model translate() {
		try {
			model = ModelFactory.createDefaultModel();
			InputStream stream = datasource.getDataProvider().getData();
			translate(stream);
		}catch(Exception e) {
			logger.error(e.toString());
		}
		return this.model;
	}

	@Override
	public void translate(InputStream stream) {
		long startTime = System.currentTimeMillis();
		try {
			model = ModelFactory.createDefaultModel();
			if(datasource.getDataHandler() instanceof RDFHandler) {
				model.read(stream, null, "NT");
			}else {
				datasource.getDataHandler().splitData(stream).parallelStream()
				.map(chunk -> toTranslationMatrix(chunk))
				.map(matrix -> solveMatrix(matrix))
				.forEach(nt ->  model.read(new ByteArrayInputStream(nt.getBytes()), null, "NT"));
				
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		long endTime = (System.currentTimeMillis() - startTime);
		logger.debug("translation " + (endTime - startTime) + " milliseconds");
	}

	
	/**
	 * The matrix has has column header the dataReference, and as cell a list of values extracted from the raw data
	 * @param chunk
	 * @return
	 */
	private Map<String, List<String>> toTranslationMatrix(String chunk) {
		Map<String, List<String>> v =  this.dataReferences.parallelStream()
				.map(reference -> toMatrixColumn(reference, chunk))
				//.map(column -> markForLinking(column))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		return v;
	}

	private Entry<String, List<String>> toMatrixColumn(String reference, String chunk) {
		List<String> cleanedValues = new ArrayList<>();

		try {
			cleanedValues = this.datasource.getDataHandler().filter(reference, chunk).parallelStream().filter(elem -> elem!=null).map(str-> str.toString().replaceAll("\"", "\\\\\"")).collect(Collectors.toList());
		}catch(Exception e) {
			logger.error(e.toString());
			logger.error(reference);
			logger.error(chunk);
		}
		return new AbstractMap.SimpleEntry<>(reference, cleanedValues);
	}


	private String solveMatrix(Map<String, List<String>> matrix) {
		return (VelocityEvaluator.evaluateTemplate(this.id, matrix)).toString();
	}


	@Override
	public int hashCode() {
		return Objects.hash(dataReferences, datasource,  type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TranslationUnitAlfa other = (TranslationUnitAlfa) obj;
		return Objects.equals(dataReferences, other.dataReferences) && Objects.equals(datasource, other.datasource)
				&& type == other.type;
	}

	@Override
	public Model getRDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getScheduledTime() {
		return null;
	}

	

}
