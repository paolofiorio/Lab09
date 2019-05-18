/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

public class BordersController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
    private ComboBox<Country> cmbNazione;
	@FXML
	void doCalcolaConfini(ActionEvent event) {
		txtResult.clear();
		int anno;
		
		try{
			anno= Integer.parseInt(txtAnno.getText());
			if(anno>2016 ||anno<1816) {
				txtResult.setText("Errore: inserire un anno compreso tra 1816 e 2016");
				return;}
			}
		catch (NumberFormatException e) {
				txtResult.setText("Errore: inserire un anno in cifre");
				return;
			}
			
			
		try {	
			model.creaGrafo(anno);
			List<Country> countries = model.getCountries();
			cmbNazione.getItems().addAll(countries);
			
			txtResult.appendText(String.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents()));
			Map<Country, Integer> stats = model.getCountryCounts();
			for (Country country : stats.keySet())
				txtResult.appendText(String.format("%s %d\n", country, stats.get(country)));

				
		} catch (RuntimeException e) {
			txtResult.setText("Errore");
		}
	}
	 @FXML
	 void doTrovaTuttiVicini(ActionEvent event) {
		 txtResult.clear();

			if (cmbNazione.getItems().isEmpty()) {
				txtResult.setText("Grafo pieno. Crea un grafo o seleziona un nuovo anno");
			}

			Country selectedCountry = cmbNazione.getSelectionModel().getSelectedItem();
			if (selectedCountry == null) {
				txtResult.setText("Seleziona una nazione.");
			}

			try {
				List<Country> reachableCountries = model.getReachableCountries(selectedCountry);
				for (Country country : reachableCountries) {
					txtResult.appendText(String.format("%s\n", country));
				}
			} catch (RuntimeException e) { 
				txtResult.setText("Nazione selezionata non presente nel grafo");
	}
	    }
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
		assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Borders.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
	}
  
	public void setModel(Model model) {
		this.model=model;
	}
}
