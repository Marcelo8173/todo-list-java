package br.com.marceloandre.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.marceloandre.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if(servletPath.startsWith("/task")) {
            var authorization = request.getHeader("Authorization");
            byte[] authDecoded =  Base64.getDecoder().decode(authorization.substring("Basic".length()).trim());
            String authString = new String(authDecoded);

            String[] credentials = authString.split(":");
            String userName = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUserName(userName);

            if(user == null) {
                response.sendError(401, "Usuario sem autorização");
            } else {
                var passwordVerify =  BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request,response);
                } else {
                    response.sendError(401, "Usuario sem autorização");
                }
            }


        } else {
            filterChain.doFilter(request,response);
        }
    }
}
