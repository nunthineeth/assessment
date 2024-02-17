package com.kbtg.bootcamp.posttest.securityconfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private String SECRET_KEY = "secrefoejfoejfojeofjeofgjoswgjs;gjwp[gjq[ek[pkvp[wjp[kp[vp[wkegt";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<GrantedAuthority> getAuthorities(String token){
        List<String> authorities = extractClaim(token, claims -> claims.get("authorities", List.class));
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return (Claims) Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parse(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("authorities", authorities);

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) //username or email
                .setIssuedAt(new Date(System.currentTimeMillis())) //create time
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5))) //expire time
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

   public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
