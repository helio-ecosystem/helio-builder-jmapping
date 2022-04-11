package test.jmapping;

import org.apache.jena.rdf.model.Model;
import org.junit.Test;

import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.tests.TestUtils;

public class TestAsynchronous {


	@Test
	public void testIssue7() throws IncompatibleMappingException, TranslationUnitExecutionException, IncorrectMappingException, ExtensionNotFoundException  {
		Model model = TestUtils.generateRDFSynchronously(TestUtils.processJMapping("/Users/andreacimmino/Desktop/Lab/json-mapping/src/test/resources/async-provider/watcher/mapping.json"));
	}
}
