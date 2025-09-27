package hello.springdb2.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Data 애너테이션
 * - 클래스에 붙이면 보일러 플레이트(반복되는) 코드를 자동으로 생성해줌
 * - 주로 DTO, VO, Entity 클래스 작성 시 많이 사용됨
 * 
 * 자동 생성되는 메서드
 *  - @Getter : 모든 필드에 대한 getter 생성
 *  - @Setter : 모든 필드에 대한 setter 생성
 *  - @ToString : toString() 메서드 생성
 *  - @EqualsAndHashCode : equals(), hashCode() 메서드 생성
 *  - @RequiredArgsConstructor : final 필드나 @NonNull 필드에 대한 생성자 생성
 */
@Data
@NoArgsConstructor
public class Item {

	private Long id;
	private String itemName;
	private Integer price;
	private Integer quantity;
	
	public Item(String itemName, int price, int quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
}
