package test.jmapping;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import helio.blueprints.TranslationUnit;
import helio.blueprints.components.ComponentType;
import helio.blueprints.components.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.jmapping.processor.JMappingProcessor;
import helio.jmapping.processor.VelocityEvaluator;

public class TestJMapping {

//	static {
//		try {
//			Components.registerAndLoad("/Users/andreacimmino/Desktop/helio-handler-csv-0.0.2.jar", "handlers.CsvHandler",ComponentType.HANDLER);
//		} catch (ExtensionNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-jayway/releases/download/v0.0.2/helio-handler-jayway-0.0.2.jar", "handlers.JsonHandler",ComponentType.HANDLER);
//		} catch (ExtensionNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-jsoup/releases/download/v0.0.1/helio-handler-jsoup-0.0.1.jar", "handlers.JsoupHandler",ComponentType.HANDLER);
//		} catch (ExtensionNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-provider-url/releases/download/v0.0.1/helio-provider-url-0.0.1.jar", "provider.URLProvider", ComponentType.PROVIDER);
//		} catch (ExtensionNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-regex/releases/download/v0.0.1/helio-handler-regex-0.0.1.jar", "handlers.RegexHandler",ComponentType.HANDLER);
//		} catch (ExtensionNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-handler-xml/releases/download/v0.0.1/helio-handler-xml-0.0.1.jar", "handlers.XmlHandler",ComponentType.HANDLER);
//		} catch (ExtensionNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			Components.registerAndLoad("https://github.com/helio-ecosystem/helio-provider-file/releases/download/v.0.0.1/helio-provider-file-0.0.1.jar",  "providers.FileProvider", ComponentType.PROVIDER);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			Components.registerAndLoad("/Users/andreacimmino/Desktop/Lab/helio-materialiser/target/helio-core-0.4.5.jar", "helio.components.functions.HF", ComponentType.FUNCTION);
//			Components.registerAndLoad("/Users/andreacimmino/Desktop/Lab/helio-materialiser/target/helio-core-0.4.5.jar", "helio.components.handlers.RDFHandler", ComponentType.HANDLER);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}


//	@Test
//	public void test01() throws IncompatibleMappingException, TranslationUnitExecutionException {
//		String mapping1 = "{\"datasources\":[{\"id\":\"OccupantBehavior Datasource\",\"refresh\":\"1000\",\"handler\":{\"type\":\"JsonHandler\",\"iterator\":\"$\"},\"provider\":{\"type\":\"URLProvider\",\"url\":\"http://api.icndb.com/jokes/random?firstName=John&lastName=Doe\"}}],\"resource_rules\":[{\"id\":\"Building sync\",\"datasource\":\"OccupantBehavior Datasource\",\"subject\":\"https://www.data.bimerr.occupancy.es/resource/sync/{$.value.id}\",\"properties\":[{\"predicate\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\",\"object\":\"https://bimerr.iot.linkeddata.es/def/building#Building\",\"is_literal\":\"False\"},{\"predicate\":\"https://bimerr.iot.linkeddata.es/def/building#description\",\"object\":\"{$.value.joke}\",\"is_literal\":\"True\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\"},{\"predicate\":\"https://w3id.org/def/saref4bldg#hasSpace\",\"object\":\"https://www.data.bimerr.occupancy.es/resource/{$.value.categories.*}\",\"is_literal\":\"False\"}]}]}";
//		String jsonPayload = "{\"$.value.id\":[\"2\"],\"$.value.joke\":[\"hi hi\"],\"$.value.categories.*\":[\"1\",\"2\"]}";
//		JMappingProcessor processor = new JMappingProcessor();
//
//		Set<TranslationUnit> tMaps = processor.parseMapping(mapping1);
//		System.out.println(tMaps.size());
//		Assert.assertTrue(tMaps.size() > 0);
//	}


	@Test
	public void test02() throws IncompatibleMappingException, TranslationUnitExecutionException {
		String jsonPayload = "{\"$.value.id\":[\"2\", \"3\"],\"$.value.joke\":[\"hi hi\"],\"$.value.categories.*\":[\"1\",\"2\"]}";
		VelocityEvaluator.registerVelocityTemplate("temp1", "#set( $directoryRoot = $ref.get('$.value.id') )"
				+ "#foreach( ${Hash1052400559} in $directoryRoot)"
				+ "${Hash1052400559},"
				+ "#end");
		StringWriter r = VelocityEvaluator.evaluateTemplate("temp1", toMatrix(jsonPayload));
		System.out.println(">"+r.toString());
		Assert.assertTrue(true);
	}


	private static String sampleMap = "#set( $subjectSet = \"<https://example.test.es/test>\" )\n"
			+ "#set($subjects = $HF.splitSubjects($subjectSet) )"
			+ " $subjects"
			+ "#foreach( $subject in $subjects )"
				+ "#set( $predicate1425683616=\"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\" )\n"
				+ "#set( $object1421907372=\"<https://example.test.org/Test>\" )\n"
				+ "#if( $HF.notBlank($subject) && $HF.notBlank($predicate1425683616) && $HF.notBlank($object1421907372) )\n"
				+ "$HF.formatNewURI($subject) $HF.formatNewURI($predicate1425683616) $HF.format($object1421907372,false,false,false) .\n"
				+ "#end\n"
				/*+ "#set( $predicate1451699415=\"<http://www.example.org/ontology#key>\" )\n"
				+ "#set( $object276193334=\" #foreach( ${Hash1052400559} in $ref.get('$.*.key')) $HF.quote()${Hash1052400559}$HF.quote()^^<http://www.w3.org/2001/XMLSchema#integer> #if( ($foreach.count) && ($foreach.count <= $Hash1052400559SET.size()) ), #end #end\" )\n"
				+ "#if( $HF.notBlank($subject) && $HF.notBlank($predicate1451699415) && $HF.notBlank($object276193334) )\n"
				+ "$HF.formatNewURI($subject) $HF.formatNewURI($predicate1451699415) $HF.format($object276193334,true,true,false) .\n"
				+ "#end\n"*/
			+" #end";
	
	@Test
	public void test03() throws IncompatibleMappingException, TranslationUnitExecutionException {
		Map<String, List<String>> values = new HashMap<>();
		values.put("$.*.key", toList(1, 2, 3, 4));
		values.put("$.[*].text", toList("Spain", "Germany", "Italy", "France"));
		values.put("$.[*].number", toList(12312, 45, 754, 23));
		
		VelocityEvaluator.registerVelocityTemplate("temp1", sampleMap);
		StringWriter r = VelocityEvaluator.evaluateTemplate("temp1", values);
		System.out.println(">"+r.toString());
		Assert.assertTrue(true);
	}
	
	private static String sampleMap2 = "#set( $subjectSet = \"<https://example.test.es/test>\" )\n"
			+ "#set($subjects = $HF.splitSubjects($subjectSet) )#foreach( $subject in $subjects )#set( $predicate1425683616=\"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\" )\n"
			+ "#set( $object1421907372=\"<https://example.test.org/Test>\" )\n"
			+ "#if( $HF.notBlank($subject) && $HF.notBlank($predicate1425683616) && $HF.notBlank($object1421907372) )\n"
			+ "$HF.formatNewURI($subject) $HF.formatNewURI($predicate1425683616) $HF.format($object1421907372,false,false,false) .\n"
			+ "#end\n"
			+ "#set( $predicate1451699415=\"<http://www.example.org/ontology#key>\" )\n"
			+ "#set( $object276193334=\" #foreach( ${Hash1052400559} in $ref.get('$.*.key')) $HF.quote()${Hash1052400559}$HF.quote()^^<http://www.w3.org/2001/XMLSchema#integer> #if( ($foreach.count) && ($foreach.count <= $Hash1052400559SET.size()) ), #end #end\" )\n"
			+ "#if( $HF.notBlank($subject) && $HF.notBlank($predicate1451699415) && $HF.notBlank($object276193334) )\n"
			+ "$HF.formatNewURI($subject) $HF.formatNewURI($predicate1451699415) $HF.format($object276193334,true,true,false) .\n"
			+ "#end\n"
			+ "#set( $predicate367269885=\"<http://www.example.org/ontology#number>\" )\n"
			+ "#set( $object986231631=\" #foreach( ${Hash1205709} in $ref.get('$.[*].number')) $HF.quote()${Hash1205709}$HF.quote()^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> #if( ($foreach.count) && ($foreach.count <= $Hash1205709SET.size()) ), #end #end\" )\n"
			+ "#if( $HF.notBlank($subject) && $HF.notBlank($predicate367269885) && $HF.notBlank($object986231631) )\n"
			+ "$HF.formatNewURI($subject) $HF.formatNewURI($predicate367269885) $HF.format($object986231631,true,true,false) .\n"
			+ "#end\n"
			+ "#set( $predicate2061321369=\"<http://www.example.org/ontology#text>\" )\n"
			+ "#set( $object1163762206=\" #foreach( ${Hash987870871} in $ref.get('$.[*].text')) $HF.quote()${Hash987870871}$HF.quote()@en #if( ($foreach.count) && ($foreach.count <= $Hash987870871SET.size()) ), #end #end\" )\n"
			+ "#if( $HF.notBlank($subject) && $HF.notBlank($predicate2061321369) && $HF.notBlank($object1163762206) )\n"
			+ "$HF.formatNewURI($subject) $HF.formatNewURI($predicate2061321369) $HF.format($object1163762206,true,false,true) .\n"
			+ "#end\n"
			+ "#end";
	
	
	@Test
	public void test04() throws IncompatibleMappingException, TranslationUnitExecutionException {
		Map<String, List<String>> values = new HashMap<>();
		values.put("$.*.key", toList(1, 2, 3, 4));
		values.put("$.[*].text", toList("Spain", "Germany", "Italy", "France"));
		values.put("$.[*].number", toList(12312, 45, 754, 23));
		
		VelocityEvaluator.registerVelocityTemplate("temp1", sampleMap2);
		StringWriter r = VelocityEvaluator.evaluateTemplate("temp1", values);
		System.out.println(">"+r.toString());
		Assert.assertTrue(true);
	}
	
	private static List<String> toList(Object ...args){
		List<String> v = new ArrayList<>();
		for( int index = 0; index < args.length; index++)
			v.add(String.valueOf(args[index]));
		return v;
	}
	
	private static Map<String, List<String>> toMatrix(String jsonPayload) {
		Map<String, List<String>> simplifiedMatrix = (new Gson()).fromJson(jsonPayload, new HashMap<String, List<String>>().getClass());
		return simplifiedMatrix;
	}
}
