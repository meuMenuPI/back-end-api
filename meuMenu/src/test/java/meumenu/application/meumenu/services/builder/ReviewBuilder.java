package meumenu.application.meumenu.services.builder;

import meumenu.application.meumenu.review.ReviewDTOCadastro;
import meumenu.application.meumenu.review.DadosCadastroReview;
import meumenu.application.meumenu.review.Review;
import meumenu.application.meumenu.review.ReviewDTO;

import java.util.List;

public class ReviewBuilder {
    private ReviewBuilder(){
        throw new IllegalStateException("Classe utilitária");
    }
    public static Review criarReview(){
        return new Review(criarDadosCadastroReviewDto());
    }

    public static ReviewDTO criarReviewDto(){
        return new ReviewDTO("Lucas", 1,1,"18/05/2022 - 18:11","Comida muito boa chefe",3.2,4.3,3.2  );
    }

    public static ReviewDTOCadastro criarReviewDtoCadastro(){
        return new ReviewDTOCadastro( 1,1,"Comida muito boa chefe",3.2,4.3,3.2  );
    }

    public static DadosCadastroReview criarDadosCadastroReviewDto(){
        return new DadosCadastroReview( 1,1,"Comida muito boa chefe",
                3.2,4.3,3.2);
    }


    public static List<ReviewDTO> criarListaDto(){
        return List.of(
                new ReviewDTO("Lucas 1", 1,1,"18/05/2022 - 18:11",
                        "Comida muito boa chefe 1",3.2,4.3,3.5),
                new ReviewDTO("Lucas 2", 1,1,"19/05/2022 - 18:11",
                        "Comida muito boa chefe 2",3.2,4.3,3.5),
                new ReviewDTO("Lucas 3", 1,1,"20/05/2022 - 18:11",
                        "Comida muito boa chefe 3",3.2,4.3,3.5)
        );
    }
}
