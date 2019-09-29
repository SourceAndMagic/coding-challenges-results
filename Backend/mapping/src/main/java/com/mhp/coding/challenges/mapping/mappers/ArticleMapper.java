package com.mhp.coding.challenges.mapping.mappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.db.Image;
import com.mhp.coding.challenges.mapping.models.db.blocks.ArticleBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.GalleryBlock;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.models.dto.ImageDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ArticleBlockDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.GalleryBlockDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock;
import com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock;
import com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock;

@Component
public class ArticleMapper {

	public ArticleDto map(Article article) {
		final ArticleDto articleDto = new ArticleDto();
		articleDto.setId(article.getId());
		articleDto.setTitle(article.getTitle());
		articleDto.setDescription(article.getDescription());
		articleDto.setAuthor(article.getAuthor());

		final List<ArticleBlockDto> blocksDto = new ArrayList<>();
		article.getBlocks().stream().forEach(block -> blocksDto.add(map(block)));
		blocksDto.sort(Comparator.comparing(ArticleBlockDto::getSortIndex));
		articleDto.setBlocks(blocksDto);

		return articleDto;
	}

	public Article map(ArticleDto articleDto) {
		// Nicht Teil dieser Challenge.
		return new Article();
	}

	private ArticleBlockDto map(ArticleBlock block) {
		ArticleBlockDto articleBlockDto;
		
		if (block instanceof GalleryBlock) {
			articleBlockDto = new GalleryBlockDto();
			final List<ImageDto> imagesDto = new ArrayList<>();
			((GalleryBlock) block).getImages().stream().forEach(image -> imagesDto.add(map(image)));
			((GalleryBlockDto) articleBlockDto).setImages(imagesDto);

		} else if (block instanceof com.mhp.coding.challenges.mapping.models.db.blocks.ImageBlock) {
			articleBlockDto = new ImageBlock();
			((ImageBlock) articleBlockDto)
					.setImage(map(((com.mhp.coding.challenges.mapping.models.db.blocks.ImageBlock) block).getImage()));

		} else if (block instanceof com.mhp.coding.challenges.mapping.models.db.blocks.TextBlock) {
			articleBlockDto = new TextBlock();
			((TextBlock) articleBlockDto)
					.setText(((com.mhp.coding.challenges.mapping.models.db.blocks.TextBlock) block).getText());

		} else if (block instanceof com.mhp.coding.challenges.mapping.models.db.blocks.VideoBlock) {
			articleBlockDto = new VideoBlock();
			((VideoBlock) articleBlockDto)
					.setUrl(((com.mhp.coding.challenges.mapping.models.db.blocks.VideoBlock) block).getUrl());
			((VideoBlock) articleBlockDto)
					.setType(((com.mhp.coding.challenges.mapping.models.db.blocks.VideoBlock) block).getType());
			
		} else {
			articleBlockDto = new ArticleBlockDto();
			System.out.println(String.format("No specific mapping for class %s implemented. Using common mapping.",
					block.getClass().getName()));
			
		}
		
		articleBlockDto.setSortIndex(block.getSortIndex());
		return articleBlockDto;
	}

	private ImageDto map(Image image) {
		ImageDto imageDto = new ImageDto();
		imageDto.setId(image.getId());
		imageDto.setImageSize(image.getImageSize());
		imageDto.setUrl(image.getUrl());
		return imageDto;
	}

}
