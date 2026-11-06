package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import ar.edu.utn.dds.k3003.exceptions.donaciones.ProductoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private Fachada fachada;

    public ProductoController(Fachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> agregarProducto(@RequestBody ProductoDTO productoDTO) {
        try {
         ProductoDTO productoRegistrado = this.fachada.agregarProducto(productoDTO);
         return ResponseEntity.status(HttpStatus.CREATED).body(productoRegistrado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerProductos() {
        List<ProductoDTO> productos = this.fachada.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarProducto(@PathVariable String id) {
        try {
            ProductoDTO producto = this.fachada.buscarProductoPorID(id);
            return ResponseEntity.ok(producto);
        } catch (ProductoNoEncontradoException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la solicitud del producto.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> modificarProducto(@PathVariable String id, @RequestBody ProductoDTO productoDTO) {
        try {
            ProductoDTO productoActualizado = this.fachada.actualizarProducto(id, productoDTO);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        try {
            this.fachada.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (ProductoNoEncontradoException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
