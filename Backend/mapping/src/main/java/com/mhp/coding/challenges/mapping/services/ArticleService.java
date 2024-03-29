package com.mhp.coding.challenges.mapping.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mhp.coding.challenges.mapping.mappers.ArticleMapper;
import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository;

@Service
public class ArticleService {

	private final ArticleRepository repository;

	private final ArticleMapper mapper;

	@Autowired
	public ArticleService(ArticleRepository repository, ArticleMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	public List<ArticleDto> list() {
		final List<ArticleDto> articlesDto = new ArrayList<>();
		repository.all().forEach(article -> articlesDto.add(mapper.map(article)));
		return articlesDto;
	}

	public ArticleDto articleForId(Long id) {
		final Optional<Article> article = repository.findBy(id);
		if (article.isPresent()) {
			return mapper.map(article.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article was not found.");
		}
	}

	public ArticleDto create(ArticleDto articleDto) {
		final Article create = mapper.map(articleDto);
		repository.create(create);
		return mapper.map(create);
	}
}
