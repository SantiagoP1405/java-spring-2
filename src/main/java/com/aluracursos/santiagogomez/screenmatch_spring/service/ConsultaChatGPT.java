package com.aluracursos.santiagogomez.screenmatch_spring.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
//
public class ConsultaChatGPT {
    public static String obtenerTraduccion(String texto) {
        String apikey = "EJEMPLO API";
        OpenAiService service = new OpenAiService(apikey);

        CompletionRequest requisicion = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduce a español el siguiente texto: " + texto)
                .maxTokens(500)
                .temperature(0.7)
                .build();

        var respuesta = service.createCompletion(requisicion);
        return respuesta.getChoices().get(0).getText();
    }
}
