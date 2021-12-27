package com.fbytes.contest.Contest.HashGen;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class HashGen implements IHashGen {
    @Override
    public String genHash(String inputStr) {
        return DigestUtils.md5DigestAsHex(inputStr.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }
}
