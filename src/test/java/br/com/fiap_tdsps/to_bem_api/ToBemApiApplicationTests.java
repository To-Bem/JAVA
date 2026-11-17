package br.com.fiap_tdsps.to_bem_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:contexto;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=false"
})
@org.springframework.test.context.TestExecutionListeners(
    listeners = org.springframework.test.context.support.DependencyInjectionTestExecutionListener.class,
    mergeMode = org.springframework.test.context.TestExecutionListeners.MergeMode.REPLACE_DEFAULTS)
class ToBemApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
