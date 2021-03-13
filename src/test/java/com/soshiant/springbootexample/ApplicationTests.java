package com.soshiant.springbootexample;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mockStatic;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith({MockitoExtension.class})
class ApplicationTests {

	@Test
	@DisplayName("Test main method of Application Class")
	void testMainMethodApplicationClass() {
		try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
			mocked.when(() -> { SpringApplication.run(Application.class, new String[] {}); })
					.thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

			Application.main(new String[] {});
			mocked.verify(() -> { SpringApplication.run(Application.class, new String[] {}); });

		}
	}

}
