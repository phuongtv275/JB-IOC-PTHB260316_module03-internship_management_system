package com.example.internshipmanagementsystem;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.internshipmanagementsystem.entity.AssessmentResult;
import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import com.example.internshipmanagementsystem.entity.User;
import java.lang.reflect.Field;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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

  @Test
  void shouldMapApplicationEnums_asPostgreSqlNamedEnums() throws NoSuchFieldException {
    Field role = User.class.getDeclaredField("role");
    Field status = InternshipAssignment.class.getDeclaredField("status");

    assertThat(role.getAnnotation(JdbcTypeCode.class).value()).isEqualTo(SqlTypes.NAMED_ENUM);
    assertThat(status.getAnnotation(JdbcTypeCode.class).value()).isEqualTo(SqlTypes.NAMED_ENUM);
  }
}
