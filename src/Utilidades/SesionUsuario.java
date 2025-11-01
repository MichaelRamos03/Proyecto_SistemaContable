

package Utilidades;

import Modelo.Usuario;

/**
 *
 * @author Michael Ramos;
**/
public class SesionUsuario {

    // 1. Guarda la única instancia que existirá.
    // 'static' significa que esta variable pertenece a la clase, no a un objeto.
    private static SesionUsuario instancia;

    // 2. Aquí se guardará el usuario que inicie sesión.
    private Usuario usuarioLogueado;

    // 3. ¡El truco principal! El constructor es PRIVADO.
    // Esto evita que cualquiera pueda crear un nuevo objeto con "new SesionUsuario()".
    private SesionUsuario() {
    }

    // 4. La única forma de obtener acceso a la instancia.
    // Es como pedirle a la recepcionista que te atienda.
    public static SesionUsuario getInstancia() {
        // Si es la primera vez que se llama, se crea el objeto.
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        // En las llamadas siguientes, simplemente devuelve el objeto que ya se creó.
        return instancia;
    }

    // Métodos normales para guardar y obtener el usuario
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
}