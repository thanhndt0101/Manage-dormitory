package com.example.ManageDom;

import com.example.ManageDom.dao.UserResponsitory;
import com.example.ManageDom.entity.Room;
import com.example.ManageDom.entity.User;
import com.example.ManageDom.service.UserNotFoundException;
import com.example.ManageDom.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @MockBean
    private UserResponsitory repo;
    @InjectMocks
    private UserService service;
    @Test
    public void testFindByEmail(){

        Optional<User> u = service.findByEmail("admin@gmail.com");

        Assertions.assertThat(u).isNotNull();
    }
    @Test
    public void testgetId() throws UserNotFoundException {
        User u = new User();
        u.setId(9);
        Room r = new Room();
        r.setRid("null");
        u.setName("thanh");
        u.setEmail("thanh9@gmail.com");
        u.setPassword("thanh");
        u.setEnabled(true);
        u.setRoom(r);
        Optional<User> os = Optional.of(u);
        Mockito.when(repo.findById(9)).thenReturn(os);
        User user = service.get(9);
        Assertions.assertThat(user).isNotNull();
    }


}
