import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;


public class Calculator extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Calculator.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private Button addBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Button closebtn;

    @FXML
    private Button decimalBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button divideBtn;

    @FXML
    private Button eightBtn;

    @FXML
    private Button equalBtn;

    @FXML
    private Button exponentBtn;

    @FXML
    private Button fiveBtn;

    @FXML
    private Button fourBtn;

    @FXML
    private Button minusBtn;

    @FXML
    private Button multiplyBtn;

    @FXML
    private Button nineBtn;

    @FXML
    private TextField notationField;

    @FXML
    private Button oneBtn;

    @FXML
    private Button openBtn;

    @FXML
    private Button sevenBtn;

    @FXML
    private Button sixBtn;

    @FXML
    private Button threeBtn;

    @FXML
    private Button twoBtn;

    @FXML
    private Button zeroBtn;

    @FXML
    private TextArea historyField;

    @FXML
    private ScrollPane scrollPane;


    List<String> history = new ArrayList<>();

    @FXML
    public void numberAndOperationButtons(ActionEvent e){
        String character = "";
        //Digits
        if(e.getSource()==oneBtn){
            character = "1";
        }else if(e.getSource()==twoBtn){
            character = "2";
        } else if(e.getSource()==threeBtn){
            character = "3";
        }else if(e.getSource()==fourBtn){
            character = "4";
        }else if(e.getSource()==fiveBtn){
            character = "5";
        }else if(e.getSource()==sixBtn){
            character = "6";
        }else if(e.getSource()==sevenBtn){
            character = "7";
        }else if(e.getSource()==eightBtn){
            character = "8";
        }else if(e.getSource()==nineBtn){
            character = "9";
        } else if(e.getSource()==zeroBtn){
            character = "0";
        }else if(e.getSource()==decimalBtn){
            character = ".";
        }

        //Operation
        if(e.getSource()==addBtn){
            character = "+";
        }else if(e.getSource()==minusBtn){
            character = "-";
        } else if(e.getSource()==multiplyBtn){
            character = "*";
        }else if(e.getSource()==divideBtn){
            character = "/";
        }else if(e.getSource()==exponentBtn){
            character = "^";
        }else if(e.getSource()==openBtn){
            character = "(";
        }else if(e.getSource()==closebtn){
            character = ")";
        }
        notationField.setText(notationField.getText()+character);
    }


    @FXML
    public void utilityButtons(ActionEvent e) {
        if (e.getSource()==equalBtn){
            String input = notationField.getText(); //Gets the Input from text-field
            CalculatorLogic calculate = new CalculatorLogic();
            String result = calculate.callProcessMethods(input); //get the result from the processMethods which returns a computed equation
            notationField.setText(result);//set the text-field to result
            notationField.positionCaret(notationField.getLength());//set the focus to the last character of text-field
            if(!result.equals("Error")&&!input.replaceAll(",","").equals(result.replaceAll(",",""))){
                //condition that does not display error in the history
                history.add(input+"\n ="+result);
                historyField.setText(String.join("\n",history));
            }

        }else if(e.getSource()==deleteBtn){
            int len = notationField.getText().length();
            String deleted = notationField.getText(0,len-1);
            notationField.setText(deleted);
        }else if(e.getSource()==clearBtn){
            notationField.setText("");
        }

    }


    @FXML
    public void keyListeners(KeyEvent e){
        if(e.getCode()==KeyCode.ENTER){
            equalBtn.fire();
        }
        notationField.requestFocus();
    }



}
