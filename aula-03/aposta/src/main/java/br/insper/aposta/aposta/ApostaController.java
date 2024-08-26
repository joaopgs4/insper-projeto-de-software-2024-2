package br.insper.aposta.aposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aposta")
public class ApostaController {

    @Autowired
    private ApostaService apostaService;

    @GetMapping
    public List<Aposta> listar() {
        return apostaService.listar();
    }

    @GetMapping("/{IdAposta}")
    public String getAposta(@PathVariable String IdAposta) {
        return apostaService.verificaAposta(IdAposta);
    }

    @GetMapping("/status/{status}")
    public List<Aposta> getByStatus(@PathVariable String status) {
        return apostaService.achaAposta(status);
    }

    @PostMapping
    public void salvar(@RequestBody Aposta aposta) {
        apostaService.salvar(aposta);
    }
}
