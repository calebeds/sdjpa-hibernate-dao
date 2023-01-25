package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class BookDaoImpl implements BookDao {

    private final EntityManagerFactory emf;

    public BookDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Book findByISBN(String isbn) {
        EntityManager em = this.getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
            query.setParameter("isbn", isbn);

            return query.getSingleResult();
        } finally {
         em.close();
        }

    }

    @Override
    public Book getById(Long id) {
        EntityManager em = this.getEntityManager();

        try {
            Book book = em.find(Book.class, id);
            return book;
        } finally {
            em.close();
        }

    }
    @Override
    public Book findBookByTitle(String title) {
        EntityManager em = this.getEntityManager();
        try {
            TypedQuery<Book> query = em.createNamedQuery("find_book_by_title", Book.class);
            query.setParameter("title", title);
            return query.getSingleResult();

        } finally {
            em.close();
        }
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(book);
            em.flush();
            em.getTransaction().commit();
            return book;
        } finally {
            em.close();
        }
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.flush();
            em.getTransaction().commit();
            Book updated = em.find(Book.class, book.getId());
            return updated;
        } finally {
            em.close();
        }
    }
    @Override
    public void deleteBookById(Long id) {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, id);
            em.remove(book);
            em.flush();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Book> findAllBooks() {
        EntityManager em = this.getEntityManager();
        try {
            TypedQuery<Book> query = em.createNamedQuery("find_all_books", Book.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    private EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }
}
