package model.dao;

import java.util.List;
import java.util.Optional;


/** Interface genérica para operações de Acesso a Banco de Dados (DAO)
 * @param <T> O tipo da entidade principal que o DAO gerencia
 * @param <U> O tipo da entidade associada usada para buscas
 * @param <K> O tipo da chave primária da entidade principal
 */
public interface GenericDao<T, U, K>{

	void insert(T entidade);
	void update(T entidade);
	void deleteById(K id);
	Optional<T> findById(K id);
	List<T> findAll();
	List<T> findByEntity(U entidade);
}
