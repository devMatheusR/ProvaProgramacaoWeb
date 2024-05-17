package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Aula;
import model.AulaDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import db.Db;

 @WebServlet(urlPatterns = { "/prova1", "/nova", "/edit" })
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ControllerServlet() {
		super();
	}

	/*
	 * 	Este método é responsável pelo roteamento do projeto.
	 * 	Se você fez alguma alteração no nome do projeto, isso pode
	 * 	causar impacto aqui e talvez você precise ajustar alguns parâmetros.
	 * 	IMPORTANTE: no caso da rota 'edit', o AJAX envia um parâmetro (via GET) que
	 * 				identifica um id. Isso terá um impacto essencial na página edit.jsp.
	 * 				Lá, você precisará recuperar esse valor.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String action = request.getServletPath();
			
		
		if (action.equals("/nova")) {
			RequestDispatcher rd = request.getRequestDispatcher("nova.jsp");
			rd.forward(request, response);
		} else if (action.equals("/edit")) {
			this.getEditPage(request, response);
		}
		
	
		
	}
	
	
	
	protected void getEditPage(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
	
			String id = request.getParameter("id");
			
			HttpSession session = request.getSession();
			Db db = Db.getInstance();
			
			AulaDto aula = db.findById(id);
			
			if(aula == null) {
				session.setAttribute("dto", null);
				session.setAttribute("hasError", true);
			} else {
				session.setAttribute("dto", aula);
				session.setAttribute("hasError", false);
			}
			
			RequestDispatcher rd = request.getRequestDispatcher("edit.jsp");
			
			rd.forward(request, response);
				
		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String op = request.getParameter("op");
		System.out.println("OQUE TA VINDO DE FORA " + request.getParameter("op"));
		
		switch (op) {
		case "START_SESSION":
			this.poeDadosNaSessao(session);
			break;
		case "RESET":
			this.reset();
			break;
		case "CREATE":
			this.create(request);
			break;
		case "READ":
			this.getAula(request, response);
			break;
		case "UPDATE":
			this.update(request, response);
			break;
		case "DELETE":
			this.delete(request, response);
			break;
		}
	}

	private void poeDadosNaSessao(HttpSession session) {	
		Db banco = Db.getInstance();
		ArrayList<AulaDto> aulas = banco.findAll();
		session.setAttribute("lista", aulas);
	}

	private void reset() {
		Db banco = Db.getInstance();
		banco.reset();
	}

	private void create(HttpServletRequest request) {
		
		Aula newAula = new Aula();

		Db db = Db.getInstance();	
		newAula.setId(new Random().nextLong());
		newAula.setAssunto(request.getParameter("assunto"));
		newAula.setData(request.getParameter("data"));
		newAula.setDuracao(Integer.parseInt(request.getParameter("duracao")));
		newAula.setCodDisciplina(Integer.parseInt(request.getParameter("codDisciplina")));
		newAula.setHorario(request.getParameter("horario"));
		
		AulaDto aulaDto = new AulaDto(newAula);
		System.out.println("DENTRO DO CREATE " + newAula.getData());
		db.create(aulaDto);
	}

	private void delete(HttpServletRequest request , HttpServletResponse response) {
		try {
			Db banco =  Db.getInstance();
			AulaDto aulaDto = banco.findById(request.getParameter("id"));
			banco.delete(aulaDto.id);
		}catch (Exception e) {	 
		}
	}

	private void getAula(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Db db = Db.getInstance();
		AulaDto dto = db.findById(id);
		response.setContentType("application/json");
		StringBuilder stb = new StringBuilder();
		stb.append("{\"id\": \"").append(id).append("\",").append("\"disciplina\": \"").append(dto.disciplina)
				.append("\",").append("\"codDisciplina\": \"").append(dto.codDisciplina).append("\",")
				.append("\"assunto\": \"").append(dto.assunto).append("\"").append("\"duracao\": \"")
				.append(dto.duracao).append("\"").append("\"data\": \"").append(dto.data).append("\"")
				.append("\"horario\": \"").append(dto.horario).append("\"").append("}");
		String json = stb.toString();
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
		}
	}
	
	private void update(HttpServletRequest request, HttpServletResponse response) {
		try {
			Db banco = Db.getInstance();
			Aula novaAula = new Aula();
			novaAula.setHorario(request.getParameter("horario"));
			novaAula.setId(Long.parseLong(request.getParameter("id")));
			novaAula.setCodDisciplina(Integer.parseInt(request.getParameter("codDisciplina")));
			novaAula.setData(request.getParameter("data"));
			System.out.println("DATA ANTES DE FORMATAR "+novaAula.getData());
			novaAula.setAssunto(request.getParameter("assunto"));
			novaAula.setDuracao(Integer.parseInt(request.getParameter("duracao")));
			AulaDto aulaDto = new AulaDto(novaAula);
			System.out.println("DATA DEPOIS DE FORMATAR "+novaAula.getData());
			aulaDto.reverteFormatoData();
			banco.update(aulaDto);
		}catch (Exception e) {
			
		}
	}
}