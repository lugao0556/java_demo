package com.example.demo.Controller;

import com.example.demo.UserEndpoints;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserAccess {
    private static final  String template = "Login success, %s";


    @PostMapping("/admin/addUser")
    public ResponseEntity<String> addUser(@RequestHeader(value = "role",defaultValue = "Null") String role,
                                          @RequestBody UserEndpoints body) throws IOException {
        if (!role.equals("admin")) {
            return ResponseEntity.badRequest().body("not a admin user");
        } else {
            String user = body.userId;
            List<String> eps = body.endpoint;
            System.out.println(eps.size());
            save(user, eps);
            return ResponseEntity.status(HttpStatus.CREATED).body("created");
        }

    }

    @GetMapping("/user/*")
    public ResponseEntity<String> checkAccess(@RequestHeader(value = "role",defaultValue = "Null") String role,
                                              @RequestHeader(value = "userId",defaultValue = "Null") String userId,
                                              HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String[] requests = requestURL.split("/");

        if (role.equals("admin")) {
            return ResponseEntity.status(HttpStatus.OK).body("succuss");
        } else {
            boolean isExisted = check(userId, requests[requests.length-1]);
            System.out.println(requestURL + " " + isExisted);
            if (isExisted == true) {
                return ResponseEntity.status(HttpStatus.OK).body("succuss");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("faied");
            }
        }
    }


    private void save(String user, List<String> endpoints) throws IOException {
        FileWriter fw = new FileWriter("src/main/resources/user_resources_mapping.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (String ep : endpoints) {
            System.out.println(user.concat(":").concat(ep));
            bw.write(user.concat(":").concat(ep).concat("\n"));
        }
        bw.close();
    }

    private boolean check(String userId, String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/user_resources_mapping.txt"));
        String line = br.readLine();
        while (line != null) {
            System.out.println(line + " " + userId + " " + path);
            String[] userAndEp = line.split(":");
            if (userAndEp[0].equals(userId) && userAndEp[1].equals(path)) {return  true;}
            line = br.readLine();
        }
        br.close();
        return  false;
    }



}
