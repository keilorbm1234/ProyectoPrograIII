package resourcemanager.logic;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.time.LocalDate;

public class ReservaIAService {
    private static ReservaIAService instance;
    private final ReservaExtractorService extractorService;
    private final CategoriaService categoriaService;

    private ReservaIAService(){
        ChatLanguageModel modelo = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        this.extractorService = AiServices.create(ReservaExtractorService.class, modelo);
        this.categoriaService = CategoriaService.getInstance();
    }

    public static synchronized ReservaIAService getInstance(){
        if(instance == null){
            instance = new ReservaIAService();
        }
        return instance;
    }

    public ReservaExtraccion extraerDatosReserva(String frase) throws Exception{
        if(frase == null || frase.trim().isEmpty()){
            throw new ValidationException("Debe ingresar una frase para usar la extracción con IA.");
        }
        String categoriasTexto = String.join(", ", categoriaService.getNombreCategorias());
        String fechaHoy = LocalDate.now().toString();

        try {
            ReservaExtraccion datos = extractorService.extraer(frase, categoriasTexto, fechaHoy);
            if(datos == null){
                throw new ValidationException("La IA no ha podido extraer la frase.");
            }
            return datos;
        } catch (ValidationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ValidationException("No se pudo comunicar con el servicio de IA.");

        }
    }
}
