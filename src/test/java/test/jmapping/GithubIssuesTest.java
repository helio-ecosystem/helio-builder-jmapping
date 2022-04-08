package test.jmapping;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;

public class GithubIssuesTest {

	@Test
	public void testIssue7() throws IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException  {

		Model generated = TestControl.runJMapping("./src/test/resources/git-issues/issue07/mapping-issue7.json");
		Model expectedModel = TestControl.readModel("./src/test/resources/git-issues/issue07/expected-rdf.ttl");
		generated.write(System.out,"NT");
		System.out.println("-----");
		expectedModel.write(System.out,"NT");
		Assert.assertTrue(TestControl.compareModels(generated, expectedModel));
		if(TestControl.compareModels(generated, expectedModel))
			Assert.assertTrue(TestControl.compareModels(expectedModel, generated));
	}

	/**
	 * This method test the generation of subjects using the function current time stamp
	 * @throws ExtensionNotFoundException
	 * @throws IncorrectMappingException
	 * @throws TranslationUnitExecutionException
	 * @throws IncompatibleMappingException
	 * @throws SparqlRemoteEndpointException
	 * @throws SparqlQuerySyntaxException
	 * @throws MalformedMappingException if the provided mapping has syntax errors
	 */
	@Test
	public void testIssue8() throws IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException {
		Model generated =  TestControl.generateRDFSynchronously(TestControl.runJMapping("./src/test/resources/git-issues/issue08/mapping-issue8.json"));
		Boolean contains = generated.contains(null, RDF.type, ResourceFactory.createResource("https://bimerr.iot.linkeddata.es/def/building#Building"));
		String literal = generated.listObjectsOfProperty(ResourceFactory.createProperty("https://bimerr.iot.linkeddata.es/def/building#description")).nextNode().asLiteral().toString();
		contains &= literal.equals("A office building which contains 12 space and 16 staffs.");
		contains &= generated.contains(null, ResourceFactory.createProperty("https://w3id.org/def/saref4bldg#hasSpace"), ResourceFactory.createResource("https://www.data.bimerr.occupancy.es/resource/S2_Researcher_Office"));
		Assert.assertTrue(contains);
	}

	// https://github.com/oeg-upm/helio/issues/14
	// This test takes ~1h to be ran
	@Test
	public void testIssue14() throws SparqlQuerySyntaxException, SparqlRemoteEndpointException, IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException, InterruptedException  {
		LogManager.getLogManager().reset();
		List<Long> times = new ArrayList<>();
		for(int index=0; index < 30; index++) {
			long startTime2 = System.nanoTime();
			Model expected = TestControl.readModel("./src/test/resources/git-issues/issue14/expected-rdf.ttl");
			Model generated =  TestControl.generateRDFSynchronously(TestControl.runJMapping("./src/test/resources/git-issues/issue14/rml-mapping.txt"));

			//generated.write(writer, "Turtle");
			//Assert.assertTrue(TestControl.compareModels(generated, expected));
			long endTime2 = (System.nanoTime()- startTime2) / 1000000;
			times.add(endTime2);
			if(index ==15)
				Thread.sleep(2000);
		}

		System.out.println(">>>>Loading units: "+times);
	}




	@Test
	public void testIssue26() throws IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException  {
		Model generated =  TestControl.generateRDFSynchronously(TestControl.runJMapping("./src/test/resources/git-issues/issue26/rml-map.ttl"));
		Model expectedModel = TestControl.readModel("./src/test/resources/git-issues/issue26/expected.ttl");
		Assert.assertTrue(TestControl.compareModels(generated, expectedModel));
}


	@Test
	public void testIssue27() throws IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException  {
		Model generated =  TestControl.generateRDFSynchronously(TestControl.processJMapping("./src/test/resources/git-issues/issue27/mapping.ttl"));
		Model expectedModel = TestControl.readModel("./src/test/resources/git-issues/issue27/expected.ttl");
		Assert.assertTrue(TestControl.compareModels(generated, expectedModel));
	}
}
