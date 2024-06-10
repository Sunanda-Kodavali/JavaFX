module dci.j24e01.pkemon_info {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;


    opens dci.j24e01.pkemon_info to javafx.fxml;
    exports dci.j24e01.pkemon_info;
}