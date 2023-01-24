package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class BookDaoImpl implements BookDao {

    private final EntityManagerFactory emf;

    public BookDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Book getById(Long id) {
        EntityManager em = this.getEntityManager();
        Book book = em.find(Book.class, id);

        em.close();

        return book;
    }
    @Override
    public Book findBookByTitle(String title) {
        EntityManager em = this.getEntityManager();

        TypedQuery<Book> query = getEntityManager().createQuery("SELECT b FROM Book b " +
                "WHERE b.title = :title", Book.class);

        query.setParameter("title", title);

        em.close();

        return query.getSingleResult();
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.persist(book);
        em.flush();
        em.getTransaction().commit();

        em.close();

        return book;
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = this.getEntityManager();
        em.getTransaction().begin();
        em.merge(book);
        em.flush();
        em.getTransaction().commit();

        Book updated = em.find(Book.class, book.getId());

        em.close();

        return updated;
    }
    @Override
    public void deleteBookById(Long id) {
        EntityManager em = this.getEntityManager();
        em.getTransaction().begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        em.flush();
        em.getTransaction().commit();

        em.close();
    }

    private EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }
}
