package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Label resultLabel;
    private TextField expressionField;

    @Override
    public void start(Stage primaryStage) {
        // ラベル作成
        resultLabel = new Label("計算式を入力してください");
        resultLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        
        // 入力フィールド設定
        expressionField = new TextField();
        expressionField.setPromptText("例: 3 + 5 =");
        expressionField.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 16;");

        // 演算子ボタン
        Button plusBtn = createOperatorButton("+");
        Button minusBtn = createOperatorButton("-");
        Button multiplyBtn = createOperatorButton("*");
        Button divideBtn = createOperatorButton("/");
        
        // クリアボタン（新規追加）
        Button clearBtn = new Button("C");
        clearBtn.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> {
            expressionField.clear();
            resultLabel.setText("計算式を入力してください");
            expressionField.requestFocus(); // 入力フィールドにフォーカスを戻す
        });

        // ボタンレイアウト
        HBox operatorBox = new HBox(10, plusBtn, minusBtn, multiplyBtn, divideBtn);
        HBox controlBox = new HBox(10, clearBtn); // クリアボタン用のレイアウト
        operatorBox.setAlignment(Pos.CENTER);
        controlBox.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15, resultLabel, expressionField, operatorBox, controlBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // 入力監視
        setupExpressionListener();

        // シーン作成
        Scene scene = new Scene(root, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Keyboard Calculator");
        primaryStage.show();
    }

    private Button createOperatorButton(String operator) {
        Button btn = new Button(operator);
        btn.setOnAction(e -> {
            String current = expressionField.getText();
            expressionField.setText(current + " " + operator + " ");
            expressionField.end();
        });
        btn.setStyle("-fx-font-size: 14; -fx-min-width: 40;");
        return btn;
    }

    private void setupExpressionListener() {
        expressionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.endsWith("=") && !oldValue.endsWith("=")) {
                Platform.runLater(() -> {
                    String expression = newValue.substring(0, newValue.length() - 1).trim();
                    expressionField.setText(expression + " = ");
                    expressionField.positionCaret(expressionField.getText().length());
                    
                    try {
                        double result = evaluateExpression(expression);
                        resultLabel.setText(String.format("結果: %.2f", result));
                    } catch (Exception e) {
                        resultLabel.setText("エラー: 正しい式を入力してください");
                    }
                });
            }
        });
    }

    private double evaluateExpression(String expression) {
        String[] parts = expression.split(" ");
        if (parts.length != 3) throw new IllegalArgumentException();

        double a = Double.parseDouble(parts[0]);
        double b = Double.parseDouble(parts[2]);
        String op = parts[1];

        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": 
                if (b == 0) throw new ArithmeticException("0で除算できません");
                return a / b;
            default: throw new IllegalArgumentException("不明な演算子");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}