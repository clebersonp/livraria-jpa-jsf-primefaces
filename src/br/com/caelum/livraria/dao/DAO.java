package br.com.caelum.livraria.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DAO<T> {

	private final Class<T> classe;

	public DAO(Class<T> classe) {
		this.classe = classe;
	}

	public void adiciona(T t) {

		// consegue a entity manager
		EntityManager em = new JPAUtil().getEntityManager();

		// abre transacao
		em.getTransaction().begin();

		// persiste o objeto
		em.persist(t);

		// commita a transacao
		em.getTransaction().commit();

		// fecha a entity manager
		em.close();
	}

	public void remove(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();

		em.remove(em.merge(t));

		em.getTransaction().commit();
		em.close();
	}

	public void atualiza(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();

		em.merge(t);

		em.getTransaction().commit();
		em.close();
	}
	
	public int quantidadeDeElementos() {
		EntityManager em = new JPAUtil().getEntityManager();
		
		long result = (Long) em.createQuery("select count(n) from " + classe.getSimpleName() + " n").getSingleResult();
		
		em.close();
		
		return (int) result;
	}

	public List<T> listaTodos() {
		EntityManager em = new JPAUtil().getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).getResultList();

		em.close();
		return lista;
	}

	public T buscaPorId(Integer id) {
		EntityManager em = new JPAUtil().getEntityManager();
		T instancia = em.find(classe, id);
		em.close();
		return instancia;
	}

	public int contaTodos() {
		EntityManager em = new JPAUtil().getEntityManager();
		long result = (Long) em.createQuery("select count(n) from livro n")
				.getSingleResult();
		em.close();

		return (int) result;
	}

	public List<T> listaTodosPaginada(int firstResult, int maxResults, Map<String, Object> filters) {
		EntityManager em = new JPAUtil().getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		Root<T> root = query.from(classe);
		
		String titulo = (String) filters.get("titulo");
		String strPreco = (String) filters.get("preco");
		Double preco = null; 
		
		List<Predicate> predicates = new ArrayList<>();
		Predicate lessThanOrEqualTo = null;
		Predicate and = null;

		if (strPreco != null) {
			preco = Double.valueOf(strPreco);
			lessThanOrEqualTo = em.getCriteriaBuilder().lessThanOrEqualTo(root.<Double>get("preco"), preco);
			predicates.add(lessThanOrEqualTo);
		}
		
		if (titulo != null) {
			and = em.getCriteriaBuilder().like(root.<String>get("titulo"), titulo + "%");
			predicates.add(and);
		}
		
		if (predicates.size() > 0) {
			query = query.where(predicates.toArray(new Predicate[0]));
		}
		
		List<T> lista = em.createQuery(query).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();

		em.close();
		return lista;
	}

}
