package br.ufsc.lrg.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;

import br.com.srs.gsonld.GsonLD;
import br.ufsc.lrg.users.SemanticUser;
import br.ufsc.lrg.users.User;

public class SemanticJsonPerformanceTest {
	
	public static void writeData(String data, String path) {
		try {

			Files.write(Paths.get(path), data.getBytes(), StandardOpenOption.APPEND);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String processHttpGet(String url) {
		String linha, retorno = null;
		HttpURLConnection conexao = null;
		try {
			URL target = new URL(url);
			conexao = (HttpURLConnection) target.openConnection();
			conexao.setRequestMethod("GET");
			conexao.setRequestProperty("Accept", "application/json");
			if (conexao.getResponseCode() != 200)
				throw new RuntimeException("Erro HTTP - Código "
						+ conexao.getResponseCode());
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conexao.getInputStream())));

			retorno = "";
			while ((linha = br.readLine()) != null)
				retorno += linha;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conexao != null)
				conexao.disconnect();
		}
		return retorno;
	}

	public static void main(String[] args) {
		if(args.length < 2){
			System.err.println("you need to inform the base url of application in the firt param \n"
					+ " in the second param the path of file to write the data of aplication \n"
					+ " for example: java -jar app.jar http://localhost:8080 /home/name/archive.csv");
			System.exit(-1);
		}
		if(args.length > 2){
			System.err.println("only two param permited");
			System.exit(-1);
		}
		String url = args[0]+"SemanticTest/service/semantic/";
		long unmarshallTimeNano = 0;

		for (int i = 0; i < 1000; i++) {

			long time = System.nanoTime();
			String httpResponse = processHttpGet(url + i);

			unmarshallTimeNano = System.nanoTime();
			new GsonLD().fromJsonLD(httpResponse, SemanticUser.class);
			unmarshallTimeNano = System.nanoTime() - unmarshallTimeNano;

			time = System.nanoTime() - time;

			
			writeData((i+1)+","+unmarshallTimeNano +","+httpResponse.length()+","+time+"\n",args[1]);
		
		}
		String httpResponse = processHttpGet(args[0] + "/SemanticTest/service/getData/");
	}
}
