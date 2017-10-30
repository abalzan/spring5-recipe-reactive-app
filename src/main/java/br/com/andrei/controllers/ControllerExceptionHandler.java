package br.com.andrei.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.andrei.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice //proporciona que eu lance uma exception para todo e qualquer controller evitando codigo duplicado
public class ControllerExceptionHandler {

//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(NumberFormatException.class)
//	public ModelAndView handleNumberFormat(Exception exception) {
//		
//		log.error("Handling number format exception");
//		log.error(exception.getMessage());
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		modelAndView.setViewName("400error");
//		modelAndView.addObject("exception", exception);
//		
//		return modelAndView;
//	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(Exception exception) {

		log.error("Handling not found exception");
		log.error(exception.getMessage());

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("404error");
		modelAndView.addObject("exception", exception);

		return modelAndView;
	}

}
