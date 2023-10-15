package br.com.cunhaedu.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.stereotype.Component;

import br.com.cunhaedu.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
      var serveltPath = request.getServletPath();

      if(!serveltPath.startsWith("/tasks")) {
        filterChain.doFilter(request, response);
        return;
      }

      var authorization = request.getHeader("Authorization");

      if(authorization == null) {
        response.sendError(401);
        return;
      }

      var base64Authorization = authorization.substring("Basic".length()).trim();
      byte[] authDecoded = Base64.getDecoder().decode(base64Authorization);
      var credentials = new String(authDecoded).split(":");

      var username = credentials[0];
      var password = credentials[1];

      var user = this.userRepository.findByUsername(username);

      if(user == null ) {
        response.sendError(401);
        return;
      }

      var passwordVerified = BCrypt
        .verifyer()
        .verify(password.toCharArray(), user.getPassword());

      if(!passwordVerified.verified) {
        response.sendError(401);
        return;
      }

      request.setAttribute("userId", user.getId());
      filterChain.doFilter(request, response);
  }
}
