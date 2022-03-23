package helio.jmapping.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import helio.blueprints.components.MappingProcessor;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.MappingExecutionException;
import helio.blueprints.mappings.Datasource;
import helio.blueprints.mappings.TripleMapping;
import helio.jmapping.Expresions;
import helio.jmapping.JMapping;
import helio.jmapping.TranslationRules;

public class JMappingProcessor implements MappingProcessor {

	public static Logger logger = LoggerFactory.getLogger(JMappingProcessor.class);

	public static final Gson GSON = new GsonBuilder()
			  .excludeFieldsWithoutExposeAnnotation()
			  .create();
	
	@Override
	public Set<TripleMapping> parseMapping(String content) throws IncompatibleMappingException, MappingExecutionException {
		Set<TripleMapping> tripleMapping = new HashSet<>();
		JMapping jsonMapping = findMappingReader(content);
		
		
		if(jsonMapping != null) {
			try {
			Set<JMapping> subMappings = groupMappings(jsonMapping);
			subMappings.parallelStream()
				.map(jMap -> toTripleMapping(jMap))
				.forEach(tMap -> tripleMapping.add(tMap));
			}catch(Exception e) {
				e.printStackTrace();
				throw new MappingExecutionException(e.toString());
			}
			
		}
		return tripleMapping;
	}
	
	private JMapping findMappingReader(String content) throws IncompatibleMappingException {
		JMapping jsonMapping = null;
		StringBuilder errors = new StringBuilder();
		try {
			jsonMapping = GSON.fromJson(content, JMapping.class);
		} catch(Exception e) {
			errors.append("Provided mapping is not a Json mapping: ").append(e.toString()).append("\n");
		}
		try {
			RmlReader reader = new RmlReader();
			jsonMapping = reader.readMapping(content);
		} catch(Exception e) {
			errors.append("Provided mapping is not a Json mapping: ").append(e.toString()).append("\n");
		}
		if(jsonMapping==null) {
			throw new IncompatibleMappingException(errors.toString());
		}
		return jsonMapping;
	}
	
	private TripleMapping toTripleMapping(JMapping unitaryMapping) {
		TranslationRules rules = unitaryMapping.getTranslationRules().get(0);
		String template = VelocityMapperToNT.toVelocityTemplate(rules);	
		Set<String> dataReferences = Expresions.extractDataReferences(rules).parallelStream().collect(Collectors.toSet());
		return new TripleMapping(unitaryMapping.getDatasources().get(0), template, dataReferences);
	}
	
	private Set<JMapping> groupMappings(JMapping mapping) throws IncorrectMappingException, ExtensionNotFoundException {
		Set<JMapping> subMappings = new HashSet<>();
		mapping.checkMapping();
		List<Datasource> datasources = mapping.getDatasources();
		List<TranslationRules> translationRulesList = mapping.getTranslationRules();
		for (Datasource datasource : datasources) {
			for (TranslationRules translationRule : translationRulesList) {
				if(translationRule.hasDataSource(datasource.getId())) {
					//boolean markedForLinking = mapping.getLinkRules().stream().anyMatch(lrules -> lrules.getSourceNamedGraph().equals(translationRule.getId()) || lrules.getTargetNamedGraph().equals(translationRule.getId()));
					try {
						JMapping subMapping = new JMapping();
						subMapping.getDatasources().add(datasource);
						subMapping.getTranslationRules().add(translationRule);
						subMappings.add(subMapping);
					}catch(Exception e) {
						logger.error(e.toString());
					}
				}
			}
		}
		return subMappings;
	}
}
