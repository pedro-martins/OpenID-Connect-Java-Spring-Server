package br.ufsc.lrg.lists;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import br.ufsc.lrg.users.User;

public class UsersList {
	private List<User> usersList = new ArrayList<User>();

	public UsersList(String path) {
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(path));

			while ((sCurrentLine = br.readLine()) != null) {
				String[] dados = sCurrentLine.split(";");
				int id = 0;
				for(int i = 0; i < dados.length-1;){
					int index = dados[i].split(" ").length-1;
					String nome = dados[i++].split(" ")[index];
					String email = dados[i++];
					String cidade = dados[i++];
					String dataNasc = dados[i++];
					String telefone = dados[i++];
					String contaBanco = dados[i++];
					String card = dados[i].split(" ")[0];
					long cartao = Long.parseLong(card);
					User user = new User(id, nome, email, cidade, dataNasc, telefone, contaBanco, cartao);
					usersList.add(user);
					id++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	public User getUserById(int id){
		return usersList.get(id);
	}
	
}
