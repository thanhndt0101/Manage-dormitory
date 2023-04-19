package com.example.ManageDom;

import com.example.ManageDom.dao.RequestResponsitory;
import com.example.ManageDom.entity.Request;
import com.example.ManageDom.entity.Room;
import com.example.ManageDom.service.RequestService;
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
public class ReqeustServiceTest {
    @MockBean
    private RequestResponsitory repo;
    @InjectMocks
    private RequestService service;
    @Test
    public void testgetId() throws UserNotFoundException {

        Request r = new Request();
       r.setId(1);
       r.setType("change");
       r.setSid(1);
       r.setStatus("pending");
       r.setNewRoom("A101");
       r.setOldRoom("A102");

        Optional<Request> os = Optional.of(r);
        Mockito.when(repo.findById(1)).thenReturn(os);
        Request request = service.get(1);
        Assertions.assertThat(request).isNotNull();
    }

}
