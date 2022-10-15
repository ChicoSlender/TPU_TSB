import java.io.Serializable;
import java.util.*;

/**
 * Clase que emula la funcionalidad de la clase nativa java.util.Hashtable provista por Java.
 * TSBHashTableDA utiliza un arreglo de objetos de una clase interna Entry que implementa la interfaz interna homologa de Map
 *
 * La clase no admite repeticion de claves (al insertar una clave repetida se sobreescribe el valor insertado previamente emparejado con la misma clave),
 * ni la inserción de pares con claves o valores nulos.
 *
 * A diferencia de la clase provista por la cátedra esta implementa un método de direccionamiento abierto para la resolución de colisiones
 *
 * @author Grupo 20
 * @version Octubre de 2022
 * @param <K> La clase de los objetos que serán usados como clave en la tabla.
 * @param <V> La clase de los objetos que serán los valores almacenados en la tabla. */

public class TSBHashTableDA<K, V> implements Map<K, V>, Cloneable, Serializable {
    //************************ Constantes (privadas o públicas).

    // El tamaño máximo que podrá tener el arreglo interno de soporte
    private final static int MAX_SIZE = Integer.MAX_VALUE;


    //************************ Atributos privados (estructurales).

    // la tabla hash: el arreglo que contiene los pares clave-valor (objetos de la clase Entry)
    private Map.Entry<K, V>[] table;

    // el tamaño inicial de la tabla (tamaño con el que fue creada)
    private int initial_capacity;

    // la cantidad de objetos que contiene la tabla
    private int count;

    // el factor de carga utilizado para determinar si hace falta un rehashing de la tabla
    private float load_factor;

    //************************ Atributos privados (para gestionar las vistas).

    /*
     * Cada uno de estos campos se inicializa para contener una instancia de la
     * vista que sea más apropiada, la primera vez que esa vista es requerida.
     * La vista son objetos stateless (no se requiere que almacenen datos, sino
     * que sólo soportan operaciones), y por lo tanto no es necesario crear más
     * de una de cada una.
     */
    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;


    //************************ Atributos protegidos (control de iteración).

    // conteo de operaciones de cambio de tamaño (utilizado para fail-fast iterator).
    protected transient int modCount;

    //************************ Constructores.

    /**
     * Crea una tabla vacía, con la capacidad inicial igual a 11 y con factor
     * de carga igual a 0.5f.
     */
    public TSBHashTableDA()
    {
        this(11, 0.8f);
    }

    /**
     * Crea una tabla vacía, con la capacidad inicial indicada y con factor
     * de carga igual a 0.5f.
     * @param initial_capacity la capacidad inicial de la tabla.
     */
    public TSBHashTableDA(int initial_capacity)
    {
        this(initial_capacity, 0.8f);
    }

    /**
     * Crea una tabla vacía, con la capacidad inicial indicada y con el factor
     * de carga indicado. Si la capacidad inicial indicada por initial_capacity
     * es menor o igual a 0, la tabla será creada de tamaño 11. Si el factor de
     * carga indicado es negativo o cero, se ajustará a 0.5f.
     * @param initial_capacity la capacidad inicial de la tabla.
     * @param load_factor el factor de carga de la tabla.
     */
    public TSBHashTableDA(int initial_capacity, float load_factor)
    {
        if(load_factor <= 0) { load_factor = 0.8f; }
        if(initial_capacity <= 0) { initial_capacity = 11; }
        else
        {
            if(initial_capacity > TSBHashTableDA.MAX_SIZE)
            {
                initial_capacity = TSBHashTableDA.MAX_SIZE;
            }
        }

        this.table = new Entry[initial_capacity] ;
        Arrays.fill(table, null);

        this.initial_capacity = initial_capacity;
        this.load_factor = load_factor;
        this.count = 0;
        this.modCount = 0;
    }

    /**
     * Crea una tabla a partir del contenido del Map especificado.
     * @param t el Map a partir del cual se creará la tabla.
     */
    public TSBHashTableDA(Map<? extends K,? extends V> t)
    {
        this(11, 0.8f);
        this.putAll(t);
    }

    //************************ Implementación de métodos especificados por la interfaz Map.

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }

    //************************ Clases internas de soporte

    /**
     * Clase interna que representa los pares de objetos clave-valor a almacenar dentro de la tabla hash.
     * Instancias de esta clase son las almacenadas dentro del arreglo de soporte
     */
    private class Entry<K, V> implements Map.Entry<K, V> {

        private K key;
        private V value;

        /**
         * Constructor de la clase
         * @throws IllegalArgumentException si se intenta inicializar con clave y/o valor nulos*/
        public Entry(K key, V value)
        {
            if(key == null || value == null)
            {
                throw new IllegalArgumentException("Entry(): no se acepta null como parámetros");
            }
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        /**
         * @throws IllegalArgumentException si se intenta setear un valor null*/
        @Override
        public V setValue(V value) {
            if (value == null) {
                throw new IllegalArgumentException("Entry.setValue(): no se acepta null como parámetro");
            }

            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            final Entry other = (Entry) obj;
            return key.equals(other.key) && value.equals(other.value);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.key);
            hash = 61 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public String toString() {
            return "(" + key.toString() + ", " + value.toString() + ")";
        }
    }
}
