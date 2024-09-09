package com.moabits.cicd;

import com.moabits.cicd.model.entity.User;
import com.moabits.cicd.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User(null, "John Doe", "john@example.com", null);
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
    }
}
