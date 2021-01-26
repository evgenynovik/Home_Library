package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.*;
import com.netcracker.education.dao.interfaces.*;
import com.netcracker.education.services.converters.BookConverter;
import com.netcracker.education.services.dto.BookDTO;
import com.netcracker.education.services.impl.BookServiceImpl;
import com.netcracker.education.services.interfaces.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BookConverter.class,
        BookServiceImpl.class})
@ActiveProfiles("test")
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;
    @MockBean
    private BookDAO bookDAO;
    @MockBean
    private GenreDAO genreDAO;
    @MockBean
    private AuthorDAO authorDAO;
    @MockBean
    private BookSeriesDAO bookSeriesDAO;
    @MockBean
    private UserCardDAO userCardDAO;
    @MockBean
    private ReviewDAO reviewDAO;
    @Value("${books.top}")
    private Integer top;
    private final BookDTO bookDTO = BookDTO.builder()
            .id(8)
            .title("The hobbit")
            .year(1954)
            .description("Adventure book")
            .authorId(9)
            .authorName("Jack London")
            .genreId(10)
            .genreTitle("scientific literature")
            .bookSeriesId(12)
            .bookSeriesTitle("The Chronicles of Amber")
            .userCardId(11).build();
    private final BookDTO anotherBookDTO = BookDTO.builder()
            .title("Dracula")
            .year(1810)
            .authorId(11)
            .authorName("Bram Stoker")
            .genreId(17)
            .genreTitle("Horror").build();

    @BeforeEach
    public void setUp() {
        Book book = new Book("The hobbit", 1954, "Adventure book",
                null, null, null, null, null);
        Author author = new Author("Jack", "London", "He was an American writer",
                "US", Collections.emptySet());
        Author secondAuthor = new Author("Robert", "Frost", "He was an American writer",
                "US", Collections.emptySet());
        Genre genre = new Genre("scientific literature",
                "is scholarly publications that report theoretical work in the natural sciences",
                Collections.emptySet());
        UserCard userCard = new UserCard("sky@gmail.com",  false,
                null, null, null);
        BookSeries bookSeries = new BookSeries("The Chronicles of Amber",
                "is a series of fantasy novels ", Collections.emptySet());
        Set<Author> authors = new HashSet<>();
        Book primaryBook = new Book("The hobbit", 1954, "Adventure book",
                null, null, null, null, null);

        book.setId(8);
        author.setId(9);
        secondAuthor.setId(7);
        genre.setId(10);
        userCard.setId(11);
        bookSeries.setId(12);
        authors.add(author);
        book.setAuthors(authors);
        book.setGenre(genre);
        book.setUserCard(userCard);
        book.setBookSeries(bookSeries);
        userCard.setBooks(Collections.singleton(book));
        primaryBook.setAuthors(authors);
        primaryBook.setGenre(genre);
        primaryBook.setUserCard(userCard);
        primaryBook.setBookSeries(bookSeries);

        when(bookDAO.findById(8)).thenReturn((Optional.of(book)));
        when(bookDAO.saveAndFlush(primaryBook)).thenReturn(book); // creating
        when(bookDAO.saveAndFlush(book)).thenReturn(book); // update
        when(bookDAO.findAll()).thenReturn(Collections.singletonList(book));
        when(bookDAO.getTopBooks(top)).thenReturn(Collections.singletonList(book));
        when(bookDAO.findByAuthorId(9)).thenReturn(Collections.singletonList(book));
        when(bookDAO.findByGenreId(10)).thenReturn(Collections.singletonList(book));
        when(bookDAO.showAllBooksFromUserCard(11)).thenReturn(Collections.singletonList(book));
        when(bookDAO.findByBookSeries(12)).thenReturn(Collections.singletonList(book));
        when(bookDAO.showAllBooksFromUser(7)).thenReturn(Collections.singletonList(book));
        when(userCardDAO.findById(11)).thenReturn(Optional.of(userCard));
        when(authorDAO.findById(9)).thenReturn(Optional.of(author));
        when(authorDAO.findById(7)).thenReturn(Optional.of(secondAuthor));
        when(genreDAO.findById(10)).thenReturn(Optional.of(genre));
        when(bookSeriesDAO.findById(12)).thenReturn(Optional.of(bookSeries));
    }

    @Test
    public void createTest() {
        BookDTO primaryBookDTO = BookDTO.builder()
                .title("The hobbit")
                .year(1954)
                .description("Adventure book")
                .authorId(9)
                .authorName("Jack London")
                .genreId(10)
                .genreTitle("scientific literature")
                .bookSeriesId(12)
                .bookSeriesTitle("The Chronicles of Amber")
                .userCardId(11).build();

        assertEquals(bookDTO, bookService.create(primaryBookDTO));
        verify(bookDAO).saveAndFlush(any(Book.class));
    }

    @Test
    public void searchTest() {
        assertEquals(bookDTO, bookService.search(8));
        assertNotEquals(anotherBookDTO, bookService.search(8));
        verify(bookDAO, times(4)).findById(8);
    }

    @Test
    public void updateTest() {
        assertEquals(bookDTO, bookService.update(bookDTO));
        assertNotEquals(anotherBookDTO, bookService.update(bookDTO));
        verify(bookDAO, times(2)).saveAndFlush(any(Book.class));
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(bookDTO), bookService.getAll());
        verify(bookDAO).findAll();
    }

    @Test
    public void deleteTest() {
        bookService.delete(8);
        verify(bookDAO).deleteById(8);
    }

    @Test
    public void getTopBooksTest() {
        assertEquals(Collections.singletonList(bookDTO), bookService.getTopBooks());
        assertNotEquals(Collections.singletonList(anotherBookDTO), bookService.getTopBooks());
        verify(bookDAO, times(2)).getTopBooks(anyInt());
    }

    @Test
    public void findByAuthorIdTest() {
        assertEquals(Collections.singletonList(bookDTO), bookService.findByAuthorId(9));
        assertNotEquals(Collections.singletonList(anotherBookDTO), bookService.findByAuthorId(9));
        verify(bookDAO, times(2)).findByAuthorId(9);
    }

    @Test
    public void findByGenreTest() {
        assertEquals(Collections.singletonList(bookDTO), bookService.findByGenre(10));
        assertNotEquals(Collections.singletonList(anotherBookDTO), bookService.findByGenre(10));
        verify(bookDAO, times(2)).findByGenreId(10);
    }

    @Test
    public void showAllBooksFromUserCardTest() {
        assertEquals(Collections.singletonList(bookDTO), bookService.showAllBooksFromUserCard(11));
        assertNotEquals(Collections.singletonList(anotherBookDTO), bookService.showAllBooksFromUserCard(11));
        verify(bookDAO, times(2)).showAllBooksFromUserCard(11);
    }

    @Test
    public void findByBookSeriesTest() {
        assertEquals(Collections.singletonList(bookDTO), bookService.findByBookSeries(12));
        assertNotEquals(Collections.singletonList(anotherBookDTO), bookService.findByBookSeries(12));
        verify(bookDAO, times(2)).findByBookSeries(12);
    }

    @Test
    public void showAllBooksFromUserTest() {
        assertEquals(Collections.singletonList(bookDTO), bookService.showAllBooksFromUser(7));
        assertNotEquals(Collections.singletonList(anotherBookDTO), bookService.showAllBooksFromUser(7));
        verify(bookDAO, times(2)).showAllBooksFromUser(7);
    }

    @Test
    public void addAuthorToBookById() {
        Set<Integer> authorsId = new HashSet<>();
        Set<String> names = new HashSet<>();

        authorsId.add(7);
        authorsId.add(9);
        bookDTO.setAuthorsId(authorsId);
        names.add("Jack London");
        names.add("Robert Frost");
        bookDTO.setAuthorsName(names);

        assertEquals(bookDTO, bookService.addAuthorToBookById(7, 8));
        assertNotEquals(anotherBookDTO, bookService.addAuthorToBookById(7, 8));
        verify(bookDAO, times(2)).findById(8);
        verify(authorDAO, times(2)).findById(7);
        verify(bookDAO, times(2)).saveAndFlush(any(Book.class));
    }

    @Test
    public void deleteAuthorFromBookByIdTest() {
        bookDTO.setAuthorsId(Collections.emptySet());
        bookDTO.setAuthorsName(Collections.emptySet());

        assertEquals(bookDTO, bookService.deleteAuthorFromBookById(9, 8));
        assertEquals(bookDTO, bookService.search(8));
        verify(bookDAO, times(3)).findById(8);
        verify(authorDAO).findById(9);
        verify(bookDAO).saveAndFlush(any(Book.class));
    }
}
