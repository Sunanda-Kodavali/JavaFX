module dci.j24e01.pokemonslist {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;


    opens dci.j24e01.pokemonslist to javafx.fxml;
    exports dci.j24e01.pokemonslist;
}