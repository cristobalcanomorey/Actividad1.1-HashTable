package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		Double division = (double) productos.size() / prodPorPag;
		numDePags = (int) Math.ceil(division);
	}
	
	/***
	 * Devuelve un hash sin productos repetidos
	 * @param prod
	 * @return
	 */
	private Hashtable<Integer, Producto<?>> quitaRepetidos(Producto<?>[] prod){
//		Producto<?>[] invertido = invertir(prod, prod.length);
		ArrayList<Producto<?>> lista = new ArrayList<Producto<?>>(Arrays.asList(prod));
		ArrayList<Producto<?>> listaSinRepe = new ArrayList<Producto<?>>();		
		for (Producto<?> producto : lista) {
			if(!listaSinRepe.contains(producto)) {
				listaSinRepe.add(producto);
			}
		}
		
		return deArrayAHash(listaSinRepe);
	}
	
	private Hashtable<Integer, Producto<?>> deArrayAHash(ArrayList<Producto<?>> prod){
		Hashtable<Integer, Producto<?>> hProductos = new Hashtable<Integer, Producto<?>>();
		for (Producto<?> producto : prod) {
			hProductos.put(producto.getId(), producto);
		}
		return hProductos;
	}
	
	private Object[] invertir(Object[] array, int size){
		Integer[] b = new Integer[size]; 
        int j = size; 
        for (int i = 0; i < size; i++) { 
            b[j - 1] = (Integer) array[i]; 
            j = j - 1; 
        }
        return b;
	}
	
	private ArrayList<Producto<?>> deHashAArray(Hashtable<Integer, Producto<?>> hash){
		Set<Integer> ids = productos.keySet();
		Object[] array = ids.toArray();
		array = invertir(array, array.length);
		ArrayList<Producto<?>> arrayList = new ArrayList<Producto<?>>();
		for (Object id : array) {
//			System.out.println(id);
			arrayList.add(hash.get(id));
		}
		return arrayList;
	}
		
	/***
	 * Solo añade productos en el hash
	 * @param p Producto
	 */
	public boolean add(Producto<?> p){
		if(!productos.contains(p)) {
			productos.put(p.getId(), p);
			numDePags = findPageOf(p, deHashAArray(productos));
			return true;
		} else {
			return false;
		}
		
	}
	
	/***
	 * Elimina el producto de la tabla si está y actualiza el nº de páginas buscando
	 * la página del último elemento de la lista
	 * 
	 * @param p Producto
	 */
	public boolean remove(Producto<?> p) {
		if(contains(p)) {
			productos.remove(p.getId());
			ArrayList<Producto<?>> lista = deHashAArray(productos);
			numDePags = findPageOf(lista.get(lista.size() - 1), lista);
			return true;
		} else {
			return false;
		}
	}
	
	/***
	 * Busca si el producto está en la tabla
	 * @param p Producto
	 * @return True si p está en la tabla
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
	public Producto<?>[] getPage(int n) {
		int tamPag = prodPorPag;
		int primProd = n * prodPorPag;
		int ultProd = primProd + prodPorPag;
		if(ultProd>productos.size()) {
			ultProd = productos.size();
			tamPag = ultProd-primProd;
		}
		Producto<?>[] resul = new Producto<?>[tamPag];
		ArrayList<Producto<?>> lista = deHashAArray(productos);
		List pag = lista.subList(primProd, ultProd);
				
		for (int i = 0; i < pag.size(); i++) {
			resul[i] = (Producto<?>) pag.get(i);
		}
		
		return resul;
	}
	
	public int findPageOf(Producto<?>p) {
		return findPageOf(p,deHashAArray(productos));
	}
	
	private int findPageOf(Producto<?> p, ArrayList<Producto<?>> lista) {
		if(!lista.contains(p)) {
			return -1;
		} else {
			return (int) Math.ceil((double)(lista.indexOf(p) + 1) / prodPorPag);
		}
	}
	
	public int findPageOf(Producto<?> p, boolean orden) {
		ArrayList<Producto<?>> ordenados = ordenar(orden);
		return findPageOf(p, ordenados);
	}
	
	private ArrayList<Producto<?>> ordenar(boolean menAMay) {
		ArrayList<Producto<?>> ordenados = deHashAArray(productos);
		if (menAMay) {
			Collections.sort(ordenados);
			return ordenados;
		} else {
			Collections.reverse(ordenados);
			return ordenados;
		}
	}
	
	public int getTotalPages() {
		
		return numDePags;
	}
	public int size() {
		return productos.size();
	}
	
}