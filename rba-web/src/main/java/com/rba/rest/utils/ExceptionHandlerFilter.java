package com.rba.rest.utils;

import com.rba.common.exceptions.AppException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws RuntimeException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AppException e) {
            response.getWriter().write( ResponseMessage.packageAndJsoniseError(e.getError()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//		catch (JwtException e) {
//			response.getWriter().write( ResponseMessage.packageAndJsoniseError(TimunError.SESSION_EXPIRED));
//		}
    }
}
