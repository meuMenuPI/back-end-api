package meumenu.application.meumenu.services;

import meumenu.application.meumenu.review.DadosCadastroReview;
import meumenu.application.meumenu.review.Review;
import meumenu.application.meumenu.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repositoryReview;


    public void cadastrarReview(DadosCadastroReview dados){

        this.repositoryReview.save(new Review(dados));
    }
}
