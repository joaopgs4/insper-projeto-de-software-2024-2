package br.insper.aposta.aposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApostaService {

    @Autowired
    private ApostaRepository apostaRepository;

    public void salvar(Aposta aposta) {
        aposta.setId(UUID.randomUUID().toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RetornarPartidaDTO> partida = restTemplate.getForEntity(
                "http://localhost:8080/partida/" + aposta.getIdPartida(),
                RetornarPartidaDTO.class);

        if (partida.getStatusCode().is2xxSuccessful())  {
            apostaRepository.save(aposta);
        }

    }

    public String verificaAposta(String IdAposta) {
        Optional<Aposta> op = apostaRepository.findById(IdAposta);
        if (op.isPresent()) {
            Aposta aposta = op.get();
            String status = aposta.getStatus();
            if (status == "REALIZADA") {

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<RetornarPartidaDTO> partida = restTemplate.getForEntity(
                "http://localhost:8080/partida/" + aposta.getIdPartida(),
                RetornarPartidaDTO.class);

                if (partida.getBody().getStatus() == "FINALIZADA") {
                    if (partida.getBody().getPlacarMandante() > partida.getBody().getPlacarVisitante()) {
                        if (aposta.getResultado() == "VITORIA_MANDANTE"){aposta.setStatus("GANHOU");}
                        if (aposta.getResultado() == "VITORIA_VISITANTE"){aposta.setStatus("PERDEU");}
                        if (aposta.getResultado() == "EMPATE"){aposta.setStatus("PERDEU");}
                    }
                    else if (partida.getBody().getPlacarMandante() < partida.getBody().getPlacarVisitante()) {
                        if (aposta.getResultado() == "VITORIA_MANDANTE"){aposta.setStatus("PERDEU");}
                        if (aposta.getResultado() == "VITORIA_VISITANTE"){aposta.setStatus("GANHOU");}
                        if (aposta.getResultado() == "EMPATE"){aposta.setStatus("PERDEU");}
                    }
                    else {
                        if (aposta.getResultado() == "VITORIA_MANDANTE"){aposta.setStatus("PERDEU");}
                        if (aposta.getResultado() == "VITORIA_VISITANTE"){aposta.setStatus("PERDEU");}
                        if (aposta.getResultado() == "EMPATE"){aposta.setStatus("GANHOU");}
                    }
                }
                
            }
            return status;
        }
        return "Aposta não encontrada";
    }

    public List<Aposta> listar() {
        return apostaRepository.findAll();
    }

    public List<Aposta> achaAposta(String status) {
        return apostaRepository.findByStatus(status);
    }

}
