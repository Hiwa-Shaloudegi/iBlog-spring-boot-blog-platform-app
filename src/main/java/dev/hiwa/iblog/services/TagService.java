package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.request.CreateTagsRequest;
import dev.hiwa.iblog.domain.dto.response.TagDto;
import dev.hiwa.iblog.domain.entities.Tag;
import dev.hiwa.iblog.exceptions.ResourceConstraintViolationException;
import dev.hiwa.iblog.exceptions.ResourceNotFoundException;
import dev.hiwa.iblog.mappers.TagMapper;
import dev.hiwa.iblog.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagDto> getAllTagsWithPosts() {
        List<Tag> tags = tagRepository.findAllWithPosts();

        return tags.stream().map(tagMapper::toDto).toList();
    }

    @Transactional
    public List<TagDto> createTags(CreateTagsRequest request) {
        Set<Tag> existingTags = tagRepository.findAllByNameIn(request.getNames());
        Set<String> existingNames = existingTags.stream().map(Tag::getName).collect(Collectors.toSet());

        Set<String> namesToSave = new HashSet<>(request.getNames());
        namesToSave.removeAll(existingNames);

        Set<Tag> tagsToSave = namesToSave.stream().map(name -> {
            var tag = new Tag();
            tag.setName(name);
            return tag;
        }).collect(Collectors.toSet());

        List<Tag> savedTags = new ArrayList<>();
        if (!tagsToSave.isEmpty()) {
            savedTags = tagRepository.saveAll(tagsToSave);
        }
        savedTags.addAll(existingTags);
        return savedTags.stream().map(tagMapper::toDto).toList();
    }

    public TagDto getTagById(UUID tagId) {
        Tag tag = tagRepository
                .findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId.toString()));

        return tagMapper.toDto(tag);
    }

    @Transactional
    public List<TagDto> getTagByIds(Set<UUID> ids) {
        List<Tag> tags = tagRepository.findAllById(ids);

        if (ids.size() != tags.size())
            throw new ResourceNotFoundException("Tag", "ids", "Some tag IDs not found");

        return tags.stream().map(tagMapper::toDto).toList();
    }


    public void deleteTagById(UUID id) {
        Tag tag = tagRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id.toString()));


        if (!tag.getPosts().isEmpty()) throw new ResourceConstraintViolationException(
                "Tag cannot be deleted because it has associated posts.");

        tagRepository.deleteById(id);
    }

}
