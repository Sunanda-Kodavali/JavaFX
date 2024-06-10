package dci.j24e01.pokemonslist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private Button button1;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TableView<Pokemon> tableView;

    ObjectMapper mapper;
    Request request;
    Response response;
    JsonNode root;


    public void initialize() {
        mapper = new ObjectMapper();
        TableColumn<Pokemon, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pokemon, Integer> heightColumn = new TableColumn<>("Height");
        heightColumn.setCellValueFactory(new PropertyValueFactory<>(String.valueOf("height")));

        TableColumn<Pokemon, Integer> weightColumn = new TableColumn<>("Weight");
        weightColumn.setCellValueFactory(new PropertyValueFactory<>(String.valueOf("weight")));

        TableColumn<Pokemon, Image> imageColumn = new TableColumn<>("Image");

        imageColumn.setCellValueFactory(new PropertyValueFactory<>("sprites"));
        imageColumn.setCellFactory(column -> {
            return new TableCell<>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Image item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        imageView.setImage(item);
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        setGraphic(imageView);
                    }
                }
            };
        });

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(heightColumn);
        tableView.getColumns().add(weightColumn);
        tableView.getColumns().add(imageColumn);
    }

    @FXML
    protected void onHelloButtonClick() {
        button1.setDisable(true);
        button1.setText("Loading...");

        List<Pokemon> pokemons = new ArrayList<>();

        Thread networkingThread = new Thread(() -> {

            OkHttpClient client = new OkHttpClient();
            for (int i = 1; i <= 20; i++) {
                request = new Request.Builder()
                        .url("https://pokeapi.co/api/v2/pokemon/" + i)
                        .build();

                System.out.println("ABOUT TO START A NETWORK REQUEST!");

                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(response.code());

                try {
                    root = mapper.readTree(response.body().byteStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(root.get("id").asText());

                pokemons.add(new Pokemon(
                        root.get("name").asText(),
                        root.get("height").asInt(),
                        root.get("weight").asInt(),
                        new Image(root.get("sprites").get("front_shiny").asText())));
                int counter = i;
                Platform.runLater(() -> {
                progressBar.setProgress(counter/20.0);
//
//                tableView.getItems().add(new Pokemon(
//                        root.get("name").asText(),
//                        root.get("height").asInt(),
//                        root.get("weight").asInt(),
//                        new Image(root.get("sprites").get("front_shiny").asText()))
//                );
                });

            }

            Platform.runLater(() -> {
                ObservableList<Pokemon> list = FXCollections.observableArrayList(pokemons);

                tableView.setItems(list);
                button1.setDisable(false);
                button1.setText("Hello!");
            });
        });

        networkingThread.start();
    }
}