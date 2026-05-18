package tr.ozanbey.agricalc.webapp.service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tr.ozanbey.agricalc.webapp.webapp.security.CurrentUser;
import tr.ozanbey.agricalc.webapp.webapp.util.ServerAddress;

import java.io.Serializable;

@Service
public class BaseService implements Serializable {

    public String getIpAddress() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return ServerAddress.getServerIp(httpServletRequest);
    }

    public CurrentUser getCurrentUser() {
        return (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
