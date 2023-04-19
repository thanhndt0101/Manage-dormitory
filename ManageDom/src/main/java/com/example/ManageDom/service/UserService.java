package com.example.ManageDom.service;


import com.example.ManageDom.dao.UserResponsitory;
import com.example.ManageDom.entity.Request;
import com.example.ManageDom.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserResponsitory repo;

    public Optional<User> findByEmail(String email){
        return  repo.findByEmail(email);
    }

    public User get(Integer id) throws UserNotFoundException {
        Optional<User> result = repo.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("could not found with id "+ id);
    }
    public void save(User user) {
        repo.save(user);
    }
    public List<User> listUserByRid(String rid){
        return(List<User>)repo.findByrid(rid);
    }
    public Page<User> listAllUser(String keyword, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber-1,5);
        if(keyword!=null){
            return repo.findAll(keyword,pageable);
        }
        else return repo.findAll(pageable);

    }
    public List<User> listUserByFakeDate(LocalDate fakeDate){
        return (List<User>) repo.findByFakeDate(fakeDate);
    }
    public List<User> ListAllUserNotByPage(){
        return (List<User>) repo.findAll();
    }

}
