package hello.springdb2.repository.jdbcTemplate;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import hello.springdb2.domain.Item;
import hello.springdb2.repository.ItemRepository;
import hello.springdb2.repository.ItemSearchCond;
import hello.springdb2.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;

/**
 * JdbcTemplateItemRepositoryV1
 * - ItemRepository 인터페이스 구현체
 * - Spring JDBC의 JdbcTemplate을 사용해서 DB CRUD 구현
 * - JdbcTemplate은 JDBC 보일러 플레이트 코드(try/catch, close, prepareStatement 등)를 줄여줌
 */

@Slf4j
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

	// JdbcTemplate 객체
	private final JdbcTemplate template;
	
	public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
	private final RowMapper<Item> itemRowMapper = (rs, rowNum) -> {
		Item item = new Item();
		item.setId(rs.getLong("id"));
		item.setItemName(rs.getString("item_name"));
		item.setPrice(rs.getInt("price"));
		item.setQuantity(rs.getInt("quantity"));
		return item;
	};
	
	@Override
	public Item save(Item item) {
		String sql = "insert into item (item_name, price, quantity) values (?, ?, ?)";
		
		// KeyHolder: DB가 생성한 자동 증가 키(id)르 저장
		KeyHolder keyHolder = new GeneratedKeyHolder();
       
		template.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
			ps.setString(1, item.getItemName());
			ps.setInt(2, item.getPrice());
			ps.setInt(3, item.getQuantity());
			return ps;
		}, keyHolder);
		
		long key = keyHolder.getKey().longValue();
		item.setId(key);
		return item;
	}

	@Override
	public Optional<Item> findById(Long id) {
		String sql = "select id, item_name, price, quantity from item where id = ?";
		try {
			Item item = template.queryForObject(sql, itemRowMapper, id);
			return Optional.of(item);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Item> findAll(ItemSearchCond cond) {
		String itemName = cond.getItemName();
		Integer maxPrice = cond.getMaxPrice();
		
		String sql = "select id, item_name, price, quantity from item";
		if(StringUtils.hasText(itemName) || maxPrice != null) {
			sql += " where";
		}
		
		boolean andFlag = false;
		List<Object> param = new ArrayList<>();
		if(StringUtils.hasText(itemName)) {
			sql += " item_name like concat('%',?,'%')";
			param.add(itemName);
			andFlag = true;
		}
		
		if(maxPrice != null) {
			if(andFlag) {
				sql += " and";
			}
			sql += " price <= ?";
			param.add(maxPrice);
		}
		log.info("sql = {}", sql);
		return template.query(sql, itemRowMapper, param.toArray());
	}

	@Override
	public void update(Long itemId, ItemUpdateDto updateParam) {
		String sql = "update item set item_name=?, price=?, quantity=? where id=?";
		template.update(sql, 
				updateParam.getItemName(),
				updateParam.getPrice(),
				updateParam.getQuantity(),
				itemId);
	}

}
