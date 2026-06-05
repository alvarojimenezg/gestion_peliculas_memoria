package cl.usm.gestionPeliculasMemoria.controllers;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.services.PeliculasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeliculasControllerTest {

    @Mock
    private PeliculasService peliculasService;

    private PeliculasController controller;

    @BeforeEach
    void setUp() {
        controller = new PeliculasController(peliculasService);
    }

    @Test
    void getAllWithoutQueryOk() {
        List<Pelicula> mockList = new ArrayList<>();
        mockList.add(new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]));
        
        when(peliculasService.getAll()).thenReturn(mockList);

        ResponseEntity<List<Pelicula>> response = controller.getAll(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockList, response.getBody());
    }

    @Test
    void getAllWithQueryOk() {
        List<Pelicula> mockList = new ArrayList<>();
        mockList.add(new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]));
        
        when(peliculasService.filter("Inception")).thenReturn(mockList);

        ResponseEntity<List<Pelicula>> response = controller.getAll("Inception");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockList, response.getBody());
    }

    @Test
    void getAllEmptyQueryOk() {
        List<Pelicula> mockList = new ArrayList<>();
        mockList.add(new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]));
        
        when(peliculasService.getAll()).thenReturn(mockList);

        ResponseEntity<List<Pelicula>> response = controller.getAll("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockList, response.getBody());
    }

    @Test
    void getAllNok() {
        when(peliculasService.getAll()).thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<List<Pelicula>> response = controller.getAll(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void createPeliculaOk() {
        Pelicula input = new Pelicula("PELI1", "Inception", "Christopher Nolan", null, new Comentario[0]);
        Pelicula output = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        
        when(peliculasService.createPelicula(input)).thenReturn(output);

        ResponseEntity<?> response = controller.createPelicula(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(output, response.getBody());
    }

    @Test
    void createPeliculaNok() {
        Pelicula input = new Pelicula("PELI1", "Inception", "Christopher Nolan", null, new Comentario[0]);
        
        when(peliculasService.createPelicula(input)).thenReturn(null);

        ResponseEntity<?> response = controller.createPelicula(input);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void findByIdOk() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        
        when(peliculasService.findById("PELI1")).thenReturn(pelicula);

        ResponseEntity<Pelicula> response = controller.findById("PELI1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pelicula, response.getBody());
    }

    @Test
    void findByIdNotFoundNok() {
        when(peliculasService.findById("PELI2")).thenReturn(null);

        ResponseEntity<Pelicula> response = controller.findById("PELI2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void findByIdExceptionNok() {
        when(peliculasService.findById("PELI1")).thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<Pelicula> response = controller.findById("PELI1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void getComentariosOk() {
        Comentario[] comentarios = new Comentario[] {
            new Comentario("usuario1", "Buenisima")
        };
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", comentarios);
        
        when(peliculasService.findById("PELI1")).thenReturn(pelicula);

        ResponseEntity<?> response = controller.getComentarios("PELI1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comentarios, response.getBody());
    }

    @Test
    void getComentariosNotFoundNok() {
        when(peliculasService.findById("PELI2")).thenReturn(null);

        ResponseEntity<?> response = controller.getComentarios("PELI2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void getComentariosExceptionNok() {
        when(peliculasService.findById("PELI1")).thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<?> response = controller.getComentarios("PELI1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
