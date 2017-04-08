package feup.cm.traintickets.sqlite;

import java.util.List;

/**
 * Created by mercurius on 06/04/17.
 */

public interface IOperation<T> {
    T get(int id);
    List<T> getAll();
    void create(T t);
    int delete(int id);
}
