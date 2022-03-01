package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class GeradorDePagamento {

    @Autowired
    private PagamentoDao pagamentos;

    public void gerarPagamento(Lance lanceVencedor, LocalDate vencimento) {
        Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
        this.pagamentos.salvar(pagamento);
    }

    private LocalDate proximoDiaUtil(LocalDate data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();

        switch (diaDaSemana) {
            case FRIDAY:
                return data = data.plusDays(3);
            case SATURDAY:
                return data = data.plusDays(2);
            default:
                return data = data.plusDays(1);
        }
    }

}