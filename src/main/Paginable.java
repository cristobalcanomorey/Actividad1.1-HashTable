package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Paginable<E extends Producto> {

	private Hashtable<Integer, Producto<?>> productos = new Hashtable<Integer, Producto<?>>();
	private int prodPorPag = 3;
	private int numDePags = 0;

	/***
	 * Crea un paginable transformando un array de productos a un HashTable sin
	 * productos repetidos
	 * 
	 * @param prod Array de productos
	 */
	Paginable(Producto<?>[] prod) {
		this.productos = quitaRepetidos(prod);
		Double division = (double) productos.size() / prodPorPag;
		numDePags = (int) Math.ceil(division);
	}

	/***
	 * Devuelve un Hashtable sin productos repetidos
	 * 
	 * @param prod Array de productos que puede contener repetidos
	 * @return Hashtable de productos únicos
	 */
	private Hashtable<Integer, Producto<?>> quitaRepetidos(Producto<?>[] prod) {
		ArrayList<Producto<?>> lista = new ArrayList<Producto<?>>(Arrays.asList(prod));
		ArrayList<Producto<?>> listaSinRepe = new ArrayList<Producto<?>>();
		for (Producto<?> producto : lista) {
			if (!listaSinRepe.contains(producto)) {
				listaSinRepe.add(producto);
			}
		}

		return deArrayAHashTable(listaSinRepe);
	}

	/***
	 * Añade productos en el array si no está repetida y actualiza el nº de páginas
	 * buscando la página del último producto añadido
	 * 
	 * @param p Producto que intenta añadir
	 * @return True si ha funcionado, false si no
	 */
	public boolean add(Producto<?> p) {
		if (!productos.contains(p)) {
			productos.put(p.getId(), p);
			numDePags = findPageOf(p, deHashAArray(productos));
			return true;
		} else {
			return false;
		}

	}

	/***
	 * Crea un Hashtable y le mete los productos del ArrayList
	 * 
	 * @param prod ArrayList de productos
	 * @return Hashtable de productos
	 */
	private Hashtable<Integer, Producto<?>> deArrayAHashTable(ArrayList<Producto<?>> prod) {
		Hashtable<Integer, Producto<?>> hProductos = new Hashtable<Integer, Producto<?>>();
		for (Producto<?> producto : prod) {
			hProductos.put(producto.getId(), producto);
		}
		return hProductos;
	}

	/***
	 * Crea un ArrayList y le mete los productos del Hashtable. Es necesario
	 * invertir el orden del hashtable para que el output sea el mísmo que en los
	 * otros proyectos
	 * 
	 * @param hash Hashtable de productos
	 * @return ArrayList de productos
	 */
	private ArrayList<Producto<?>> deHashAArray(Hashtable<Integer, Producto<?>> hash) {
		Set<Integer> ids = productos.keySet();
		Object[] array = ids.toArray();
		array = invertir(array, array.length);
		ArrayList<Producto<?>> arrayList = new ArrayList<Producto<?>>();
		for (Object id : array) {
			arrayList.add(hash.get(id));
		}
		return arrayList;
	}

	/***
	 * Invierte el orden de un array de objetos
	 * 
	 * @param array Array con las ids de los productos
	 * @param size  Tamaño del array
	 * @return Array con las ids de los productos en el orden contrario
	 */
	private Object[] invertir(Object[] array, int size) {
		Integer[] b = new Integer[size];
		int j = size;
		for (int i = 0; i < size; i++) {
			b[j - 1] = (Integer) array[i];
			j = j - 1;
		}
		return b;
	}

	/***
	 * Elimina el producto del Hashtable si existe y actualiza el nº de páginas
	 * buscando la página del último elemento de la lista
	 * 
	 * @param p Producto que intenta eliminar
	 * @return True si ha funcionado, false si no
	 */
	public boolean remove(Producto<?> p) {
		if (contains(p)) {
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
	 * 
	 * @param p Producto que se quiere encontrar
	 * @return True si está en el array, false si no
	 */
	public boolean contains(Producto<?> producto) {
		boolean resul = false;
		Set<Integer> ids = productos.keySet();
		for (Integer id : ids) {
			if (resul) {
				break;
			} else {
				resul = producto.equals(productos.get(id));
			}
		}
		return resul;
	}

	/***
	 * Calcula que productos hay en la página pasada por parámetro y los devuelve
	 * como un array
	 * 
	 * @param n Número de página
	 * @return Array de productos en esa página
	 */
	public Producto<?>[] getPage(int n) {
		int tamPag = prodPorPag;
		int primProd = n * prodPorPag;
		int ultProd = primProd + prodPorPag;
		if (ultProd > productos.size()) {
			ultProd = productos.size();
			tamPag = ultProd - primProd;
		}
		Producto<?>[] resul = new Producto<?>[tamPag];
		ArrayList<Producto<?>> lista = deHashAArray(productos);
		List pag = lista.subList(primProd, ultProd);

		for (int i = 0; i < pag.size(); i++) {
			resul[i] = (Producto<?>) pag.get(i);
		}

		return resul;
	}

	/***
	 * Encuentra la página del producto
	 * 
	 * @param p Producto a buscar
	 * @return -1 si no está, nº de página si está
	 */
	public int findPageOf(Producto<?> p) {
		return findPageOf(p, deHashAArray(productos));
	}

	/***
	 * Encuentra la página del producto
	 * 
	 * @param p     Producto a buscar
	 * @param lista ArrayList de productos
	 * @return -1 si no está, nº de página si está
	 */
	private int findPageOf(Producto<?> p, ArrayList<Producto<?>> lista) {
		if (!lista.contains(p)) {
			return -1;
		} else {
			return (int) Math.ceil((double) (lista.indexOf(p) + 1) / prodPorPag);
		}
	}

	/***
	 * Encuentra la página del producto en una lista ordenada de mayór a menor o
	 * viceversa
	 * 
	 * @param p     Producto a buscar
	 * @param orden True = menor a mayor, False = mayor a menor
	 * @return -1 si no está, nº de página si está
	 */
	public int findPageOf(Producto<?> p, boolean orden) {
		ArrayList<Producto<?>> ordenados = ordenar(orden);
		return findPageOf(p, ordenados);
	}

	/***
	 * Ordena un array con los productos
	 * 
	 * @param menAMay True = menor a mayor, False = mayor a menor
	 * @return ArrayList con los productos ordenados
	 */
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