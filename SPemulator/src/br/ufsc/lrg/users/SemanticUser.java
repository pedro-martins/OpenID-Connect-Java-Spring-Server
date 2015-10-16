package br.ufsc.lrg.users;

import br.com.srs.gsonld.SemanticClass;
import br.com.srs.gsonld.SemanticProperty;

@SemanticClass("http://schema.org/Person")
public class SemanticUser {
	@SemanticProperty("http://schema.org/vatID")
	private int id;
	@SemanticProperty("http://schema.org/givenName")
	private String nome;
	@SemanticProperty("http://schema.org/email")
	private String email;
	@SemanticProperty("http://schema.org/address")
	private String cidade;
	@SemanticProperty("http://schema.org/birthDate")
	private String dataNasc;
	@SemanticProperty("http://schema.org/telephone")
	private String telefone;
	@SemanticProperty("http://schema.org/duns")
	private String contaBanco;
	@SemanticProperty("http://schema.org/taxID")
	private long cartao;

	public SemanticUser(){
		
	}
	public SemanticUser(int id, String nome, String email, String cidade,
			String dataNasc, String telefone, String contaBanco, long cartao) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cidade = cidade;
		this.dataNasc = dataNasc;
		this.telefone = telefone;
		this.contaBanco = contaBanco;
		this.cartao = cartao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getContaBanco() {
		return contaBanco;
	}

	public void setContaBanco(String contaBanco) {
		this.contaBanco = contaBanco;
	}

	public long getCartao() {
		return cartao;
	}

	public void setCartao(long cartao) {
		this.cartao = cartao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
