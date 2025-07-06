package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.request.CreateTagsRequest;
import dev.hiwa.iblog.domain.dto.response.TagResponse;
import dev.hiwa.iblog.domain.entities.Tag;
import dev.hiwa.iblog.mappers.TagMapper;
import dev.hiwa.iblog.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagResponse> getAllTagsWithPosts() {
        List<Tag> tags = tagRepository.findAllWithPosts();

        return tags.stream().map(tagMapper::toTagResponse).toList();
    }

    @Transactional
    public List<TagResponse> createTags(CreateTagsRequest request) {
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
        return savedTags.stream().map(tagMapper::toTagResponse).toList();
    }
}
