package test.jmapping;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import helio.blueprints.ComponentType;
import helio.blueprints.Components;
import helio.blueprints.components.TranslationUnit;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.MappingExecutionException;
import helio.jmapping.TripleMapping;
import helio.jmapping.processor.JMappingProcessor;
import helio.jmapping.processor.VelocityEvaluator;

public class TestJMapping {

	static {
		try {
			Components.registerAndLoad("/Users/andreacimmino/Desktop/helio-handler-csv-0.0.2.jar", "handlers.CsvHandler",ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-jayway/releases/download/v0.0.2/helio-handler-jayway-0.0.2.jar", "handlers.JsonHandler",ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-jsoup/releases/download/v0.0.1/helio-handler-jsoup-0.0.1.jar", "handlers.JsoupHandler",ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-provider-url/releases/download/v0.0.1/helio-provider-url-0.0.1.jar", "provider.URLProvider", ComponentType.PROVIDER); 
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-regex/releases/download/v0.0.1/helio-handler-regex-0.0.1.jar", "handlers.RegexHandler",ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-xml/releases/download/v0.0.1/helio-handler-xml-0.0.1.jar", "handlers.XmlHandler",ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
	
		try {
			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-provider-file/releases/download/v.0.0.1/helio-provider-file-0.0.1.jar",  "providers.FileProvider", ComponentType.PROVIDER);
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad("/Users/andreacimmino/Desktop/Lab/helio-materialiser/target/helio-core-0.4.5.jar", "helio.components.functions.HF", ComponentType.FUNCTION);
			Components.registerAndLoad("/Users/andreacimmino/Desktop/Lab/helio-materialiser/target/helio-core-0.4.5.jar", "helio.components.handlers.RDFHandler", ComponentType.HANDLER);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test01() throws IncompatibleMappingException, MappingExecutionException {
		String mapping1 = "{\"datasources\":[{\"id\":\"OccupantBehavior Datasource\",\"refresh\":\"1000\",\"handler\":{\"type\":\"JsonHandler\",\"iterator\":\"$\"},\"provider\":{\"type\":\"URLProvider\",\"url\":\"http://api.icndb.com/jokes/random?firstName=John&lastName=Doe\"}}],\"resource_rules\":[{\"id\":\"Building sync\",\"datasource\":\"OccupantBehavior Datasource\",\"subject\":\"https://www.data.bimerr.occupancy.es/resource/sync/{$.value.id}\",\"properties\":[{\"predicate\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\",\"object\":\"https://bimerr.iot.linkeddata.es/def/building#Building\",\"is_literal\":\"False\"},{\"predicate\":\"https://bimerr.iot.linkeddata.es/def/building#description\",\"object\":\"{$.value.joke}\",\"is_literal\":\"True\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\"},{\"predicate\":\"https://w3id.org/def/saref4bldg#hasSpace\",\"object\":\"https://www.data.bimerr.occupancy.es/resource/{$.value.categories.*}\",\"is_literal\":\"False\"}]}]}";
		String jsonPayload = "{\"$.value.id\":[\"2\"],\"$.value.joke\":[\"hi hi\"],\"$.value.categories.*\":[\"1\",\"2\"]}";
		JMappingProcessor processor = new JMappingProcessor();
		
		Set<TranslationUnit> tMaps = processor.parseMapping(mapping1);
		System.out.println(tMaps.size());
		Assert.assertTrue(tMaps.size() > 0);
	}
	
	
	@Test
	public void test02() throws IncompatibleMappingException, MappingExecutionException {
		String jsonPayload = "{\"$.value.id\":[\"2\"],\"$.value.joke\":[\"hi hi\"],\"$.value.categories.*\":[\"1\",\"2\"]}";
		VelocityEvaluator.registerVelocityTemplate("temp1", "#set( $directoryRoot = $ref.get('$.value.id') )"
				+ "$directoryRoot");
		StringWriter r = VelocityEvaluator.evaluateTemplate("temp1", toMatrix(jsonPayload));
		System.out.println(">"+r.toString());
		Assert.assertTrue(true);
	}
	
	
	private static Map<String, List<String>> toMatrix(String jsonPayload) {
		Map<String, List<String>> simplifiedMatrix = (new Gson()).fromJson(jsonPayload, new HashMap<String, List<String>>().getClass());
		return simplifiedMatrix;
	}
}
