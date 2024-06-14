package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.book.BookDto;
import com.yanna.stepanova.dto.book.BookDtoWithoutCategoryIds;
import com.yanna.stepanova.dto.book.CreateBookRequestDto;
import com.yanna.stepanova.mapper.BookMapper;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.model.Category;
import com.yanna.stepanova.repository.book.BookRepository;
import com.yanna.stepanova.service.impl.BookServiceImpl;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepo;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
            Get correct bookDto for valid requestDto""")
    public void save_WithValidRequestDto_ReturnBookDto() {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto("Title 1", "Writer 1",
                "000-11-00000000", BigDecimal.valueOf(0.99), Set.of(5L, 8L, 9L),
                "Description", "Cover image");

        Book book = new Book();
        book.setTitle(requestDto.title());
        book.setAuthor(requestDto.author());
        book.setIsbn(requestDto.isbn());
        book.setPrice(requestDto.price());
        book.setDescription(requestDto.description());
        book.setCoverImage(requestDto.coverImage());
        book.setCategorySet(requestDto.categoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet()));

        BookDto expected = new BookDto();
        expected.setId(2L);
        expected.setTitle(book.getTitle());
        expected.setAuthor(book.getAuthor());
        expected.setPrice(book.getPrice());
        expected.setIsbn(book.getIsbn());
        expected.setDescription(book.getDescription());
        expected.setCoverImage(book.getCoverImage());
        expected.setCategoryIds(book.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepo.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);
        //when
        BookDto actual = bookService.save(requestDto);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get correct bookDto for existing book""")
    public void getBookById_WithValidId_ReturnBookDto() {
        //given
        Long bookId = 1L;
        Book book = new Book(bookId);
        book.setTitle("Some book");
        book.setAuthor("Some author");
        book.setPrice(BigDecimal.valueOf(1.99));
        book.setIsbn("111-00-00000000");
        book.setDescription("Some description");
        book.setCoverImage("Some image");
        book.setCategorySet(Set.of(new Category(1L), new Category(4L)));
        Mockito.when(bookRepo.findById(bookId)).thenReturn(Optional.of(book));

        BookDto expected = new BookDto();
        expected.setId(book.getId());
        expected.setTitle(book.getTitle());
        expected.setAuthor(book.getAuthor());
        expected.setPrice(book.getPrice());
        expected.setIsbn(book.getIsbn());
        expected.setDescription(book.getDescription());
        expected.setCoverImage(book.getCoverImage());
        expected.setCategoryIds(book.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);
        //when
        BookDto actual = bookService.getBookById(bookId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get an exception when trying to get a book by invalid id""")
    public void getBookById_WithInvalidId_ThrowException() {
        //given
        Long bookId = -1L;
        Mockito.when(bookRepo.findById(bookId)).thenReturn(Optional.empty());
        //when
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(bookId));
        //then
        String expected = String.format("Book with id: %s not found", bookId);
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get a list of bookDto by author""")
    public void getAllByAuthor_WithValidAuthor_ReturnListBookDto() {
        //given
        String author = "Writer_1";
        Book book = new Book(1L);
        book.setTitle("Some book");
        book.setAuthor(author);
        book.setPrice(BigDecimal.valueOf(1.99));
        book.setIsbn("111-00-00000000");
        book.setDescription("Some description");
        book.setCoverImage("Some image");
        book.setCategorySet(Set.of(new Category(1L)));
        List<Book> bookList = List.of(book);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setPrice(book.getPrice());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryIds(book.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        Mockito.when(bookRepo.findAllByAuthorContainsIgnoreCase(author)).thenReturn(bookList);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        //when
        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookService.getAllByAuthor(author);
        //then
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    @DisplayName("""
            Get a list of all bookDto""")
    public void getAll_WithValidPageable_ReturnAllBookDto() {
        //given
        Book book1 = new Book(1L);
        book1.setTitle("One book");
        book1.setAuthor("Writer A");
        book1.setPrice(BigDecimal.valueOf(1.99));
        book1.setIsbn("111-00-00000000");
        book1.setDescription("Some description");
        book1.setCoverImage("Some image");
        book1.setCategorySet(Set.of(new Category(1L)));

        Book book2 = new Book(2L);
        book2.setTitle("Two book");
        book2.setAuthor("Writer B");
        book2.setPrice(BigDecimal.valueOf(3.00));
        book2.setIsbn("111-11-00000000");
        book2.setCategorySet(Set.of());

        List<Book> bookList = List.of(book1, book2);

        BookDto bookDto1 = new BookDto();
        bookDto1.setId(book1.getId());
        bookDto1.setTitle(book1.getTitle());
        bookDto1.setAuthor(book1.getAuthor());
        bookDto1.setPrice(book1.getPrice());
        bookDto1.setIsbn(book1.getIsbn());
        bookDto1.setDescription(book1.getDescription());
        bookDto1.setCoverImage(book1.getCoverImage());
        bookDto1.setCategoryIds(book1.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        BookDto bookDto2 = new BookDto();
        bookDto2.setId(book2.getId());
        bookDto2.setTitle(book2.getTitle());
        bookDto2.setAuthor(book2.getAuthor());
        bookDto2.setPrice(book2.getPrice());
        bookDto2.setIsbn(book2.getIsbn());
        bookDto2.setDescription(book2.getDescription());
        bookDto2.setCoverImage(book2.getCoverImage());
        bookDto2.setCategoryIds(book2.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
        Mockito.when(bookRepo.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        Mockito.when(bookMapper.toDto(book2)).thenReturn(bookDto2);
        //when
        List<BookDto> expected = List.of(bookDto1, bookDto2);
        List<BookDto> actual = bookService.getAll(pageable);
        //then
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    @DisplayName("""
            Get updated bookDto by valid id""")
    public void updateBook_WithValidIdAndRequestDto_ReturnBookDto() {
        //given
        Long bookId = 7L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto("Update title", "Update author",
                "000-00-12345678", BigDecimal.valueOf(1.23), Set.of(1L, 2L),
                "Update description", "Update cover image");

        Book oldBook = new Book(bookId);
        oldBook.setTitle("Old title");
        oldBook.setAuthor("Old author");
        oldBook.setPrice(BigDecimal.valueOf(1.99));
        oldBook.setIsbn("000-00-00000000");
        oldBook.setDescription("Old description");
        oldBook.setCoverImage("Old image");
        oldBook.setCategorySet(null);

        Book updatedBook = new Book(oldBook.getId());
        updatedBook.setTitle(requestDto.title());
        updatedBook.setAuthor(requestDto.author());
        updatedBook.setIsbn(requestDto.isbn());
        updatedBook.setPrice(requestDto.price());
        updatedBook.setDescription(requestDto.description());
        updatedBook.setCoverImage(requestDto.coverImage());
        updatedBook.setCategorySet(requestDto.categoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet()));

        BookDto expected = new BookDto();
        expected.setId(updatedBook.getId());
        expected.setTitle(updatedBook.getTitle());
        expected.setAuthor(updatedBook.getAuthor());
        expected.setPrice(updatedBook.getPrice());
        expected.setIsbn(updatedBook.getIsbn());
        expected.setDescription(updatedBook.getDescription());
        expected.setCoverImage(updatedBook.getCoverImage());
        expected.setCategoryIds(updatedBook.getCategorySet().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        Mockito.when(bookRepo.findById(bookId)).thenReturn(Optional.of(oldBook));
        Mockito.when(bookMapper.updateBookFromDto(oldBook, requestDto)).thenReturn(updatedBook);
        Mockito.when(bookMapper.toDto(updatedBook)).thenReturn(expected);
        //when
        BookDto actual = bookService.updateBook(bookId, requestDto);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get an exception when trying to update a book by invalid id""")
    public void updateBook_WithInvalidId_ThrowException() {
        //given
        Long bookId = 10L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto("Update title", "Update author",
                "000-00-12345678", BigDecimal.valueOf(1.23), Set.of(1L, 2L),
                "Update description", "Update cover image");
        Mockito.when(bookRepo.findById(bookId)).thenReturn(Optional.empty());
        //when
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(bookId, requestDto));
        //then
        String expected = "Can't get book by id = " + bookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get all books without category id by valid category id""")
    public void getAllByCategoryId_WithValidCategotyId_ReturnAllBookDtoWithoutCategoryIds() {
        //given
        Long categoryId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        Book book1 = new Book(1L);
        book1.setTitle("First title");
        book1.setAuthor("Writer A");
        book1.setPrice(BigDecimal.valueOf(1.99));
        book1.setIsbn("111-00-00000000");
        book1.setDescription("Some description");
        book1.setCoverImage("Some image");
        book1.setCategorySet(Set.of(new Category(1L)));

        Book book2 = new Book(2L);
        book2.setTitle("Second title");
        book2.setAuthor("Writer B");
        book2.setPrice(BigDecimal.valueOf(3.00));
        book2.setIsbn("111-11-00000000");
        book2.setCategorySet(Set.of(new Category(1L), new Category(2L)));

        BookDtoWithoutCategoryIds bookDtoWithout1 = new BookDtoWithoutCategoryIds(book1.getId(),
                 book1.getTitle(), book1.getAuthor(), book1.getPrice(), book1.getIsbn(),
                 book1.getDescription(), book1.getCoverImage());
        BookDtoWithoutCategoryIds bookDtoWithout2 = new BookDtoWithoutCategoryIds(book2.getId(),
                book2.getTitle(), book2.getAuthor(), book2.getPrice(), book2.getIsbn(),
                book2.getDescription(), book2.getCoverImage());

        List<Book> bookList = List.of(book1, book2);

        Mockito.when(bookRepo.findAllByCategorySet_Id(categoryId, pageable)).thenReturn(bookList);
        Mockito.when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookDtoWithout1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookDtoWithout2);
        //when
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithout1, bookDtoWithout2);
        List<BookDtoWithoutCategoryIds> actual = bookService.getAllByCategoryId(categoryId, pageable);
        //then
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}

