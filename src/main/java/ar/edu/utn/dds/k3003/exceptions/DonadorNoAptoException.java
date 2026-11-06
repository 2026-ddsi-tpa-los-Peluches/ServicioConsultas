package ar.edu.utn.dds.k3003.exceptions;


public class DonadorNoAptoException extends RuntimeException {
    public DonadorNoAptoException(String mensaje) {
        super(mensaje);
    }
}
