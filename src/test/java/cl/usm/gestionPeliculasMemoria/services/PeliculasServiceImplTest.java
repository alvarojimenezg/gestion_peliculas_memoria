package cl.usm.gestionPeliculasMemoria.services;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.repositories.PeliculasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeliculasServiceImplTest {

    @Mock
    private PeliculasRepository peliculasRepository;

    private PeliculasServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PeliculasServiceImpl(peliculasRepository);
    }

    @Test
    void createPeliculaOk() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", null, new Comentario[0]);
        
        when(peliculasRepository.insert(pelicula)).thenReturn(pelicula);

        Pelicula result = service.createPelicula(pelicula);

        assertEquals("PELI1", result.getId());
        assertEquals("Inception", result.getTitulo());
        assertEquals("Christopher Nolan", result.getDirector());
        assertEquals(10, result.getTokenDescarga().length());
    }

    @Test
    void createPeliculaNok_repositoryException() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", null, new Comentario[0]);
        
        when(peliculasRepository.insert(pelicula)).thenThrow(new IllegalArgumentException("ID duplicado"));

        Pelicula result = service.createPelicula(pelicula);

        assertEquals(null, result);
    }

    @Test
    void getAllOk() {
        List<Pelicula> mockList = new ArrayList<>();
        mockList.add(new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]));
        mockList.add(new Pelicula("PELI2", "Interstellar", "Christopher Nolan", "token456", new Comentario[0]));

        when(peliculasRepository.findAll()).thenReturn(mockList);

        List<Pelicula> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("PELI1", result.get(0).getId());
        assertEquals("PELI2", result.get(1).getId());
    }

    @Test
    void findByIdOk() {
        Pelicula pelicula = new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]);
        
        when(peliculasRepository.findById("PELI1")).thenReturn(pelicula);

        Pelicula result = service.findById("PELI1");

        assertEquals("PELI1", result.getId());
        assertEquals("Inception", result.getTitulo());
        assertEquals("Christopher Nolan", result.getDirector());
    }

    @Test
    void findByIdNok_notFound() {
        when(peliculasRepository.findById("PELI2")).thenReturn(null);

        Pelicula result = service.findById("PELI2");

        assertEquals(null, result);
    }

    @Test
    void filterOk() {
        List<Pelicula> mockList = new ArrayList<>();
        mockList.add(new Pelicula("PELI1", "Inception", "Christopher Nolan", "token123", new Comentario[0]));
        mockList.add(new Pelicula("PELI2", "Interstellar", "Christopher Nolan", "token456", new Comentario[0]));
        mockList.add(new Pelicula("OTRA3", "The Dark Knight", "Christopher Nolan", "token789", new Comentario[0]));

        when(peliculasRepository.findAll()).thenReturn(mockList);

        // Búsqueda por ID insensible a mayúsculas/minúsculas
        List<Pelicula> result1 = service.filter("peli");
        assertEquals(2, result1.size());
        assertEquals("PELI1", result1.get(0).getId());
        assertEquals("PELI2", result1.get(1).getId());

        // Búsqueda por título insensible a mayúsculas/minúsculas
        List<Pelicula> result2 = service.filter("knight");
        assertEquals(1, result2.size());
        assertEquals("OTRA3", result2.get(0).getId());

        // Búsqueda que no retorna resultados
        List<Pelicula> result3 = service.filter("Avatar");
        assertEquals(0, result3.size());
    }
}
