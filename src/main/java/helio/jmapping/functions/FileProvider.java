package helio.jmapping.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;

import com.google.gson.JsonObject;

import helio.blueprints.DataProvider;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.functions.Consumer;

public class FileProvider implements DataProvider {

	@Override
	public void configure(JsonObject configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(@NonNull FlowableEmitter<@NonNull String> emitter) throws Throwable {
		BufferedReader br = new BufferedReader(new FileReader(new File("test.txt")));
		String st;
		StringBuilder sbr = new StringBuilder();
		while ((st = br.readLine()) != null)
			sbr.append(st).append("\n");
		emitter.onNext(sbr.toString());
		emitter.onComplete();
		
	}



}
