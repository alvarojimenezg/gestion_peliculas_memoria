package cl.usm.gestionPeliculasMemoria.repositories;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeliculasRepositoryImplTest {

    private PeliculasRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new PeliculasRepositoryImpl();
    }

    @Test
    void insertOk() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        Pelicula result = repository.insert(pelicula);

        assertEquals("PELI1", result.getId());
        assertEquals("Inception", result.getTitulo());
        assertEquals("Christopher Nolan", result.getDirector());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void insertNok_nullId() {
        Pelicula pelicula = new Pelicula(null, "Inception", "Christopher Nolan", "token123", new Comentario[0]);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.insert(pelicula);
        });

        assertEquals("El ID de la pelicula no puede ser nulo", exception.getMessage());
    }

    @Test
    void insertNok_duplicateId() {
        Pelicula pelicula1 = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        repository.insert(pelicula1);

        Pelicula pelicula2 = new Pelicula("peli1", "Interstellar", "Christopher Nolan", "token456", new Comentario[0]);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.insert(pelicula2);
        });

        assertEquals("La pelicula con ID peli1 ya existe", exception.getMessage());
    }

    @Test
    void findAllOk() {
        Pelicula pelicula1 = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        Pelicula pelicula2 = new Pelicula("PELI2", "Interstellar", "Christopher Nolan", "token456", new Comentario[0]);

        repository.insert(pelicula1);
        repository.insert(pelicula2);

        List<Pelicula> result = repository.findAll();

        assertEquals(2, result.size());
        assertEquals("PELI1", result.get(0).getId());
        assertEquals("PELI2", result.get(1).getId());

        // Verificar que la lista devuelta sea una copia independiente
        result.clear();
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void findByIdOk() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        repository.insert(pelicula);

        Pelicula result1 = repository.findById("PELI1");
        assertEquals("PELI1", result1.getId());
        assertEquals("Inception", result1.getTitulo());
        assertEquals("Christopher Nolan", result1.getDirector());

        Pelicula result2 = repository.findById("peli1");
        assertEquals("PELI1", result2.getId());
        assertEquals("Inception", result2.getTitulo());
        assertEquals("Christopher Nolan", result2.getDirector());
    }

    @Test
    void findByIdNok_nullId() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        repository.insert(pelicula);

        Pelicula result = repository.findById(null);
        assertEquals(null, result);
    }

    @Test
    void findByIdNok_notFound() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        repository.insert(pelicula);

        Pelicula result = repository.findById("PELI2");
        assertEquals(null, result);
    }
}
