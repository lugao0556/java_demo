package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class UserEndpoints {
    public String userId;
    public List<String> endpoint = new ArrayList<>();

    public UserEndpoints(String u, List<String> eps) {
        this.userId = u;
        this.endpoint = eps;
    }
}
