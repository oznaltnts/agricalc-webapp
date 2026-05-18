package tr.ozanbey.agricalc.webapp.webapp.security;

import jakarta.faces.FacesException;
import jakarta.faces.FactoryFinder;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.faces.render.RenderKit;
import jakarta.faces.render.RenderKitFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.primefaces.PrimeFaces;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;

@Component
@SessionScope
public class JsfAjaxTimeoutPhaseListener implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        PrimeFaces pr = PrimeFaces.current();
        ExternalContext ec = fc.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();

        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(o instanceof CurrentUser)) {
            // user credential not found.
            // considered to be a Timeout case
            if (ec.isResponseCommitted()) {
                // redirect is not possible
                return;
            }
            try {
                if (checkRequestComingFromSecurePages(request.getRequestURI())
                        && ((pr != null && pr.isAjaxRequest()) || (fc.getPartialViewContext().isPartialRequest()))
                        && fc.getResponseWriter() == null && fc.getRenderKit() == null) {

                    response.setCharacterEncoding(request.getCharacterEncoding());
                    RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

                    RenderKit renderKit = factory.getRenderKit(fc, fc.getApplication().getViewHandler().calculateRenderKitId(fc));
                    ResponseWriter responseWriter = renderKit.createResponseWriter(response.getWriter(), null, request.getCharacterEncoding());
                    fc.setResponseWriter(responseWriter);
                    ec.redirect("/");
                }
            } catch (IOException e) {
                throw new FacesException(e);
            }
        } else {
            return; //This is not a timeout case . Do nothing !
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public boolean checkRequestComingFromSecurePages(String path) {
        return !path.startsWith("/public") && !path.startsWith("/common");
    }

}
