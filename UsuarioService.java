import java.util.LinkedHashMap;
import java.util.Map;

// Guarda los usuarios en un Map con el id como clave
// Garantiza que no haya ids duplicados porque no admite repes
public class UsuarioService {
    private Map<Integer, Usuario> usuarios = new LinkedHashMap<>();

    // Da de alta un usuario; lanza excepción si los datos no son válidos o si su id ya existe
    public void alta(Usuario usuario) throws IdDuplicadoException, DatoInvalidoException {
        validar(usuario);
        if (usuarios.containsKey(usuario.getId())) {
            throw new IdDuplicadoException("Ya existe un usuario con id " + usuario.getId());
        }
        usuarios.put(usuario.getId(), usuario);
    }

    // Comprueba que los datos del usuario tengan sentido
    private void validar(Usuario u) throws DatoInvalidoException {
        if (u.getId() <= 0) {
            throw new DatoInvalidoException("El id debe ser mayor que 0");
        }
        if (u.getNombre() == null || u.getNombre().isBlank()) {
            throw new DatoInvalidoException("El nombre no puede estar vacío");
        }
        if (u.getEmail() == null || !u.getEmail().contains("@") || u.getEmail().contains(" ")) {
            throw new DatoInvalidoException("El email no es válido (debe contener @ y no tener espacios)");
        }
    }

    // Pone inactivo al usuario, lanza excepcion si no existe
    public void baja(int id) throws UsuarioNoEncontradoException {
        buscarPorId(id).setEstado(false);
    }

    // Devuelve el usuario con ese id, get devuelve null si no existe
    public Usuario buscarPorId(int id) throws UsuarioNoEncontradoException {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No existe ningún usuario con id " + id);
        }
        return usuario;
    }

    // Muestra solo los usuarios activos
    public void listarActivos() {
        boolean hayActivos = false;
        for (Usuario u : usuarios.values()) {
            if (u.isActivo()) {
                System.out.println(u.getId() + " | " + u.getNombre() + " | " + u.getEmail());
                hayActivos = true;
            }
        }
        if (!hayActivos) {
            System.out.println("No hay usuarios activos");
        }
    }
}
