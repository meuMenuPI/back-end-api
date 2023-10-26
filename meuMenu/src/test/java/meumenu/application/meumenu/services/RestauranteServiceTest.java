package meumenu.application.meumenu.services;

import meumenu.application.meumenu.exceptions.NaoEncontradoException;
import meumenu.application.meumenu.restaurante.DadosCadastroRestaurante;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.services.builder.RestauranteBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceTest {

    @Mock
    private RestauranteRepository repository;

    @InjectMocks
    private RestauranteService service;

    @Test
    @DisplayName("Criação do restaurante quando informar dados corretos")
    void deveCriarRestauranteQuandoInformarDadosValidos() {
        DadosCadastroRestaurante restaurante = RestauranteBuilder.criarDadosCadastroRestaurante();
        RestauranteDTO restaurante2 = RestauranteBuilder.criarRestauranteDTO();

        Mockito.when(repository.save(Mockito.any(Restaurante.class))).thenReturn(RestauranteBuilder.criarRestaurante());

        DadosCadastroRestaurante resultado = service.cadastrar(restaurante);
        assertNotNull(resultado);
        assertEquals(restaurante.usuario(), resultado.usuario());
        assertEquals(restaurante.nome(), resultado.nome());
        assertEquals(restaurante.cnpj(), resultado.cnpj());
        assertEquals(restaurante.especialidade(), resultado.especialidade());
        assertEquals(restaurante.beneficio(), resultado.beneficio());
        assertEquals(restaurante.telefone(), resultado.telefone());
        assertEquals(restaurante.site(), resultado.site());
        assertEquals(restaurante.estrela(), resultado.estrela());

    }

    @Test
    @DisplayName("Deve retornar exception quando não houver restaurante")
    void deveRetornarExceptionQuandoNaoHouverRestaurante(){

        NaoEncontradoException exception = assertThrows(NaoEncontradoException.class, () -> {
            service.listar();
        });

        assertEquals("Nenhum restaurante encontrado", exception.getMessage());

    }
    @Test
    @DisplayName("Deve retornar lista de restaurantes quando existirem")
    void deveRetornarListaDeRestaurantesQuandoExistirem() {
        List<Restaurante> listaRestaurantes = List.of(
                RestauranteBuilder.criarRestaurante(),
                RestauranteBuilder.criarRestaurante(),
                RestauranteBuilder.criarRestaurante()
        );
        Mockito.when(repository.findAll()).thenReturn(listaRestaurantes);

        List<RestauranteDTO> resultado = service.listar();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(listaRestaurantes.size(), resultado.size());
    }

    @Test
    @DisplayName("Deve retornar um restaurante por ID")
    void deveRetornarRestaurantePorId() {
        int idRestaurante = 1;
        Restaurante restaurante = RestauranteBuilder.criarRestaurante();
        Optional<Restaurante> optionalRestaurante = Optional.of(restaurante);

        when(repository.findById(idRestaurante)).thenReturn(optionalRestaurante);

        RestauranteDTO resultado = service.listarPorId(idRestaurante);


        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        assertEquals(restaurante.getEspecialidade().name(), resultado.getEspecialidade());
        assertEquals(restaurante.isBeneficio(), resultado.isBeneficio());
        assertEquals(restaurante.getTelefone(), resultado.getTelefone());
        assertEquals(restaurante.getSite(), resultado.getSite());
        assertEquals(restaurante.getEstrela(), resultado.getEstrela());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrar restaurante por ID")
    void deveLancarExcecaoQuandoNaoEncontrarRestaurantePorId() {
        int idRestaurante = 1;

        when(repository.findById(idRestaurante)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> service.listarPorId(idRestaurante));
    }

}
