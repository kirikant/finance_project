package mail.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

   private final UserDetailsService userDetailsService;


    @Value("${jwt.secret}")
    String secret;
    @Value("${jwt.time}")
    long validityDays;


    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public  String createToken(String username,String role){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role",role);
        Date date = new Date();
        Date date1 = new Date(date.getTime()+ TimeUnit.DAYS.toMillis(validityDays));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(date1)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token){
        Jws<Claims> claimsJws= Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody()
                .getSubject();

    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request){
return  request.getHeader("Authorization");
    }
}