package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeradorDePagamentoTest {

    @Mock
    PagamentoDao pagamentoDao;

    @Mock
    LeilaoDao leilaoDao;

    @Mock
    EnviadorDeEmails enviadorDeEmails;

    @Captor
    ArgumentCaptor<Pagamento> captor;

    @InjectMocks
    GeradorDePagamento geradorDePagamento;

    @InjectMocks
    FinalizarLeilaoService finalizarLeilaoService;

    @Test
    void deveGerarPagamentoParaVencedorDoLeilao() {
        List<Leilao> leiloes = leiloes();

        when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
        finalizarLeilaoService.finalizarLeiloesExpirados();
        Lance lanceVencedor = leiloes.get(0).getLanceVencedor();
        geradorDePagamento.gerarPagamento(lanceVencedor);

        verify(pagamentoDao, times(1)).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();
        assertEquals(lanceVencedor.getLeilao(), pagamento.getLeilao());
        assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario());
        assertEquals(lanceVencedor.getValor(), pagamento.getValor());
        assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        assertFalse(pagamento.getPago());
    }

    private List<Leilao> leiloes() {
        List<Leilao> lista = new ArrayList<>();

        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));

        Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
        Lance segundo =new Lance(new Usuario("Ciclano"), new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);

        lista.add(leilao);

        return lista;
    }

}