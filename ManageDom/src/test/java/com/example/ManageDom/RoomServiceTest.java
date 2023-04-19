package com.example.ManageDom;

import com.example.ManageDom.dao.RoomResponsitory;
import com.example.ManageDom.entity.Room;
import com.example.ManageDom.entity.User;
import com.example.ManageDom.service.RoomService;
import com.example.ManageDom.service.UserNotFoundException;
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
public class RoomServiceTest {
    @MockBean
    private RoomResponsitory repo;
    @InjectMocks
    private RoomService service;
    @Test
    public void testgetId() throws UserNotFoundException {

        Room r = new Room();
        r.setRid("A101");
        r.setType("4");
        r.setDom("a");
        r.setGrade("4");
        r.setCurrent(4);

        Optional<Room> os = Optional.of(r);
        Mockito.when(repo.findById("A101")).thenReturn(os);
        Room room = service.get("A101");
        Assertions.assertThat(room).isNotNull();
    }
}
