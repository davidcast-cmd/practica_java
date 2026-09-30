public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private boolean estado;

    // constructor
    public Usuario(int id, String nombre, String email, boolean estado) {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.estado = estado;
    }

    // setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivo() {
        return estado;
    }
}
