module com.example.Scrabble {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.example.Scrabble.View to javafx.fxml;
    opens com.example.Scrabble.VM to javafx.fxml;

    exports com.example.Scrabble.View;
    exports com.example.Scrabble.VM;
}