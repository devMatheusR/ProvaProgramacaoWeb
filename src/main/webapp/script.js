// ==============================================================
// 		EVENTOS
reset = function() {
		const payload = {
		op: "RESET"
	}
	fetch("ControllerServlet", {
    method: "POST",
    headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams(payload)
    }).then(() => {
		atualizaSessao()
		window.location.href = "/prova1"
	}).catch(() => {
		alert("Erro ao resetar listagem de aulas")
	})
}

novaAula = function() {
	window.location.href = "nova";
}

calcelarNovaAula = function() {
	window.location.href = "/prova1";
}

editarAula = function(id) {
	window.location.href = "edit?id=" + id;
}

enviarNovaAula = function() {
	let data = document.getElementById('data-id').value;
	let horario = document.getElementById('hora-id').value;
	let duracao = document.getElementById('dur-id').value;
	let codDisciplina = document.getElementById('disc-id').value;
	let assunto = document.getElementById('ass-id').value;
	if (!validacaoAula(data, horario, duracao, codDisciplina, assunto)) {
        document.getElementById('validator').style.display = 'block';
        return;
    }
        if(duracao<0){
		return document.getElementById('validator').style.display = 'block';
	}
	let dataHoje = new Date()
	console.log("DATA DE HOJE",dataHoje)
	console.log("DATA FORM",data)
	//let dateTimeString = `${data}T${horario}:00`
	let dataHojeForm = new Date(data);
	dataHojeForm.setHours(0, 0, 0, 0);
	dataHoje.setHours(0,0,0,0);
	    if(dataHojeForm<dataHoje){
		return document.getElementById('validator').style.display = 'block';
	}
	        if(codDisciplina == 0){
		return document.getElementById('validator').style.display = 'block';
	}
    const payload = {
		data,
		horario,
		duracao,
		codDisciplina,
		assunto,
		op: "CREATE"
	}
	fetch("ControllerServlet", {
    method: "POST",
    headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams(payload)
    }).then(() => {
		atualizaSessao()
		window.location.href = "/prova1"
	}).catch(() => {
		alert("Erro ao criar aula")
	})
    // Aqui, você faz uma requisição AJAX POST a ControllerServlet e
    // envia a chave 'op' valendo 'CREATE'. Envie, do mesmo modo, os parâmetros
    // data, horario, duracao, codDisciplina e assunto.
    // Se a requisição for bem sucedida, execute atualizaSessao() e
    // window.location.href = "/prova1"
    // Se não for bem sucedida, decida o que fazer
}

// ENVIA CONTEÚDO EM EDIÇÃO
enviarEdit = function() {
	let id = document.getElementById('id').innerHTML;
	let data = document.getElementById('data-id').value;
	let horario = document.getElementById('hora-id').value;
	let duracao = document.getElementById('dur-id').value;
	let codDisciplina = document.getElementById('disc-id').value;
	let assunto = document.getElementById('ass-id').value;
	if (!validacaoAula(data, horario, duracao, codDisciplina, assunto)) {
        document.getElementById('validator').style.display = 'block';
        return;
    }
    if(duracao<0){
		return document.getElementById('validator').style.display = 'block';
	}
	let dataHoje = new Date()
	console.log("DATA DE HOJE",dataHoje)
	console.log("DATA FORM",data)
	//let dateTimeString = `${data}T${horario}:00`
	let dataHojeForm = new Date(data);
	dataHojeForm.setHours(0, 0, 0, 0);
	dataHoje.setHours(0,0,0,0);
	    if(dataHojeForm<dataHoje){
		return document.getElementById('validator').style.display = 'block';
	}
		        if(codDisciplina == 0){
		return document.getElementById('validator').style.display = 'block';
	}
    const payload = {
		id,
		data,
		horario,
		duracao,
		codDisciplina,
		assunto,
		op: "UPDATE"
	}
	fetch("ControllerServlet", {
    method: "POST",
    headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams(payload)
    }).then(() => {
		atualizaSessao()
		window.location.href = "/prova1"
	}).catch(() => {
		alert("Erro ao editar aula")
	})
}

// DELETA UMA AULA
deletarAula = function(id) {
	    const payload = {
		id:id,
		op: "DELETE"
	}
	fetch("ControllerServlet", {
    method: "POST",
    headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams(payload)
    }).then(() => {
		atualizaSessao()
		window.location.href = "/prova1"
	}).catch(() => {
		alert("Erro ao deletar aula")
	})
    
}

const atualizaSessao = function() {
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		if (req.readyState == 4 && req.status == 200) {
		} else {
			//alert("Erro ao atualizar sessão")
		}
	}
	req.send("op=START_SESSION");
}

// ============================================================
// 			VALIDAÇÕES

validacaoAula = function(data, hora, duracao, idDisciplina, assunto) {
      const params = [data,hora,duracao, idDisciplina, assunto].every(value => value)
    return params;
}
// ===================================================================================
// 		INICIALIZA O PROCESSAMENTO

atualizaSessao();
