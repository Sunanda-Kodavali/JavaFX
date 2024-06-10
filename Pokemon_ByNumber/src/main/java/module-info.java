module dci.j24e01.pokemon_bynumber {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;


    opens dci.j24e01.pokemon_bynumber to javafx.fxml;
    exports dci.j24e01.pokemon_bynumber;
}