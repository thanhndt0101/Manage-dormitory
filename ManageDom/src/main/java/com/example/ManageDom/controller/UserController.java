package com.example.ManageDom.controller;


import com.example.ManageDom.config.CheckMail;
import com.example.ManageDom.config.UserExcelExporter;

import com.example.ManageDom.entity.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.ManageDom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller

public class UserController {
    @Autowired
    private UserService uService;
    @Autowired
    private RoomService rservice;
    @Autowired
    private RequestService reqService;
    @Autowired
    private NotiService nService;
    @Autowired
    private CheckMail checkMail;
    @Autowired
    private MailService mService;

    //Function Log out for user
    @GetMapping("logout")
    public String logOut(HttpSession session) {

        session.removeAttribute("userss");

        return "login";
    }
    //Function return list booking list for user and admin
    //can se all member each room
    @GetMapping("/book/{pageNumber}")
    public String showBook(Model model, HttpSession session, @PathVariable("pageNumber") int currentPage, @Param("keyword") String keyword) {
        Page<Room> page = rservice.listRoom(keyword, currentPage);
        List<Room> listRoom = page.getContent();
        long totalItem = page.getTotalElements();
        int totalPage = page.getTotalPages();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String autho = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        Optional<User> Ouser = uService.findByEmail(email);
        User user = Ouser.get();
        session.setAttribute("userss", user);
        String username = user.getName();
        Integer userid = user.getId();
        List<Request> listReq = reqService.listRequestById(userid);
        //check list is empty
        if (listReq.isEmpty()) {
            model.addAttribute("check", true);
        } else {
            int lastindex = listReq.size() - 1;
            if (listReq.get(lastindex).getStatus().equals("pending")) {
                model.addAttribute("check", false);
            } else {
                model.addAttribute("check", true);
            }
        }
        List<Notification> listNotiById = nService.listNotiBySid(userid);
        model.addAttribute("listNoti", listNotiById);
        model.addAttribute("username", username);
        model.addAttribute("userid", userid);
        model.addAttribute("key", keyword);
        model.addAttribute("cc", autho);
        model.addAttribute("listRoom", listRoom);
        model.addAttribute("totalItem", totalItem);
        model.addAttribute("totalPage", totalPage);
        return "booking";
    }
    //Function for student to change status booking
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable("id") String id, HttpSession session) {
        User user = (User) session.getAttribute("userss");
        Request req = new Request();
        //Check student have room or not
        if (!user.getRoom().getRid().equals("null")) {
            req.setSid(user.getId());
            req.setType("change");
            req.setOldRoom(user.getRoom().getRid());
            req.setNewRoom(id);
            req.setStatus("pending");

            reqService.save(req);


        } else {
            req.setSid(user.getId());
            req.setType("new");
            req.setOldRoom("null");
            req.setNewRoom(id);
            req.setStatus("pending");


            reqService.save(req);

        }


        return "redirect:/request/1";


    }
    //Function for student to see current room
    @GetMapping("/current")
    public String showCurrent(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userss");
        String name = user.getName();
        try {
            Room room = rservice.get(user.getRoom().getRid());
            model.addAttribute("Room", room);
            model.addAttribute("username", name);
        } catch (UserNotFoundException e) {
            return "booking";
        }
        return "current";
    }
    //Function for student to checkout from dormitory
    @GetMapping("/checkout")
    public String showCheckOut(HttpSession session) {
        User user = (User) session.getAttribute("userss");
        Request req = new Request();
        req.setSid(user.getId());
        req.setType("checkout");
        req.setOldRoom(user.getRoom().getRid());
        req.setNewRoom("null");
        req.setStatus("pending");
        reqService.save(req);
        return "redirect:/request/1";
    }
    //Function for user can see all their request
    //Function fot admin can see all request of student to handle
    @GetMapping("/request/{pageNumber}")
    public String showRequest(HttpSession session, Model model, @PathVariable("pageNumber") int currentPage, @Param("keyword") String keyword) {

        User user = (User) session.getAttribute("userss");
        Integer userid = user.getId();
        String username = user.getName();
        //check roll user or admin
        if (user.getId() == 5) {
            Page<Request> page = reqService.listAllRequest(keyword, currentPage);
            List<Request> listReq = page.getContent();
            long totalItem = page.getTotalElements();
            int totalPage = page.getTotalPages();
            //check list request is empty
            if (!listReq.isEmpty()) {
                model.addAttribute("listReq", listReq);
                model.addAttribute("userid", userid);
                model.addAttribute("totalItem", totalItem);
                model.addAttribute("totalPage", totalPage);
                model.addAttribute("key", keyword);
                model.addAttribute("username", username);
            }
        } else {
            Page<Request> page = reqService.listRequestById(user.getId(), keyword, currentPage);
            List<Request> listReq = page.getContent();
            long totalItem = page.getTotalElements();
            int totalPage = page.getTotalPages();

            if (!listReq.isEmpty()) {
                //check list request is empty
                model.addAttribute("listReq", listReq);
                model.addAttribute("c", userid);
                model.addAttribute("totalItem", totalItem);
                model.addAttribute("totalPage", totalPage);
                model.addAttribute("key", keyword);
                model.addAttribute("username", username);
            }
        }
        return "request";
    }
    //Function for admin to accept request of student
    @GetMapping("/accept/{id}")
    public String showAccept(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            Request req = reqService.get(id);
            req.setStatus("approved");
            Room roomNew = rservice.get(req.getNewRoom());
            Room roomOld = rservice.get(req.getOldRoom());
            int newCurrent = roomNew.getCurrent();
            int oldCurrent = roomOld.getCurrent();
            User user = uService.get(req.getSid());
            //check last room of student
            if (req.getOldRoom().equals("null")) {
                newCurrent++;
                roomNew.setCurrent(newCurrent);
            } else {
                //check new room of student
                if (req.getNewRoom().equals("null")) {
                    oldCurrent--;
                    roomOld.setCurrent(oldCurrent);
                }
                //check new room of student
                if (!req.getNewRoom().equals("null")) {
                    oldCurrent--;
                    newCurrent++;
                    roomOld.setCurrent(oldCurrent);
                    roomNew.setCurrent(newCurrent);
                }

            }
            user.setDateBook(java.time.LocalDate.now());
            user.setStatus("chua thanh toan");
            user.setRoom(roomNew);
            reqService.save(req);
            uService.save(user);
            rservice.save(roomNew);
            rservice.save(roomOld);

            return "redirect:/request/1";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "add fail");
            return "redirect:/request/1";
        }
    }
    //Function for admin to denied request of student
    @GetMapping("/denied/{id}")
    public String showDenied(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            Request req = reqService.get(id);
            req.setStatus("denied");
            reqService.save(req);
            return "redirect:/request/1";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "add fail");
            return "redirect:/request/1";
        }
    }
    //Function for admin can see all student each room
    @GetMapping("listmember/{id}")
    public String showListMember(Model model, @PathVariable("id") String id, RedirectAttributes ra) {
        List<User> listUser = uService.listUserByRid(id);
        model.addAttribute("list", listUser);
        model.addAttribute("id", id);
        return "listMember";
    }
    //Function for admin to delete each student from each room
    @GetMapping("/delete/{id}/{rid}")
    public String showDelete(@PathVariable("id") Integer id, @PathVariable("rid") String rid, RedirectAttributes ra) {
        try {
            User u = uService.get(id);

            Room r = new Room();
            r.setRid("null");
            u.setRoom(r);
            Room room = rservice.get(rid);
            int i = room.getCurrent();
            i--;
            room.setCurrent(i);
            rservice.save(room);
            uService.save(u);
            return "redirect:/book/1";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "add fail");
            return "redirect:/book/1";
        }
    }
    //Function for student to see their profile
    @GetMapping("/profile")
    public String showYouProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userss");
        model.addAttribute("user", user);
        return "profile";
    }
    //Function for student to edit their profile
    @PostMapping("/profile")
    public String showEditProfile(User user, @RequestParam("fileImage") MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhotos(fileName);
        uService.save(user);
        return "profile";

    }
    //function for admin can see list all student
    @GetMapping("/listStudent/{pageNumber}")
    public String showListStudent(HttpSession session, Model model, @PathVariable("pageNumber") int currentPage, @Param("keyword") String keyword) {
        Page<User> page = uService.listAllUser(keyword, currentPage);
        List<User> listUser = page.getContent();
        long totalItem = page.getTotalElements();
        int totalPage = page.getTotalPages();
        //check list student is empty
        if (!listUser.isEmpty()) {
            model.addAttribute("listUser", listUser);
            model.addAttribute("totalItem", totalItem);
            model.addAttribute("totalPage", totalPage);
            model.addAttribute("key", keyword);

        }
        return "listStudent";
    }
    //Function for admin to notify user
    @GetMapping("/notify")
    public String showListNotify() {
        LocalDate fakeDate = LocalDate.parse("2023-01-06");

        List<User> listUser = uService.listUserByFakeDate(fakeDate);
        //Get all user have not paid
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getStatus().equals("chua thanh toan")) {
                Notification n = new Notification();
                User u = new User();
                u.setId(listUser.get(i).getId());
                n.setUser(u);
                n.setMessage("vui long thanh toan tien phong");

                nService.save(n);
            }

        }

        return "redirect:/listStudent/1";
    }
    //Function for admin to cancel room each student
    @GetMapping("/cancel")
    public String showCancel() throws UserNotFoundException {
        LocalDate fakeDate = LocalDate.parse("2022-12-24");
        List<User> listUser = uService.listUserByFakeDate(fakeDate);
        //Get all user have not paid lately and notify to them
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getStatus().equals("chua thanh toan")) {
                Notification n = new Notification();
                User u = new User();
                u.setId(listUser.get(i).getId());
                n.setUser(u);
                n.setMessage("ban da bi huy phong");

                nService.save(n);

                Room roomOld = rservice.get(listUser.get(i).getRoom().getRid());
                int current = roomOld.getCurrent();
                current--;
                roomOld.setCurrent(current);
                rservice.save(roomOld);
                Room r = new Room();
                r.setRid("null");
                listUser.get(i).setRoom(r);
                listUser.get(i).setCoin(listUser.get(i).getCoin() - 600);
                listUser.get(i).setStatus("");
                listUser.get(i).setDateBook(null);
                uService.save(listUser.get(i));
            }

        }

        return "redirect:/listStudent/1";
    }

    //Function for student paying for their room
    @GetMapping("/payment")
    public String showPayMent(HttpSession session) {
        User user = (User) session.getAttribute("userss");
        double currentCoin = user.getCoin();
        double newCoin = currentCoin - 600;
        //check student hava enough money
        if (newCoin >= 0) {
            user.setStatus("da thanh toan");
            user.setCoin(newCoin);
            uService.save(user);
        }


        return "redirect:/profile";
    }
    //Function for admin sent email for all user have not paid
    @GetMapping("/checkemail")
    public String showcheckEmail() throws ParseException {
        LocalDate fakeDate = LocalDate.parse("2023-01-05");
        List<User> listUser = uService.listUserByFakeDate(fakeDate);
        //Get all user have not paid
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getStatus().equals("chua thanh toan")) {
                checkMail.sendEmail(listUser.get(i).getEmail(), "Thông báo đóng tiền", "Vui lòng thanh ton tiền");
            }

        }

        return "booking";
    }
    //Function for admin can export excel file list all student information
    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = uService.ListAllUserNotByPage();

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        excelExporter.export(response);
    }
    //Funtion for admin can handle email to sent user
    @GetMapping("/sentEmail")
    public String showSentEmail() throws ParseException {

        return "sheduleMail";
    }
    //Funtion save mail of admin to database
    @PostMapping("/sentEmail")
    public String showListEmail(@RequestParam("content") String content, @RequestParam("date") String date) {
        String newDate = date.replace('T', ' ');
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date1 = LocalDateTime.parse(newDate, formatter);
        Mail m = new Mail();
        m.setContent(content);
        m.setDateSent(date1);
        mService.save(m);
        return "redirect:/booking";
    }


}
