package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Aula;
import model.AulaDto;

public class Db {

	private static Db instance = null;
	private Connection connection = null;

	private String driver;
	private String url;
	private String user;
	private String password;

	private Db() {
		this.confDB();
		this.conectar();
		this.criarTabela();
		this.popularTabela();
	}

	public static Db getInstance() {
		if (instance == null) {
			instance = new Db();
		}
		return instance;
	}

	private void confDB() {
		try {
			this.driver = "org.h2.Driver";
			this.url = "jdbc:h2:mem:testdb";
			this.user = "sa";
			this.password = "";
			Class.forName(this.driver);
		} catch (Exception e) {
			// TODO: o que fazer se algo deu errado
			e.printStackTrace();
		}
	}

	// Inicia a conexão com o banco de dados
	private void conectar() {
		try {
			this.connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (Exception e) {
			// TODO: o que fazer se algo deu errado
		}
	}

	private void criarTabela() {
		String query = "CREATE TABLE AULA ("
				+ "    ID BIGINT AUTO_INCREMENT PRIMARY KEY,"
				+ "    COD_DISCIPLINA INT,"
				+ "    ASSUNTO VARCHAR(255),"
				+ "    DURACAO INT,"
				+ "    DATA VARCHAR(20),"
				+ "    HORARIO VARCHAR(20)"
				+ ")";
		try {
			Statement statement = this.connection.createStatement();
			statement.executeUpdate(query);
			this.connection.commit();
			//this.popularTabela();
		} catch (Exception e) {
			// TODO: o que fazer se algo deu errado
		}
	}

	// Encerra a conexão
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// TODO: o que fazer se algo deu errado
		}
	}

	/*
	 * ****************************************************************
	 * CRUD
	 * ****************************************************************
	 */

	// CRUD READ
	public ArrayList<AulaDto> findAll() {
		//this.popularTabela();
		String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA;";
		
	
		ArrayList<AulaDto> lista = new ArrayList<AulaDto>();
		
		try {
			
			
			Statement st = this.connection.createStatement();
			
			ResultSet queryResult = st.executeQuery(query);
			
			Aula aula = new Aula();
			
			while(queryResult.next()) {
				aula.setId(queryResult.getLong("ID"));
				aula.setAssunto(queryResult.getString("ASSUNTO"));
				aula.setData(queryResult.getString("DATA"));
				aula.setDuracao(queryResult.getInt("DURACAO"));
				aula.setHorario(queryResult.getString("HORARIO"));
				aula.setCodDisciplina(queryResult.getInt("COD_DISCIPLINA"));
	
				AulaDto dto = new AulaDto(aula);
				
				lista.add(dto);
			}
			
			return lista;
		
		}catch (Exception e) {
			
			System.err.println(e.getLocalizedMessage());
			
		}
		
		return lista;
	}

	public AulaDto findById(String id) {
		String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA "
				+ "WHERE ID = ?";
		
			try {
				PreparedStatement preQuery = this.connection.prepareStatement(query);
				
				 long parsedId = Long.parseLong(id);
				 
				 preQuery.setLong(1, parsedId);
				 
				 ResultSet queryResult = preQuery.executeQuery();
				 
				 Aula aula = new Aula();
		
				 
				 while (queryResult.next()) {
					 	aula.setId(queryResult.getLong("ID"));
						aula.setAssunto(queryResult.getString("ASSUNTO"));
						aula.setData(queryResult.getString("DATA"));
						aula.setDuracao(queryResult.getInt("DURACAO"));
						aula.setHorario(queryResult.getString("HORARIO"));
						aula.setCodDisciplina(queryResult.getInt("COD_DISCIPLINA"));
						
						System.out.println(queryResult.getInt("COD_DISCIPLINA"));
				}
				 
				 
				 AulaDto aulaDto = new AulaDto(aula);
				 
				 return aulaDto;
				
			}catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
			}
			return null;
			
		
		
	}

	// CRUD CREATAE
	public void create(AulaDto dto) {
		System.out.println("METODO CREATE "+dto);
		String query = "INSERT INTO AULA (COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO) "
				+ "VALUES (?,?,?,?,?)";
		try(PreparedStatement st = this.connection.prepareStatement(query)) {
			dto.reverteFormatoData();
			st.setString(1, dto.codDisciplina);
			st.setString(2, dto.assunto);
			st.setInt(3, Integer.parseInt(dto.duracao));
			st.setString(4, dto.data);
			st.setString(5, dto.horario);
			st.execute();
		}catch (Exception e) {
			System.out.println(e.getMessage()+ " mensagem erro");
		}
		
	}

	// CRUD DELETE
	public void deleteAll() {
		String query = "DELETE FROM AULA";
		try {
			Statement st = this.connection.createStatement();
			st.execute(query);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// CRUD DELETE
	public void delete(String id) {
		String query = "DELETE FROM AULA WHERE ID = ?";
		try {
			PreparedStatement pst = this.connection.prepareStatement(query);
			pst.setInt(1, Integer.parseInt(id));
			pst.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	public void update(AulaDto dto) {
		String query = "UPDATE AULA SET "
				+ "COD_DISCIPLINA = ?, ASSUNTO = ?, DURACAO = ?, DATA = ?, HORARIO = ? "
				+ "WHERE ID = ?";
		
		try(PreparedStatement st = this.connection.prepareStatement(query)) {
			
			st.setString(1, dto.codDisciplina);
			st.setString(2, dto.assunto);
			st.setInt(3, Integer.parseInt(dto.duracao));
			st.setString(4, dto.data);
			st.setString(5, dto.horario);
			st.setLong(6, Long.parseLong(dto.id));
			
			
			st.execute();
			
			
		}catch (Exception e) {
			System.out.println("Erro");
		}
	
	}

	public void reset() {
		System.out.println("DENTRO DE RESET");

		this.deleteAll();
		this.popularTabela();
	}

	public void popularTabela() {
		System.out.println("DENTRO DE POPULAR TABELA");
		AulaDto dto = new AulaDto();

		dto.codDisciplina = "1";
		dto.assunto = "Derivadas";
		dto.duracao = "2";
		dto.data = "12/04/2024";
		dto.horario = "14:00";
		this.create(dto);

		dto.codDisciplina = "3";
		dto.assunto = "Coordenadas Cartesianas";
		dto.duracao = "2";
		dto.data = "30/06/2024";
		dto.horario = "14:00";
		this.create(dto);

		dto.codDisciplina = "4";
		dto.assunto = "O Problema dos Três Corpos";
		dto.duracao = "4";
		dto.data = "12/08/2024";
		dto.horario = "14:00";
		this.create(dto);
	}

}