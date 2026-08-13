package com.example.internshipmanagementsystem;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.internshipmanagementsystem.entity.AssessmentResult;
import com.example.internshipmanagementsystem.entity.User;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InternshipManagementSystemApplicationTests {

  @Test
  void contextLoads() {}

  @Test
  void shouldUseIntegerIdentifiers_toMatchPostgreSqlIntColumns() throws NoSuchFieldException {
    Field resultId = AssessmentResult.class.getDeclaredField("resultId");
    Field userId = User.class.getDeclaredField("userId");

    assertThat(resultId.getType()).isEqualTo(Integer.class);
    assertThat(userId.getType()).isEqualTo(Integer.class);
  }
}
