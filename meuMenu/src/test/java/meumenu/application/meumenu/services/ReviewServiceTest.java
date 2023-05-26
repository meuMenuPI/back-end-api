package meumenu.application.meumenu.services;

import meumenu.application.meumenu.exceptions.NaoEncontradoException;
import meumenu.application.meumenu.review.DadosCadastroReview;
import meumenu.application.meumenu.review.Review;
import meumenu.application.meumenu.review.ReviewDTOCadastro;
import meumenu.application.meumenu.review.ReviewRepository;
import meumenu.application.meumenu.services.builder.ReviewBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
        @Mock
        private ReviewRepository repository;
        @InjectMocks
        private ReviewService service;

        @Test
        @DisplayName("Criação de review quando informar dados corretos")
        void deveCriarReviewQuandoInformarDadosValidos(){
            DadosCadastroReview review = ReviewBuilder.criarDadosCadastroReviewDto();
            Review review2 = ReviewBuilder.criarReview();

            Mockito.when(repository.save(Mockito.any(Review.class))).thenReturn(review2);

            ReviewDTOCadastro resultado = service.cadastrarReview(review);

            assertNotNull(resultado);
            assertEquals(review.fkRestaurante(), resultado.getFkRestaurante());
            assertEquals(review.descricao(), resultado.getDescricao());
            assertEquals(review.nt_comida(), resultado.getNt_comida());
            assertEquals(review.nt_ambiente(), resultado.getNt_ambiente());
            assertEquals(review.nt_atendimento(), resultado.getNt_atendimento());
        }

        @Test
        @DisplayName("Deve retornar exception quando não houver review")
        void deveRetornarExceptionQuandoNaoHouverReview(){

            NaoEncontradoException exception = assertThrows(NaoEncontradoException.class, () -> {
                service.listarPorRestaurante(1);
            });

            assertEquals("Nenhuma review encontrado nesse restaurante", exception.getMessage());

        }
    }