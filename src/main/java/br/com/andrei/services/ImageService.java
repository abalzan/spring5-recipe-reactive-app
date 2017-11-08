package br.com.andrei.services;

import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Mono;

public interface ImageService {

	Mono<Void> saveImageFile(String id, MultipartFile file);

}
