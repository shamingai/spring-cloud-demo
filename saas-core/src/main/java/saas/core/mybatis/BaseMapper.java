

package saas.core.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * The interface My mapper.
 *
 * @param <T> the type parameter 
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
