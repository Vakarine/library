package my.shop.common.fliPusto.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalController {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor trimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, trimmerEditor);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView accessDeniedExceptionHandler(Exception ex) {
        ModelAndView mv = new ModelAndView();
        mv.setStatus(HttpStatus.NOT_ACCEPTABLE);
        mv.addObject("errorText", ex.getMessage());
        mv.setViewName("errorPage");

        return mv;
    }


}
