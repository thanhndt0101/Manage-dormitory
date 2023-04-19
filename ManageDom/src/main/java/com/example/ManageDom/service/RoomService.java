package com.example.ManageDom.service;


import com.example.ManageDom.dao.RoomResponsitory;
import com.example.ManageDom.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    RoomResponsitory repo;
    public Page<Room> listRoom(String keyword, int pageNumber){

        Pageable pageable = PageRequest.of(pageNumber-1,5);
        if(keyword!=null){
            return repo.findAll(keyword,pageable);
        }
        return repo.findAll(pageable);
    }
    public Room get(String id) throws UserNotFoundException {
        Optional<Room> result = repo.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("could not found with id "+ id);
    }
    public void save(Room room) {
        repo.save(room);
    }


}
