package hello.springdb2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import hello.springdb2.config.JdbcTemplateV1Config;
import hello.springdb2.repository.ItemRepository;

/**
 * 
 * https://endmemories.tistory.com/43
 * 
 * 애플리케이션 진입점(Main Class)
 *
 * @Import(JdbcTemplateV1Config.class)
 *  - Spring 컨테이너에 외부 설정 클래스를 강제로 추가 등록.
 *  - JdbcTemplateV1Config.class: JdbcTemplate 기반 @Repository 와 @Service Bean 정의.
 *
 * @SpringBootApplication(scanBasePackages = "hello.springdb2.web")
 *  - Spring Boot 애플리케이션임을 선언.
 *  - 컴포넌트 스캔 범위를 "hello.springdb2.web"로 제한.
 *    → 해당 패키지와 하위 패키지의 @Component, @Service, @Repository를 자동으로 Bean 등록.
 *    → 그 외 패키지는 자동 스캔하지 않음.
 *    → 따라서 다른 패키지는 @Import로 명시적으로 등록해야 함.
 */
@Import(JdbcTemplateV1Config.class)
@SpringBootApplication(scanBasePackages = "hello.springdb2.web")
public class Springdb2Application {

    /**
     * 애플리케이션 실행 진입점(Main Method)
     * SpringApplication.run(...) 호출로 Spring Boot 애플리케이션 실행
     */
    public static void main(String[] args) {
        SpringApplication.run(Springdb2Application.class, args);
    }

    /**
     * @Bean
     *  - 메서드 반환 객체를 Spring Bean으로 등록.
     *  
     * @Profile("local")
     *  - "local" 프로파일이 활성화된 경우에만 Bean 등록.
     *    → 운영 환경(production)에서는 초기 데이터가 자동으로 생성되지 않음.
     */
    @Bean
    @Profile("local")
    TestDataInit testDataInit(ItemRepository itemRepository) {
        return new TestDataInit(itemRepository);
    }
}
