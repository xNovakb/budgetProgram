package sk.mtaa.budgetProgram.Filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.mtaa.budgetProgram.Constants.Constants;

import java.io.IOException;

public class AuthentificationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            String[] authHeaderArr = authHeader.split("Bearer ");
            if (authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                try {
                    Claims claims  = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();
                    request.setAttribute("userId", Integer.parseInt(claims.get("id").toString()));
                    request.setAttribute("email", claims.get("email"));
                    request.setAttribute("role", claims.get("role"));
                } catch (Exception e){
                    response.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired token");
                    return;
                }
            } else {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token ]");
                return;
            }
        } else {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
