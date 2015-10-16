package br.ufsc.lrg.users;

import br.com.srs.gsonld.SemanticClass;
import br.com.srs.gsonld.SemanticProperty;

@SemanticClass("http://schema.org/Person")
public class SemanticUser {
	@SemanticProperty("http://schema.org/vatID")
	private int idendificador;
	@SemanticProperty("http://schema.org/givenName")
	private String name;
	@SemanticProperty("http://schema.org/email")
	private String enderecoeletronico;
	@SemanticProperty("http://schema.org/address")
	private String city;
	@SemanticProperty("http://schema.org/birthDate")
	private String born;
	@SemanticProperty("http://schema.org/telephone")
	private String tel;
	@SemanticProperty("http://schema.org/duns")
	private String bankacc;
	@SemanticProperty("http://schema.org/taxID")
	private long credit;

	public SemanticUser(){
		
	}
	public SemanticUser(int id, String nome, String email, String cidade,
			String dataNasc, String telefone, String contaBanco, long cartao) {
		this.idendificador = id;
		this.name = nome;
		this.enderecoeletronico = email;
		this.city = cidade;
		this.born = dataNasc;
		this.tel = telefone;
		this.bankacc = contaBanco;
		this.credit = cartao;
	}

	public String getNome() {
		return name;
	}

	public void setNome(String nome) {
		this.name = nome;
	}

	public String getEmail() {
		return enderecoeletronico;
	}

	public void setEmail(String email) {
		this.enderecoeletronico = email;
	}

	public String getCidade() {
		return city;
	}

	public void setCidade(String cidade) {
		this.city = cidade;
	}

	public String getDataNasc() {
		return born;
	}

	public void setDataNasc(String dataNasc) {
		this.born = dataNasc;
	}

	public String getTelefone() {
		return tel;
	}

	public void setTelefone(String telefone) {
		this.tel = telefone;
	}

	public String getContaBanco() {
		return bankacc;
	}

	public void setContaBanco(String contaBanco) {
		this.bankacc = contaBanco;
	}

	public long getCartao() {
		return credit;
	}

	public void setCartao(long cartao) {
		this.credit = cartao;
	}

	public int getId() {
		return idendificador;
	}

	public void setId(int id) {
		this.idendificador = id;
	}
}
