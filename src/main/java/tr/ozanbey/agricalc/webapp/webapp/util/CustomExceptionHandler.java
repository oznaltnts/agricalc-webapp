package tr.ozanbey.agricalc.webapp.webapp.util;

import jakarta.faces.FacesException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;
import org.springframework.core.env.Environment;
import tr.ozanbey.agricalc.webapp.webapp.controller.NavigationController;
import tr.ozanbey.agricalc.webapp.webapp.util.context.SpringContextAccessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private final Environment env = SpringContextAccessor.getBean(Environment.class);
    private final NavigationController navigationController = SpringContextAccessor.getBean(NavigationController.class);

    CustomExceptionHandler(ExceptionHandler exception) {
        wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            final FacesContext fc = FacesContext.getCurrentInstance();

            try {
                if ("default".equals(env.getActiveProfiles()[0]) || "test".equals(env.getActiveProfiles()[0]) || "production".equals(env.getActiveProfiles()[0])) {
                    System.out.println(t.getMessage());
                    System.out.println(convertStackTraceToString((Exception) t));
                }
                navigationController.redirectToError();
                fc.responseComplete();

            } catch (Exception e) {
                //e.printStackTrace();
            } finally {
                i.remove();
            }
        }
        getWrapped().handle();
    }

    private String convertStackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
