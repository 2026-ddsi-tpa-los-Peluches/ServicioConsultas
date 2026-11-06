package ar.edu.utn.dds.k3003.controllers.donaciones;

import ar.edu.utn.dds.k3003.dtos.donaciones.*;
import ar.edu.utn.dds.k3003.exceptions.ProductoNoEncontradoException;
import ar.edu.utn.dds.k3003.service.ServiceProducto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ServiceProducto servicioProducto;

    public ProductoController(ServiceProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerProductos() {
        List<ProductoDTO> productos = this.servicioProducto.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarProducto(@PathVariable String id) {
        try {
            ProductoDTO producto = this.servicioProducto.buscarPorId(id);
            return ResponseEntity.ok(producto);
        } catch (ProductoNoEncontradoException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la solicitud del producto.");
        }
    }
}