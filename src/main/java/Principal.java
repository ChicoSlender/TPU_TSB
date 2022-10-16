import java.util.Objects;

public class Principal {
    public static void main(String[] args) {
        TSBHashTableDA<String, Persona> hashTable = new TSBHashTableDA<>();

        hashTable.put("Roberto", new Persona("Roberto", "Perez"));
        hashTable.put("Alejandro", new Persona("Alejandro", "ElCapo"));
        hashTable.put("Lionel", new Persona("Lionel", "Messi"));
        hashTable.put("R", new Persona("R", "R"));
        hashTable.put("E", new Persona("E", "E"));
        hashTable.put("G", new Persona("G", "G"));
        hashTable.put("XD", new Persona("Señor", "XD"));
        hashTable.put("Ete", new Persona("Ete", "Sech"));
        hashTable.put("Rambo", new Persona("Rambo", "Weon"));
        hashTable.put("Lorena", new Persona("Lorena", "Torres"));
        hashTable.put("Benja", new Persona("Benja", "GT"));
        hashTable.put("Juan", new Persona("Juan", "GT"));

        Persona alex = hashTable.get("Alejandro");
        Persona R = hashTable.get("R");

        System.out.println(alex);
        System.out.println(R);

        System.out.println(hashTable.toString());
    }
}

class Persona {
    private String nombre;
    private String apellido;

    public Persona(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    @Override
    public String toString() {
        return this.nombre + " " + this.apellido;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Persona)) return false;

        Persona other = (Persona) obj;

        return Objects.equals(this.nombre, other.nombre) && Objects.equals(this.apellido, other.apellido);
    }
}