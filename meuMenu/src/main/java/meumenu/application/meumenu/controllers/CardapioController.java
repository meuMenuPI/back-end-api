package meumenu.application.meumenu.controllers;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import meumenu.application.meumenu.cardapio.Cardapio;
import meumenu.application.meumenu.cardapio.CardapioRepository;
import meumenu.application.meumenu.cardapio.DadosCadastroCardapio;
import meumenu.application.meumenu.enums.Especialidade;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meumenu/cardapios")
public class CardapioController {
    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @PostMapping
    @Operation(summary = "Metodo de cadastrar prato", description = "Create Prato MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso prato criado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso prato criado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio> cadastrar(@RequestBody @Valid DadosCadastroCardapio dados){
        cardapioRepository.save(new Cardapio(dados));
        List<Cardapio> cardapios = cardapioRepository.findAll();
        Cardapio cardapio = new Cardapio(cardapios.get(cardapios.size()-1).getId(),dados.fk_restaurante() ,dados.nome(), dados.preco(), dados.estiloGastronomico(),  dados.descricao());
        return ResponseEntity.status(200).body(cardapio);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar todos os pratos do restaurante", description = "List cardapio", responses = {@ApiResponse(responseCode = "200", description = "Sucesso lista retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada!\"}"),})), @ApiResponse(responseCode = "204", description = "Sucesso lista retornada mas vazia!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204', \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada mas vazia!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<Cardapio>> listar(@RequestParam Integer id){
        List<Cardapio> cardapios = cardapioRepository.findByRestauranteLista(id);
        if(cardapios.isEmpty()){
            return ResponseEntity.status(204).build();
    }
        return ResponseEntity.status(200).body(cardapios);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Metodo de atualizar dados do prato", description = "Atualiza prato por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso prato atualizado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso prato atualizado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio> atualizar(@PathVariable int id, @RequestBody @Valid Cardapio dados) {
        if (cardapioRepository.existsById(id)) {
            Cardapio cardapio = cardapioRepository.findById(id).orElseThrow();
            if (dados.getNome() != null) {
                cardapio.setNome(dados.getNome());
            }
            if (dados.getPreco() != null) {
                cardapio.setPreco(dados.getPreco());
            }
            if (dados.getEstiloGastronomico() != null) {
                cardapio.setEstiloGastronomico(dados.getEstiloGastronomico());
            }
            if (dados.getDescricao() != null) {
                cardapio.setDescricao(dados.getDescricao());
            }

            cardapioRepository.save(cardapio);
            return ResponseEntity.status(200).body(cardapio);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Metodo de deletar prato por id", description = "Deletar o prato do restaurante especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o prato!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso deletou o prato!\"}"),})), @ApiResponse(responseCode = "404", description = "Prato não encontrado", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 404, \"Status\" : \"Erro\", \"Message\" :\"Prato não encontrado\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<String> deletar(@PathVariable int id) {
        if (cardapioRepository.existsById(id)) {
            Cardapio cardapio = cardapioRepository.findById(id).orElseThrow();
            cardapioRepository.delete(cardapio);
            return ResponseEntity.status(200).body("Prato deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Prato não encontrado");
    }

    @GetMapping("/ordernar/preco-crescente")
    @Operation(summary = "Metodo de ordenar prato preço crescente", description = "Ordenar pratos do restaurante por preço crescente", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o prato!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso!\"}"),})), @ApiResponse(responseCode = "204", description = "Nenhum prato cadastrado no seu restaurante!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204, \"Status\" : \"Erro\", \"Message\" :\"Nenhum prato cadastrado no seu restaurante!\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio[]> ordenarPrecoCrescente(@RequestParam Integer id){
        Cardapio[] cardapio = cardapioRepository.findByRestauranteVetor(id);
        if(cardapio.length == 0){
            return ResponseEntity.status(204).build();
        }
        boolean troca = true;
        Cardapio aux;
        while (troca) {
            troca = false;
            for (int i = 0; i < cardapio.length - 1; i++) {
                if (cardapio[i].getPreco() > cardapio[i + 1].getPreco()) {
                    aux = cardapio[i];
                    cardapio[i] = cardapio[i + 1];
                    cardapio[i + 1] = aux;
                    troca = true;
                }
            }
        }
        return ResponseEntity.status(200).body(cardapio);
    }

    @GetMapping("/ordernar/preco-decrescente")
    @Operation(summary = "Metodo de ordenar prato preço decrescente", description = "Ordenar pratos do restaurante por preço decrescente", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o prato!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso!\"}"),})), @ApiResponse(responseCode = "204", description = "Nenhum prato cadastrado no seu restaurante!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204, \"Status\" : \"Erro\", \"Message\" :\"Nenhum prato cadastrado no seu restaurante!\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio[]> ordenarPrecoDecrescente(@RequestParam Integer id){
        Cardapio[] cardapio = cardapioRepository.findByRestauranteVetor(id);
        if(cardapio.length == 0){
            return ResponseEntity.status(204).build();
        }
        boolean troca = true;
        Cardapio aux;
        while (troca) {
            troca = false;
            for (int i = 0; i < cardapio.length - 1; i++) {
                if (cardapio[i].getPreco() < cardapio[i + 1].getPreco()) {
                    aux = cardapio[i];
                    cardapio[i] = cardapio[i + 1];
                    cardapio[i + 1] = aux;
                    troca = true;
                }
            }
        }
        return ResponseEntity.status(200).body(cardapio);
    }

    //Método upload txt
    @PostMapping("/upload/txt")
    public String upload(@RequestParam MultipartFile arquivo){
        Path caminho = Path.of(System.getProperty("java.io.tmpdir") + "/arquivos");
        salvar(arquivo);
        Path caminhoTeste = Path.of(System.getProperty("java.io.tmpdir") + "/arquivos/" + arquivo.getOriginalFilename());
        leArquivoTxt(caminhoTeste);
        return "Salvo com sucesso";
    }

    public void salvar(MultipartFile arquivo){
        Path diretorioPath;
        if(System.getProperty("os.name").contains("Windows")){
            diretorioPath = Path.of(System.getProperty("java.io.tmpdir") + "/arquivos");
        }else{
            diretorioPath = Path.of(System.getProperty("user.dir") + "/arquivos");
        }
        Path arquivoPath = diretorioPath.resolve(arquivo.getOriginalFilename());

        try {
            Files.createDirectories(diretorioPath);
            arquivo.transferTo(arquivoPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Erro em salvar");
        }
    }

    public void leArquivoTxt(Path arquivo) {
        BufferedReader entrada = null;
        String registro, tipoRegistro;
        String ra, nome, curso, disciplina;
        Double media;
        int qtdFalta;
        int contaRegDadoLido = 0;
        int qtdRegDadoGravado;
        Restaurante optionalRestaurante;
        int idRestaurante = 0;
        String nomeArq = arquivo.toString();

        //nomeArq += ".txt";

//        List<Aluno> listaLida = new ArrayList<>();
        // try-catch para abrir o arquivo
        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        }
        catch (IOException erro) {
            System.out.println("Erro na abertura do arquivo");
            System.exit(1);
        }

        // try-catch para leitura do arquivo
        try {
            registro = entrada.readLine(); // le o primeiro registro do arquivo

            while (registro != null) {
                // 01234567890
                // 00NOTA20231
                tipoRegistro = registro.substring(0,2);     // obtem os 2 primeiros caracteres do registro
                // substring - primeiro argumento é onde começa a substring dentro da string
                // e o segundo argumento é onde termina a substring + 1
                // Verifica se o tipoRegistro é um header, ou um trailer, ou um registro de dados
                if (tipoRegistro.equals("00")) {
                    System.out.println("é um registro de header");
                    System.out.println("Tipo de arquivo: " + registro.substring(2,6));
                    System.out.println("Ano/semestre: " + registro.substring(6,11));
                    System.out.println("Data e hora da gravação do arquivo: " + registro.substring(11,30));
                    System.out.println("Versão do documento de layout: " + registro.substring(30,32));
                }
                else if (tipoRegistro.equals("01")) {
                    System.out.println("é um registro de trailer");
                    qtdRegDadoGravado = Integer.parseInt(registro.substring(2,12));
                    if (contaRegDadoLido == qtdRegDadoGravado) {
                        System.out.println("Quantidade de registros lidos compatível com " +
                                "quantidade de registros gravados");
                    }
                    else {
                        System.out.println("Quantidade de registros lidos incompatível com " +
                                "quantidade de registros gravados");
                    }
                }
                else if(tipoRegistro.equals("02")){
                    String cnpj = registro.substring(2,16).trim();
                    optionalRestaurante = restauranteRepository.findByCnpj(cnpj);
                    idRestaurante = optionalRestaurante.getId();
                }
                else if (tipoRegistro.equals("03")) {

                    nome = registro.substring(2,36).trim();
                    Double preco =  Double.valueOf(registro.substring(36,43).replace(',','.'));
                    String especialidadeString = registro.substring(43,53).trim();
                    Especialidade especialidadeEnum = Especialidade.valueOf(especialidadeString);
                    String descricao = registro.substring(55,152).trim();
                    List<Cardapio> cardapios = cardapioRepository.findAll();
                    Cardapio cardapioNovo = new Cardapio(cardapios.size() + 1,idRestaurante , nome, preco, especialidadeEnum, descricao);
                    List<Cardapio> lista = new ArrayList<>();
                    lista.add(cardapioNovo);
                    cardapioRepository.save(cardapioNovo);

                    contaRegDadoLido++;
                }
                else {
                    System.out.println("tipo de registro inválido");
                }
                // le o proximo registro do arquivo
                registro = entrada.readLine();
            }
            entrada.close();
        }
        catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo");
        }

        String nomeTeste = arquivo.toString();
        File arquivoDelete = new File(nomeTeste);
        arquivoDelete.delete();
    }

    // Método download txt

    @GetMapping("/download/txt")
    public ResponseEntity<String> downloadTxtFile() throws IOException {
        // Gera o conteúdo do arquivo TXT
        String content = "Este é um arquivo de texto gerado dinamicamente.";

        // Define o nome do arquivo
        String filename = "Pratos_Gerais.txt";

        // Obtém o diretório de downloads do usuário
        String userHome = System.getProperty("user.home");
        String downloadsPath = userHome + "/Downloads/";

        List<Cardapio> lista = cardapioRepository.findAll();

        // Cria o arquivo TXT
        File file = new File(downloadsPath + filename);
        try (FileWriter writer = new FileWriter(file)) {
            gravaArquivoTxt(lista, file.getAbsolutePath());
            //writer.write(content);
        }

        // Move o arquivo para o diretório de downloads
        Path sourcePath = file.toPath();
        Path destinationPath = Path.of(downloadsPath, filename);
        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // Cria um objeto Resource com base no arquivo
        Resource resource = new FileSystemResource(destinationPath.toFile());

        // Configura o cabeçalho de resposta
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        return ResponseEntity.status(200).body("Download executado com sucesso");

    }

    public static void gravaArquivoTxt(List<Cardapio> lista, String nomeArq) {
        //nomeArq += ".txt";
        int contaRegistroDado = 0;

        // Monta o registro de header
        String header = "00CARDAPIO20231";
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        header += "01";

        // Grava o registro de header
        gravaRegistro(header, nomeArq);

        // Monta e grava os registros de dados ou registros de corpo
        String corpo;
        for (Cardapio a : lista) {
            corpo = "03";
            corpo += String.format("%-34.34s",a.getNome());
            corpo += String.format("%07.2f",a.getPreco());
            corpo += String.format("%-12.12s",a.getEstiloGastronomico());
            corpo += String.format("%-100.100s",a.getDescricao());

            gravaRegistro(corpo, nomeArq);
            contaRegistroDado++;
        }

        // Monta e grava o registro de trailer
        String trailer = "01";
        trailer += String.format("%010d",contaRegistroDado);
        gravaRegistro(trailer, nomeArq);

    }


    public static void gravaRegistro(String registro, String nomeArq) {
        BufferedWriter saida = null;

        // try-catch para abrir o arquivo
        try {
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        }
        catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo" + erro);
            System.exit(1);
        }

        // try-catch para gravar o registro e finalizar
        try {
            saida.append(registro + "\n");
            saida.close();
        }
        catch (IOException erro) {
            System.out.println("Erro ao gravar no arquivo");
        }
    }

    @PostMapping("/foto-prato/{id}")
    public ResponseEntity<Boolean> cadastroFoto(@PathVariable int id, @RequestParam MultipartFile imagem) throws IOException {

        byte[] bytes = imagem.getBytes();
        if (bytes.length == 0){
            throw new IOException("Imagem não contem bytes");
        }

        Optional<Cardapio> prato = cardapioRepository.findById(id);

        if (prato.isEmpty()) {
            throw new RuntimeException("prato não encontrado");
        }

        String fileName = LocalDateTime.now() + imagem.getOriginalFilename();

        String constr = "DefaultEndpointsProtocol=https;AccountName=meumenuimagens;AccountKey=R9lel0MHe6" +
                "BQTZj3c7dDQYXKKGiC75NpsmLi/IqBChb4NAGFT5kheiorbVyx/pSAo9VC5e/Ktkju+AStGIYs7w==;Endpoint" +
                "Suffix=core.windows.net";

        BlobContainerClient container = new BlobContainerClientBuilder()
                .connectionString(constr).containerName("pratos").buildClient();

        BlobClient blob = container.getBlobClient(fileName);


        Response<BlockBlobItem> response = blob.uploadWithResponse(
                new BlobParallelUploadOptions(new ByteArrayInputStream(bytes), bytes.length),
                Duration.ofHours(5),
                null);

        if (response.getStatusCode() != 201) {
            throw new IOException("request failed");
        }

        prato.get().setFotoPrato(fileName);

        cardapioRepository.save(prato.get());

        return ResponseEntity.status(200).body(true);
    }

}
