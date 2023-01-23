package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 8/28/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {
    private final EntityManagerFactory emf;

    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Author getById(Long id) {
        return this.getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        TypedQuery<Author> query = getEntityManager().createQuery("SELECT a FROM Author a " +
                "WHERE a.firstName = :first_name and a.lastName = :last_name", Author.class);
        query.setParameter("first_name", firstName);
        query.setParameter("last_name", lastName);


        return query.getSingleResult();
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();
        // Full transaction in a hibernate context
        em.getTransaction().begin();
        em.persist(author);
        em.flush();
        em.getTransaction().commit();
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = this.getEntityManager();
        em.joinTransaction();
        em.merge(author);
        em.flush();
        em.clear();
        return em.find(Author.class, author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Author author = em.find(Author.class, id);
        em.remove(author);
        em.flush();
        em.getTransaction().commit();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
