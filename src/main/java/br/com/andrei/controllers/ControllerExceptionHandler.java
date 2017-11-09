package br.com.andrei.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.thymeleaf.exceptions.TemplateInputException;

import br.com.andrei.exceptions.InternalServerError;
import br.com.andrei.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice //proporciona que eu lance uma exception para todo e qualquer controller evitando codigo duplicado
public class ControllerExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(WebExchangeBindException.class)
	public String handleNumberFormat(Exception exception, Model model) {
		
		log.error("Handling Binding exception");
		log.error(exception.getMessage());
		
		model.addAttribute("exception", exception);
		
		return "400error";
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({NotFoundException.class, TemplateInputException.class})
	public String handleNotFound(Exception exception, Model model) {

		log.error("Handling not found exception");
		log.error(exception.getMessage());

		model.addAttribute("exception", exception);
		
		return "404error";
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InternalServerError.class)
	public void handleInternalServerError(Exception exception) {

//		log.error("Handling not found exception");
//		log.error(exception.getMessage());
//
//		ModelAndView modelAndView = new ModelAndView();
//
//		modelAndView.setViewName("500error");
//		modelAndView.addObject("exception", exception);
//
//		return modelAndView;
	}

}
