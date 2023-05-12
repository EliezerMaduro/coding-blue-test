package com.eliezer.urlshortener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eliezer.urlshortener.repository.RedisRepository;

@RestController()
@RequestMapping("/api")
public class Controller {
    @Autowired
    public RedisRepository redisRepository;

    @PostMapping("/a")
    public String postUrl(@RequestBody String url){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(url.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (int i = 0; i<4; i++){
                int value = hashBytes[i] & 0xFF;
                String hex = Integer.toHexString(value);
                if (hex.length() == 1){
                    hash.append('0');
                }
                hash.append(hex);
            }
            redisRepository.saveUrl(hash.toString(), url);
            return String.format("http://localhost:8080/api/a/%s", hash.toString());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return "exepction";
        }catch(Exception e) {
            e.printStackTrace();
            return "exception";
        }
    }

    @GetMapping("/a/{id}")
    public String getOriginalUrl(@PathVariable String id){
        return redisRepository.getUrl(id);
    }

}
