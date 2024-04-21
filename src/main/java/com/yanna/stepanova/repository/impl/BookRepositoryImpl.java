package com.yanna.stepanova.repository.impl;

import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book save(Book book) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (RuntimeException ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert the book: " + book, ex);
        }
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Can't find a book by id = " + id, ex);
        }
    }

    @Override
    public Optional<List<Book>> findAllByAuthor(String author) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    entityManager.createQuery("SELECT b FROM Book b "
                            + "WHERE lower(b.author) LIKE :author", Book.class)
                            .setParameter("author", "%" + author.toLowerCase() + "%")
                            .getResultList());
        } catch (RuntimeException ex) {
            throw new RuntimeException("Can't find books by author: " + author, ex);
        }
    }

    @Override
    public Optional<List<Book>> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList());
        } catch (RuntimeException ex) {
            throw new RuntimeException("Can't find all entities of table 'books'", ex);
        }
    }
}
