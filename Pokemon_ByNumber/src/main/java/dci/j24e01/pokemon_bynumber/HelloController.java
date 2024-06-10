package dci.j24e01.pokemon_bynumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private ImageView frontShinyImage;

    @FXML
    private TextField field;

    @FXML
    private Button button1;

    ObjectMapper mapper;


    public void initialize(){
        mapper = new ObjectMapper();
    }
    @FXML
    protected void onHelloButtonClick() {
        button1.setDisable(true);
        button1.setText("Loading...");
        Thread networkingThread = new Thread(() -> {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://pokeapi.co/api/v2/pokemon/"+field.getText())
                    .build();

            System.out.println("ABOUT TO START A NETWORK REQUEST!");

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(response.code());


            JsonNode root = null;
            try {
                root = mapper.readTree(response.body().byteStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(root.get("id").asText());
            Pokemon pokemon = new Pokemon(root.get("name").asText(), root.get("height").asInt(), root.get("weight").asInt(), root.get("sprites").get("front_shiny").asText());


            Platform.runLater(() -> {
                welcomeText.setText("Name :"+pokemon.name()+"     height:"+pokemon.height()+"     weight:"+pokemon.weight());
                frontShinyImage.setImage(new Image(pokemon.sprites()));
                field.setText("");
                button1.setDisable(false);
                button1.setText("Hello!");
            });
        });

        networkingThread.start();
    }
}