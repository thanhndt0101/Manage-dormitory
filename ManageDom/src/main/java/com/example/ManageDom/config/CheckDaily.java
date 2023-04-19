package com.example.ManageDom.config;

import com.example.ManageDom.entity.Mail;
import com.example.ManageDom.entity.Notification;
import com.example.ManageDom.entity.Room;
import com.example.ManageDom.entity.User;
import com.example.ManageDom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class CheckDaily {
    @Autowired private CheckMail checkMail;
    @Autowired private UserService uService;
    @Autowired private NotiService nService;
    @Autowired private RoomService rservice;
    @Autowired private MailService mService;
    private static final Logger logger = LoggerFactory.getLogger(CheckDaily.class);
    //Auto check user hava paid and notify or cancel room each student
    @Scheduled(fixedRate = 216000)
    public void daily() throws UserNotFoundException {
        LocalDate fakeDate = LocalDate.parse("2022-12-24");

        List<User> listUser = uService.listUserByFakeDate(fakeDate);
        //check list user is empty
        if(!listUser.isEmpty()){
            for(int i=0;i<listUser.size();i++){
                if(listUser.get(i).getStatus().equals("chua thanh toan")){
                    Notification n = new Notification();
                    User u = new User();
                    u.setId(listUser.get(i).getId());
                    n.setUser(u);
                    n.setMessage("ban da bi huy phong");

                    nService.save(n);

                    Room roomOld= rservice.get(listUser.get(i).getRoom().getRid());
                    int current = roomOld.getCurrent();
                    current--;
                    roomOld.setCurrent(current);
                    rservice.save(roomOld);
                    Room r = new Room();
                    r.setRid("null");
                    listUser.get(i).setRoom(r);
                    listUser.get(i).setCoin(listUser.get(i).getCoin()-600);
                    listUser.get(i).setStatus("");
                    listUser.get(i).setDateBook(null);
                    uService.save(listUser.get(i));
                }

            }

        }
        logger.info("Check success");
    }
    //Auto sent email already schedule
    @Scheduled(fixedRate = 3600)
    public void checkMail() throws UserNotFoundException, ParseException {
        LocalDateTime dateNow = LocalDateTime.now();
        String DateString = dateNow.toString().replace('T',' ');
        String DateLast = DateString.substring(0,16);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateNow1 = LocalDateTime.parse(DateLast,formatter);
//        logger.info(dateNow1.toString());
        List<Mail> listMail = mService.listAllMail();
        for (int i = 0;i<listMail.size();i++){
            if(dateNow1.equals(listMail.get(i).getDateSent())){
                logger.info("Check success");
                checkMail.sendEmail("thanhkcr2002@gmail.com","thong bao",listMail.get(i).getContent());
            }
            else logger.info(listMail.get(i).getDateSent().toString());
        }

    }
}
