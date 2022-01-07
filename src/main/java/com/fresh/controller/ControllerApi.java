package com.fresh.controller;

import cn.hutool.core.io.IoUtil;
import com.fresh.annotation.MAutowired;
import com.fresh.bean.MBean;
import com.fresh.entity.Users;
import com.fresh.entity.UsersVO;
import com.fresh.listen.MyApiEvent;
import com.fresh.service.UsersService;
import com.fresh.utils.AddressUtils;
import com.fresh.utils.AppClientApi;
import com.fresh.utils.IpUtils;
import com.fresh.utils.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.*;

@RequestMapping("/index")
@Controller
@RequiredArgsConstructor
@Api("接口测试")
public class ControllerApi {

    @Autowired
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private SpringContextUtil contextUtil;


    @GetMapping("/error")
    @CrossOrigin
    @ResponseBody
    public String error() {
        return "页码不见啦";
    }

    @GetMapping(value = "/sendMessage", produces = "application/octet-stream;charset=UTF-8")
    @CrossOrigin
    public void sendMessage(HttpServletResponse response) throws IOException {
        File img = new File("E://a.jpg");
        FileInputStream imgStream = new FileInputStream(img);
        response.setHeader("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".jpg");
        OutputStream os = response.getOutputStream();
        org.apache.commons.io.IOUtils.copy(imgStream, os);
        imgStream.close();
//        os.close();
    }


    @RequestMapping(value = "upload", method = {RequestMethod.POST, RequestMethod.GET})
    @CrossOrigin
    public Map<String, Object> methodApi(@RequestParam("file") List<MultipartFile> multipartFile) throws IOException {

        List<String> list = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = UUID.randomUUID().toString();
            // 本地上传
            String fileUrl = "E://img//" + uuid + "." + ext;
            File f = new File(fileUrl);
            f.createNewFile();
            FileOutputStream os = new FileOutputStream(f);
            InputStream is = file.getInputStream();
            IOUtils.copy(is, os);
            is.close();
            os.flush();
            os.close();

            list.add(fileUrl);
        }

        Map<String, Object> file = new HashMap<>();
        file.put("url", list);

        Map<String, Object> data = new HashMap<>();
        data.put("response", file);

        return data;
    }


    @GetMapping("/event")
    @ResponseBody
    public String event() {
        ApplicationContext context = contextUtil.getApplicationContext();
        context.publishEvent(new MyApiEvent("hahhaha"));

        return "OK";
    }


    @PostMapping("testApi")
    @ResponseBody
    @CrossOrigin
    public Users testApi(@RequestBody Map<String, String> hmp, HttpServletRequest request) {
        Users users = new Users();
        users.setName(hmp.get("name"));

        return users;
    }


    @GetMapping("ok")
    @ResponseBody
    @CrossOrigin
    public String ok(@RequestParam(required = false, defaultValue = "") String id, HttpServletRequest request) {
        System.out.println(id);
        String ipAddress = IpUtils.getIpAddress(request);
        System.out.println(ipAddress);
        String address = AddressUtils.getAddress(ipAddress);
        System.out.println("IP:" + ipAddress);
        System.out.println("详细：" + address);
        return address;
    }

    @GetMapping("img")
    @ResponseBody
    @CrossOrigin
    public void img(HttpServletResponse response) throws IOException {
        File file = new File("E://a.jpg");
        ServletOutputStream os = response.getOutputStream();
        IoUtil.copy(new FileInputStream(file), os);
        return;
    }


    @GetMapping("date")
    @ResponseBody
    @CrossOrigin
    public Date date(Date date) throws IOException {
        System.out.println(date);
        Date current = new Date();
        return current;
    }


    @GetMapping(path = "haha")
    @CrossOrigin
    @AppClientApi
    public Users ping2(HttpServletRequest request) throws Exception {
        Users users = new Users();
        users.setName("哈哈哈");


        return users;
    }

    @GetMapping(path = "ping")
    @CrossOrigin
    public ResponseEntity<?> ping(HttpServletRequest request) throws Exception {

        RequestMappingHandlerMapping requestMap = contextUtil.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
        HandlerExecutionChain currentHandler = requestMap.getHandler(request);
        HandlerMethod handler = (HandlerMethod) currentHandler.getHandler();

        if (handler.hasMethodAnnotation(GetMapping.class)) {
            GetMapping annotation = handler.getMethodAnnotation(GetMapping.class);
            System.out.println(annotation.value().toString());
            System.out.println(annotation.path().toString());
            System.out.println(annotation.name().toString());
        }


        Users users = new Users();
        return ResponseEntity.ok(users);
    }


    @PostMapping("postFile")
    @CrossOrigin
    @ResponseBody
    @ApiModelProperty(value = "上传")
    public String postFile(@RequestParam("file") MultipartFile file) {
        ClassLoader classLoader = this.getClass().getClassLoader();

        URL resource = classLoader.getResource("");

        System.out.println(file.getName());
        return "OK";
    }


    @PostMapping("postMany")
    @CrossOrigin
    @ResponseBody
    public String postMany(@RequestBody UsersVO usersVO) {
        System.out.println(usersVO);
        return "OK";
    }

    @Autowired
    private UsersService usersService;

    //    @Autowired
//    private SpringContextUtil contextUtil;
    @MAutowired
    private MBean mBean;

    @GetMapping("getUsers")
    @CrossOrigin
    @ResponseBody
    public Users transThread(@RequestParam(defaultValue = "", required = true) Long id) {
        return usersService.getUsers(id);
    }
}
