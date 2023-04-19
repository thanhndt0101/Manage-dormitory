package com.example.ManageDom.service;


import com.example.ManageDom.dao.RequestResponsitory;
import com.example.ManageDom.entity.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    RequestResponsitory repo;
    public void save(Request request){
        repo.save(request);
    }
    public List<Request> listRequestById(Integer sid){
        return (List<Request>)repo.findBysid(sid);
    }
    public Page<Request> listRequestById(Integer sid,String keyword,int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber-1,5);
        if(keyword!=null){
            return repo.findBysid(sid,keyword,pageable);
        }
    else return repo.findBysid(sid,pageable);
}

//    public List<Request> listAllRequest(){
//        return(List<Request>)repo.findAll();
//    }
    public Page<Request> listAllRequest(String keyword, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber-1,5);
        if(keyword!=null){
            return repo.findAll(keyword,pageable);
        }
        else return repo.findAll(pageable);

}
    public Request get(Integer id) throws UserNotFoundException {
        Optional<Request> result = repo.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("could not found with id "+ id);
    }
}

