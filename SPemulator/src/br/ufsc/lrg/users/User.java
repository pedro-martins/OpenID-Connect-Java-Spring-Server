package br.ufsc.lrg.users;

public class User {
	private int id;
	private String nome;
	private String email;
	private String cidade;
	private String dataNasc;
	private String telefone;
	private String contaBanco;
	private long cartao;
	
	
	public User(int id, String nome, String email, String cidade,
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
