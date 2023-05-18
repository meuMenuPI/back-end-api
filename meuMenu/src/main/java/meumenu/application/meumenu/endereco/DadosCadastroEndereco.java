package meumenu.application.meumenu.endereco;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroEndereco(
        Integer fk_restaurante,
        Integer fk_usuario,
        String complemento,
        Integer numero,
        String cep

) {
}
