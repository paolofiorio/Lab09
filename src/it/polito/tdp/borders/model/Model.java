package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private BordersDAO dao;
	private List<Country> countries;
	private CountryIdMap countryIdMap;
	private SimpleGraph<Country, DefaultEdge> grafo;
	
	public Model() {
		dao= new BordersDAO();
	}
	public void creaGrafo(int anno) {
		countryIdMap = new CountryIdMap();
		countries = dao.loadAllCountries(countryIdMap);

		List<Border> confini = dao.getCountryPairs(countryIdMap, anno);

		if (confini.isEmpty()) {
			throw new RuntimeException("Nessuna coppia di stati per questo anno");
		}

		grafo = new SimpleGraph<>(DefaultEdge.class);

		for (Border b : confini) {
			grafo.addVertex(b.getC1());
			grafo.addVertex(b.getC2());
			grafo.addEdge(b.getC1(), b.getC2());
		}

		System.out.format("Inseriti: %d vertici, %d archi\n", grafo.vertexSet().size(), grafo.edgeSet().size());

		// Sort the countries
		countries = new ArrayList<>(grafo.vertexSet());
		//Collections.sort(countries);
		
	}
	public List<Country> getCountries() {
		if (countries == null) {
			return new ArrayList<Country>();
		}

		return countries;
}
	public Map<Country, Integer> getCountryCounts() {
		if (grafo == null) {
			throw new RuntimeException("Grafo non esistente");
		}

		Map<Country, Integer> stats = new HashMap<Country, Integer>();
		for (Country country : grafo.vertexSet()) {
			stats.put(country, grafo.degreeOf(country));
		}
		return stats;
	}

	public int getNumberOfConnectedComponents() {
		if (grafo == null) {
			throw new RuntimeException("Grafo non esistente");
		}

		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<Country, DefaultEdge>(grafo);
		return ci.connectedSets().size();
	}
	
	public List<Country> getReachableCountries(Country selectedCountry) {

		if (!grafo.vertexSet().contains(selectedCountry)) {
			throw new RuntimeException("Selected Country not in graph");
		}

		
		List<Country> reachableCountries = this.displayAllNeighboursJGraphT(selectedCountry);
		System.out.println("Reachable countries: " + reachableCountries.size());
		

		return reachableCountries;
	}
	private List<Country> displayAllNeighboursJGraphT(Country selectedCountry) {

		List<Country> visited = new LinkedList<Country>();

		GraphIterator<Country, DefaultEdge> dfv = new DepthFirstIterator<Country, DefaultEdge>(grafo, selectedCountry);
		while (dfv.hasNext()) {
			visited.add(dfv.next());
		}

		return visited;
	}




}
