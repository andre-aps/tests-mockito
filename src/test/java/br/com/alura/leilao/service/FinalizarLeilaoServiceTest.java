package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinalizarLeilaoServiceTest {
	
	@Mock
	LeilaoDao leilaoDao;

	@Mock
	EnviadorDeEmails enviadorDeEmails;
	
	@InjectMocks
	FinalizarLeilaoService service;

	@Test
	void deveFinalizarUmLeilao() {
		List<Leilao> leiloes = leiloes();
		
		when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		Leilao leilao = leiloes.get(0);

		when(leilaoDao.salvar(any())).thenReturn(leilao);

		service.finalizarLeiloesExpirados();

		assertTrue(leilao.isFechado());
	}

	@Test
	void deveEnviarEmailParaVencedorDoLeilao() {
		List<Leilao> leiloes = leiloes();

		when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		Leilao leilao = leiloes.get(0);
		when(leilaoDao.salvar(any())).thenReturn(leilao);
		service.finalizarLeiloesExpirados();
		Lance lanceVencedor = leilao.getLanceVencedor();

		verify(enviadorDeEmails, times(1)).enviarEmailVencedorLeilao(lanceVencedor);
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
