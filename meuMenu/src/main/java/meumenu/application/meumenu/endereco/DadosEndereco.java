package meumenu.application.meumenu.endereco;

import jakarta.validation.constraints.NotBlank;

public record DadosEndereco(
        //validação atraves do "validation" metodo do spring
        @NotBlank
        String logradouro,
        @NotBlank
        String bairro,
        @NotBlank
        String cep,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        String complemento,
        String numero) {
}
