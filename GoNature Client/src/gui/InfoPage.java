package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import logic.Msg;
import logic.Park;
/**
 * this class is to show gui of information page
 * 
 * @author Shai Siso
 * @author Shahar Avital
 * @author Sean Friedman
 * @author Golan Yacobov
 * @author Matan Avraham
 * 
 * @version January 2021
 */
public class InfoPage implements Initializable{
/**
 * text area to show next park discounts
 */
    @FXML
    private TextArea parkDiscountTxt;
    /**
     * cBox to choose park
     */
	@FXML
	private ComboBox<String> cBox;
	/**
	 * list to cBox values
	 */
	ObservableList<String> list;
	/**
	 * text that show the park address
	 */
	@FXML
	private Text adrressTxt;
	/**
	 * ArrayList of all parks
	 */
	ArrayList<Park> allParks;
	/**
	 * set cBox values
	 */
	void setParkName() {
		Msg parksMsg=new Msg("Get all parks",null);
		ClientUI.chat.accept(parksMsg);
		allParks= Client.allParks;
		ArrayList<String> al = new ArrayList<String>();	
		for (Park p : allParks)
			al.add(p.getParkName());
		list = FXCollections.observableArrayList(al);
		cBox.setItems(list);
	}
	/**
	 * update info after choosing a park
	 * @param event 	selecting park from combox
	 */
	@FXML
	void parkSelect(ActionEvent event) {
		String parkName = cBox.getValue();
		Msg parkDetails = new Msg("Park details",parkName);
		ClientUI.chat.accept(parkDetails);
		adrressTxt.setText(getAddress(parkName));
		parkDiscountTxt.setText(Client.park.stDiscounts());
	}
/**
 * this method is to find the address of park
 * @param parkName	the name of the park
 * @return	address of park
 */
	private String getAddress(String parkName) {
		for (Park p : allParks)
			if (p.getParkName().equals(parkName))
				return p.getAddress();
		return "";
	}

/**
 * initial cBox
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setParkName();			
	}

}
