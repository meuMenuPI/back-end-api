package meumenu.application.meumenu.services;

import meumenu.application.meumenu.exceptions.NaoEncontradoException;
import meumenu.application.meumenu.review.DadosCadastroReview;
import meumenu.application.meumenu.review.Review;
import meumenu.application.meumenu.review.ReviewDTO;
import meumenu.application.meumenu.review.ReviewRepository;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repositoryReview;
    @Autowired
    private UsuarioRepository repositoryUsuario;

    public void cadastrarReview(DadosCadastroReview dados){

        this.repositoryReview.save(new Review(dados));
    }
    public List listarPorRestaurante(Integer fkRestaurante) {
        List<Review> review = repositoryReview.findByFkRestaurante(fkRestaurante);
        List<Usuario> usuario = new ArrayList<>();
        List<ReviewDTO> rDTO = new ArrayList<>();
        for (Review r : review){
            Optional<Usuario> user = repositoryUsuario.findById(r.getFkUsuario());
            rDTO.add(new ReviewDTO(user.orElseThrow().getNome(), r.getFkRestaurante(), r.getFkUsuario(), r.getData_hora().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)), r.getDescricao(), r.getNt_comida(), r.getNt_ambiente(), r.getNt_atendimento()));
        }
        if (review.isEmpty()) {
            throw new NaoEncontradoException("Nenhuma review encontrado nesse restaurante");
        }
        return rDTO;
    }
}
