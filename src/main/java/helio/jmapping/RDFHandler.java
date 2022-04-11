package helio.jmapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import helio.blueprints.DataHandler;

public class RdfHandler implements DataHandler {

	@Override
	public void configure(JsonObject configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> filter(String filter, String dataChunk) {
		
		return null;
	}

	@Override
	public List<String> iterator(String dataChunk) {
		List<String> data = new ArrayList<>();
		data.add(dataChunk);
		return data;
	}

}
