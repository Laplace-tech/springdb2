package hello.springdb2.repository;

import lombok.Data;

@Data
public class ItemUpdateDto {

	private String itemName;
	private Integer price;
	private Integer quantity;
	
}
