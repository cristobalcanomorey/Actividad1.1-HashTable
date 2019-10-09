package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Paginable <E extends Producto> {

	/*
	 * add()					
	 * remove()					
	 * contains()				
	 * getPage(int index)		
	 * findPageOf(Producto p)	
	 * int getTotalPages()		
	 * size()					
	 * */
		
	private Hashtable<Integer, Producto<?>> productos = new Hashtable<Integer, Producto<?>>();
	private int prodPorPag = 3;
	private int numDePags = 0;
	
	/***
	 * Crea un paginable transformando un array de productos a un HashTable sin productos repetidos
	 * @param prod Array de productos
	 */
	Paginable(Producto<?>[] prod){
		this.productos = quitaRepetidos(prod);
		numDePags = (int) Math.ceil(productos.size()/prodPorPag);
	}
	
	/***
	 * Devuelve un hash sin productos repetidos
	 * @param prod
	 * @return
	 */
	private Hashtable<Integer, Producto<?>> quitaRepetidos(Producto<?>[] prod){
		ArrayList<Producto<?>> lista = new ArrayList<Producto<?>>(Arrays.asList(prod));
		ArrayList<Producto<?>> listaSinRepe = new ArrayList<Producto<?>>();		
		for (Producto<?> producto : lista) {
			if(!listaSinRepe.contains(producto)) {
				listaSinRepe.add(producto);
			}
		}
		
		Hashtable<Integer, Producto<?>> hProductos = new Hashtable<Integer, Producto<?>>();
		for (Producto<?> producto : listaSinRepe) {
			hProductos.put(producto.getId(), producto);
		}
		return hProductos;
	}
		
	/***
	 * Solo añade productos en el array
	 * @param p Producto
	 */
	public void add(Producto<?> p){
		if(!productos.contains(p)) {
			productos.put(p.getId(), p);
		}
		numDePags = findPageOf(p);
	}
	
	/***
	 * Elimina el producto del array
	 * @param p Producto
	 */
//	public void remove(Producto<?> p) {
//		if(contains(p)) {
//			productos.remove(p);
//		}
//		
//		numDePags--;
//	}
	
	/***
	 * Busca si el producto está en el array
	 * @param p Producto
	 * @return True si p está en el array
	 */
	public boolean contains(Producto<?> producto) {
		boolean resul = false;
		Set<Integer> ids = productos.keySet();
		for(Integer id: ids) {
			if(resul) {
				break;
			} else {
				resul = producto.equals(productos.get(id));
			}
		}
		return resul;
	}
	
	/***
	 * Calcula que productos hay en la página pasada por parámetro y los devuelve como un array
	 * @param n Número de página
	 * @return Devuelve array de productos
	 */
//	public Producto<?>[] getPage(int n) {
//		int tamPag = prodPorPag;
//		int primProd = n * prodPorPag;
//		int ultProd = primProd + prodPorPag;
//		if(ultProd>productos.size()) {
//			ultProd = productos.size();
//			tamPag = ultProd-primProd;
//		}
//		Producto<?>[] resul = new Producto<?>[tamPag];
//		List pag = productos.subList(primProd, ultProd);
//				
//		for (int i = 0; i < pag.size(); i++) {
//			resul[i] = (Producto<?>) pag.get(i);
//		}
////		
////		resul = (Producto<?>[]) pag.toArray();
//		
//		return resul;
//	}
	
	public int findPageOf(Producto<?> p) {
		if(!productos.contains(p)) {
			return -1;
		} else {
			productos.
		}
	}
	
//	private int 
	
	public int getTotalPages() {
		
		return numDePags;
	}
	public int size() {
		return productos.size();
	}
	
}