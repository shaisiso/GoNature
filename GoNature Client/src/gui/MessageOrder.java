package gui;

import assistance.InputController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Order;
import logic.Visitor;

/**
 * this class is for the message simulation after an order was approved
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 *
 */
public class MessageOrder {
/**
 * text to show sent email
 */
    @FXML
    private Label emailTxt;
    /**
     * text to show sent phone
     */
    @FXML
    private Label phoneTxt;
    /**
     * text to show order id
     */
    @FXML
    private Text orderIdTxt;
    /**
     * text to show the date
     */
    @FXML
    private Text dateTxt;
    /**
     * text to show the hour
     */
    @FXML
    private Text timeTxt;
    /**
     * text to show the number of visitors
     */
    @FXML
    private Text visitorsTxt;
    /**
     * text to show the park name
     */
    @FXML
    private Text parkTxt;
    /**
     * text to show the visitors id
     */
    @FXML
    private Text visitorIDtxt;
    /**
     * text to write inside
     */
    @FXML
    private Text text1;
    /**
     * text to write inside
     */
    @FXML
    private Text text2;

/**
 * this method load details of order and visitors to show on screen    
 * @param order		order that just approved
 * @param visitor	visitor details
 */
    public void loadDetails(Order order,Visitor visitor) {
    	orderIdTxt.setText(String.valueOf(order.getOrderID()));	
		parkTxt.setText(order.getParkName());
		visitorsTxt.setText(String.valueOf(order.getNumberOfVisitors()));
		dateTxt.setText(InputController.getIsraelFormatDate(order.getDate()));
		timeTxt.setText(order.getHour()+":00");
		emailTxt.setText("Sent to email: "+order.getEmail());
		visitorIDtxt.setText(visitor.getId());
		if (visitor.getPhone()!=null)
			phoneTxt.setText("Sent to phone number: "+visitor.getPhone());
		text1.setText("Your order has been placed");
		
	}
/**
 * upload this page
 * with sound of notification
 * @param root 	parent to set in scene
 */
	public void start(Parent root) {
		AudioClip note = new AudioClip(this.getClass().getResource("NotificationSound.wav").toString());
		note.play();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Message simulation");
		primaryStage.setScene(scene);
		primaryStage.show();		
	}
	

}
