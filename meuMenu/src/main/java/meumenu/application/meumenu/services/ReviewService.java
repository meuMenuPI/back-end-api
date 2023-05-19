package meumenu.application.meumenu.services;

import meumenu.application.meumenu.exceptions.NaoEncontradoException;
import meumenu.application.meumenu.review.DadosCadastroReview;
import meumenu.application.meumenu.review.Review;
import meumenu.application.meumenu.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repositoryReview;


    public void cadastrarReview(DadosCadastroReview dados){

        this.repositoryReview.save(new Review(dados));
    }
    public List listarPorRestaurante(Integer fkRestaurante) {
        List<Review> r = repositoryReview.findByFkRestaurante(fkRestaurante);
        if (r.isEmpty()) {
            throw new NaoEncontradoException("Nenhuma review encontrado nesse restaurante");
        }
        return r;
    }
}
